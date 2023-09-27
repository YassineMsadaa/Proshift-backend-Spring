package com.itgate.ProShift.service;

import com.itgate.ProShift.entity.*;
import com.itgate.ProShift.repository.RoleRepository;
import com.itgate.ProShift.repository.UserRepository;
import com.itgate.ProShift.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JavaMailSender mailSender;
    @Override
    public List<User> findAllUser() {
        List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        return userList;
    }

    @Override
    public List<User> findUserByRole(ERole role) {
        Role role1 = roleRepository.findByName(role).orElse(null);
        List<User> users= new ArrayList<>();
        userRepository.findUserByRoles(role1).forEach(users::add);
        return users;
    }

    @Override
    public User blockUser(Long idUser) {
        User user =userRepository.findById(idUser).orElse(null);
        user.setBlocked(true);

        return userRepository.save(user);
    }

    @Override
    public User unBlockUser(Long idUser) {
        User user =userRepository.findById(idUser).orElse(null);
        user.setBlocked(false);

        return userRepository.save(user);
    }
    @Override
    public User checkin(Long idUser) {
        User user =userRepository.findById(idUser).orElse(null);
        user.setCheckin(new Date());

        return userRepository.save(user);
    }
    @Override
    public User checkOut(Long idUser){
        User user =userRepository.findById(idUser).orElse(null);
        user.setCheckin(null);
        return userRepository.save(user);
    }


    @Override
    public User updateUser(User user) {
        User user1 = userRepository.findById(user.getId()).orElse(null);
        Set<Role> role = user.getRoles();
        Set<Role> role2 = new HashSet<>();
        role2.add(roleRepository.findByName(role.iterator().next().getName()).get());
        user1.setRoles(role2);
        if (user1 != null) {
            if (user.getUsername() != null) {
                user1.setUsername(user.getUsername());
            }

            if (user.getEmail() != null) {
                user1.setEmail(user.getEmail());
            }

            if (user.getPassword() != null) {
                user1.setPassword(user.getPassword());
            }

            // Set other non-null properties accordingly
            if (user.getCin() != null) {
                user1.setCin(user.getCin());
            }


           user1.setStatut(user.isStatut());


            if (user.getResetPasswordToken() != null) {
                user1.setResetPasswordToken(user.getResetPasswordToken());
            }

            if (user.getVerificationCode() != null) {
                user1.setVerificationCode(user.getVerificationCode());
            }

            user1.setBlocked(user.isBlocked());

            if (user.getMatricule() != null) {
                user1.setMatricule(user.getMatricule());
            }

            if (user.getNom() != null) {
                user1.setNom(user.getNom());
            }

            if (user.getPrenom() != null) {
                user1.setPrenom(user.getPrenom());
            }

            if (user.getDateNaissance() != null) {
                user1.setDateNaissance(user.getDateNaissance());
            }

            if (user.getNumTelephone() != null) {
                user1.setNumTelephone(user.getNumTelephone());
            }

            if (user.getAdresse() != null) {
                user1.setAdresse(user.getAdresse());
            }

            if (user.getDateEmbauche() != null) {
                user1.setDateEmbauche(user.getDateEmbauche());
            }

            if (user.getUrgenceNom() != null) {
                user1.setUrgenceNom(user.getUrgenceNom());
            }

            if (user.getUrgenceNum() != null) {
                user1.setUrgenceNum(user.getUrgenceNum());
            }

            if (user.getCongeSolde() != null) {
                user1.setCongeSolde(user.getCongeSolde());
            }

            if (user.getCheckin() != null) {
                user1.setCheckin(user.getCheckin());
            }

            if (user.isApproved() != user1.isApproved()) {
                user1.setApproved(user.isApproved());
            }

            if (user.getPoste() != null) {
                user1.setPoste(user.getPoste());
            }

            if (user.getEtatCivil() != null) {
                user1.setEtatCivil(user.getEtatCivil());
            }

            if (user.getDepartement() != null) {
                user1.setDepartement(user.getDepartement());
            }

            // Set other properties accordingly


            return userRepository.save(user1);
        }
        return null; // Handle case when user1 is not found
    }




    @Override
    public User findUserbyId(Long idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    @Override
    public void removeUser(Long idUser) {
        userRepository.deleteById(idUser);
    }

    public String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "/auth");
    }



    public void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String toAddress = user.getEmail();
        String fromAddress = email;
        String senderName = "ProShift";
        String subject = "Veiller verifier votre compte ";
        String content = "cher [[name]],<br>"
                + "Veuillez cliquer sur le lien ci-dessous pour vérifier votre inscription :<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFIER</a></h3>"
                + "Merci,<br>"
                + "ProShift.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = "http://localhost:4200/auth/verif/" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null) {
            return false;
        } else {
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
    }


    public void sendForgotPassword(User user) throws MessagingException, UnsupportedEncodingException, MessagingException {
        String toAddress = user.getEmail();
        String fromAddress = email;
        String senderName = "Proshift";
        String subject = "Changement de mot de passe";
        String content = "Cher [[name]],<br>"
                + "Veuillez cliquer sur le lien ci-dessous pour votre mot de passe:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">CHANGER</a></h3>"
                + "Merci,<br>"
                + "ProShift.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getUsername());
        String changePasswordURL = "http://localhost:4200/auth/newpassword/" + user.getResetPasswordToken();
        content = content.replace("[[URL]]", changePasswordURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
    @Override
    public boolean changePassword(String verificationCode, String newPassword) {
        User user = userRepository.findByResetPasswordToken(verificationCode);

        if (user == null) {
            return false;
        } else {
            user.setResetPasswordToken(null);
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public boolean changePasswordByUser(Long id ,String password, String newPassword) {
        User user = userRepository.findById(id).get();

        if (!encoder.matches(password, user.getPassword())) {
            return false;
        }

        String encodedPassword = encoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return true;
    }
    @Override
    public User findByUsername(String username){
        return  userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<User> findUsersByDepartement(User.Departement departement) {
        return userRepository.findUsersByDepartement(departement);
    }
    @Override
    public User updateEmployee(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId()).get();
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getCin() != null) {
            existingUser.setCin(updatedUser.getCin());
        }
        if (updatedUser.getMatricule() != null) {
            System.out.println(updatedUser.getMatricule());
            existingUser.setMatricule(updatedUser.getMatricule());
        }
        if (updatedUser.getNom() != null) {
            existingUser.setNom(updatedUser.getNom());
        }
        if (updatedUser.getPrenom() != null) {
            existingUser.setPrenom(updatedUser.getPrenom());
        }
        if (updatedUser.getDateNaissance() != null) {
            existingUser.setDateNaissance(updatedUser.getDateNaissance());
        }
        if (updatedUser.getNumTelephone() != null) {
            existingUser.setNumTelephone(updatedUser.getNumTelephone());
        }
        if (updatedUser.getAdresse() != null) {
            existingUser.setAdresse(updatedUser.getAdresse());
        }
        if (updatedUser.getDateEmbauche() != null) {
            existingUser.setDateEmbauche(updatedUser.getDateEmbauche());
        }
        if (updatedUser.getUrgenceNom() != null) {
            existingUser.setUrgenceNom(updatedUser.getUrgenceNom());
        }
        if (updatedUser.getUrgenceNum() != null) {
            existingUser.setUrgenceNum(updatedUser.getUrgenceNum());
        }
        if (updatedUser.getCongeSolde() != null) {
            existingUser.setCongeSolde(updatedUser.getCongeSolde());
        }
        if (updatedUser.getCheckin() != null) {
            existingUser.setCheckin(updatedUser.getCheckin());
        }

        if (updatedUser.getPoste() != null) {
            existingUser.setPoste(updatedUser.getPoste());
        }
        if (updatedUser.getEtatCivil() != null) {
            existingUser.setEtatCivil(updatedUser.getEtatCivil());
        }
        if (updatedUser.getDepartement() != null) {
            existingUser.setDepartement(updatedUser.getDepartement());
        }
        if (updatedUser.getEquipe() != null) {
            existingUser.setEquipe(updatedUser.getEquipe());
        }
        return userRepository.save(existingUser);

    }

    @Override
    public List<User> findUsersByEquipe_id(Long id) {
        List<User> users= userRepository.findUsersByEquipe_id(id);
        return users;
    }

    @Override
    public List<User> findUsersByRolesContainingAndEquipeIsNull(String role) {
        Role role1=roleRepository.findByName(ERole.valueOf(role)).orElse(null);
        List<User> users = userRepository.findUsersByRolesContainingAndEquipeIsNull(role1);
        return users;
    }

    @Override
    public List<User> findUsersByRolesContainingAndEquipeIsNullOrEquipe_Id(Role role, Long id) {
        return userRepository.findUsersByRolesContainingAndEquipeIsNullOrEquipe_Id(role,id) ;
    }


    @Override
    public User assignUserToEquipe(User updatedUser,Long id) {
        Optional<User> existingUser = userRepository.findById(updatedUser.getId());
        Equipe equipe =new Equipe();
        equipe.setId(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEquipe(equipe);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User unAssignUserFromEquipe(User updatedUser) {
        Optional<User> existingUser = userRepository.findById(updatedUser.getId());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEquipe(null);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public List<User> assignUsersToEquipe(List<User> users,Long equipeid){
        for (User user: users ) {

            assignUserToEquipe(user,equipeid);
        }
        return users;
    }

    @Override
    public List<User> unassignUsersFromEquipe(List<User> users){
        for (User user: users ) {
            unAssignUserFromEquipe(user);
        }
        return users;
    }
}
