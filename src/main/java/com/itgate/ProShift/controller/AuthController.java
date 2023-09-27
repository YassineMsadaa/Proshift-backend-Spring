package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.*;
import com.itgate.ProShift.payload.request.LoginRequest;
import com.itgate.ProShift.payload.request.NewPasswordRequest;
import com.itgate.ProShift.payload.request.SignupRequest;
import com.itgate.ProShift.payload.response.JwtResponse;
import com.itgate.ProShift.payload.response.MessageResponse;
import com.itgate.ProShift.repository.JwtTokenRepository;
import com.itgate.ProShift.repository.MembreEntrepriseRepository;
import com.itgate.ProShift.repository.RoleRepository;
import com.itgate.ProShift.repository.UserRepository;
import com.itgate.ProShift.security.jwt.JwtUtils;
import com.itgate.ProShift.security.services.UserDetailsImpl;
import com.itgate.ProShift.service.interfaces.IJwtTokenService;
import com.itgate.ProShift.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  MembreEntrepriseRepository meR;
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  IJwtTokenService jwtTokenService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  RoleRepository roleRepository;
  @Autowired
  JwtTokenRepository jwtTokenRepository;
  @Autowired
  UserService userService;
  @Autowired
  PasswordEncoder encoder;
  @Autowired
  JwtUtils jwtUtils;




  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws BadCredentialsException {
    User user =userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
    if (user!=null){
      if (user.getVerificationCode()!=null) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Erreur: Ce compte n'est pas encore vérifié par e-mail!\n Veuillez vérifier votre e-mail"));
      }
      if (user.isBlocked()) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Erreur: Ce compte est bloqué!\n veuillez contacter le responsable RH, directement ou a traver notre email: Go4Dev@outlook.com."));
      }
      if (!user.isApproved()) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Erreur: Ce compte n'est pas encore approuvé!\n veuillez contacter le responsable RH, directement ou a traver notre email: Go4Dev@outlook.com."));
      }

    }else {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Erreur: Pas de compte avec ce Username!\n Veuillez vérifier"));
    }

    try {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    // Generate JWT token
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    String jwt = jwtUtils.generateToken(userDetails);

    // Save JWT token to database
    JwtToken jwtToken = new JwtToken();
    jwtToken.setToken(jwt);
    jwtToken.setCreatedDate(jwtUtils.getIssuedDateFromToken(jwt));
    jwtToken.setExpirationDate(jwtUtils.getExpirationDateFromToken(jwt));
    jwtTokenRepository.save(jwtToken);

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }
    catch (BadCredentialsException e ){
    return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Wrong password !"));
  }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }
    ////////////////// Nid input control
    if (userRepository.existsByCin(signUpRequest.getCin())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: National ID is already taken!"));
    }
    MembreEntreprise me = meR.findByCin(signUpRequest.getCin());
    if (me == null) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Wrong Cin or you are not a member of the company"));
    }
    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));
    ERole erole;
    if (me.getRole() == "CHEF") {
      erole = ERole.ROLE_CHEF;
    } else if(me.getRole() == "COORDINATEUR") {
      erole = ERole.ROLE_COORDINATEUR;
    } else if(me.getRole() == "RESPONSABLE") {
      erole = ERole.ROLE_RESPONSABLE;
    } else {
      erole = ERole.ROLE_EMPLOYEE;
    }
    Set<Role> roles = new HashSet<>();
    Role clientRole = roleRepository.findByName(erole).get();
    roles.add(clientRole);
    /////////////////// save Employee with given input
    user.setRoles(roles);
    String randomCode = RandomString.make(64);
    user.setVerificationCode(randomCode);
    user.setCongeSolde(21);
    user.setCin(signUpRequest.getCin());
    user.setBlocked(false);
    user.setApproved(false);
    userRepository.save(user);
    /////////////////// send verification mail
    try{
      userService.sendVerificationEmail(user, userService.getSiteURL(request));
    }catch (Exception e ){
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Erreur : échec de l'envoi de l'e-mail de vérification!\n Veuillez contacter le responsable RH, directement ou a traver notre email: Go4Dev@outlook.com."));
    }
    return ResponseEntity.ok(new MessageResponse("User registered successfully!\n check your email to verify your account!"));
  }
  @GetMapping("/verify")
  public String verifyUser(@Param("code") String code) {
    if (userService.verify(code)) {
      return "verify_success";
    } else {
      return "verify_fail";
    }
  }

  @PostMapping("/forgotpassword/{email}")
  public ResponseEntity<?> forgotPassword( @PathVariable String email, HttpServletRequest request) {
    User user= userRepository.findByEmail(email);
    String randomCode = RandomString.make(64);
    user.setResetPasswordToken(randomCode);
    userRepository.save(user);
    try{
    userService.sendForgotPassword(user, userService.getSiteURL(request));
    }catch (Exception e ){
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Erreur : échec de l'envoi de l'e-mail de vérification! \n Veuillez réessayer ultérieurement."));
    }
    return ResponseEntity.ok(new MessageResponse("veuillez vérifier votre e-mail pour changer votre mot de passe !"));
  }

  @PostMapping("/resetPassword")
  public ResponseEntity<?> changePassword(@Param("code") String code,@RequestBody NewPasswordRequest newPasswordRequest) {
    if (userService.changePassword(code,encoder.encode(newPasswordRequest.getNewPassword()))) {

      return ResponseEntity
              .ok()
              .body(new MessageResponse(" Le mot de passe a été modifié avec succès !"));
    } else {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse(" Échec de la modification du mot de passe !"));
    }
  }
  @PostMapping("/signout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    String token = jwtUtils.getTokenFromRequest(request);
    JwtToken jwtToken=jwtTokenRepository.findByToken(token);
    if(jwtToken==null ){
      return ResponseEntity
              .ok()
              .body(new MessageResponse(" vous êtes déjà déconnecté !"));
    }
    jwtTokenRepository.deleteById(jwtToken.getId());
    return ResponseEntity
            .ok()
            .body(new MessageResponse( "Déconnexion réussie"));
  }

  @GetMapping("/current-users")
  public ResponseEntity<List<User>> getCurrentUsers() {
    List<JwtToken> activeTokens = jwtTokenService.getAllActiveTokens();
    List<User> users = new ArrayList<>();
    for (JwtToken token : activeTokens) {
      String username =jwtUtils.getUsernameFromToken(token.getToken());
      users.add(userService.findByUsername(username));
    }
    return ResponseEntity.ok(users);
  }
}

