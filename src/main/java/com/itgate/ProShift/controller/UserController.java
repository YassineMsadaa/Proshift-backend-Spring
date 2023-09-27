package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.ERole;
import com.itgate.ProShift.entity.Role;
import com.itgate.ProShift.entity.User;
import com.itgate.ProShift.payload.request.ChangePasswordRequest;
import com.itgate.ProShift.payload.response.MessageResponse;
import com.itgate.ProShift.repository.RoleRepository;
import com.itgate.ProShift.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;
    @Autowired
    RoleRepository roleRepository;

    ////////////////GET
    @GetMapping("/getAll")
    public  ResponseEntity<?> findAll(){
        try {
            List<User> users =  userService.findAllUser();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to load the users list!"));
        }
    }

    @GetMapping("/getuser/{userId}")
    public ResponseEntity<?> findById(@PathVariable Long userId){
        if(userService.findUserbyId(userId)==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: user does not exist!"));
        }
        User user =  userService.findUserbyId(userId);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/getbyrole/{role}")
    public  ResponseEntity<?> findByRole(@PathVariable ERole role){
        Role role1 =roleRepository.findByName(role).get();
        List<User> users =  userService.findUserByRole(role1.getName());
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/getbydepartement")
    public  ResponseEntity<?> findByDepartement(@RequestBody User.Departement departement){
        List<User> users =  userService.findUsersByDepartement(departement);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/byEquipeId/{id}")
    public ResponseEntity<List<User>> findUsersByEquipeId(@PathVariable Long id) {
        List<User> users = userService.findUsersByEquipe_id(id);
            return ResponseEntity.ok(users);
    }

    @GetMapping("/byRolesAndEquipeIsNull/{role}")
    public ResponseEntity<List<User>> findUsersByRolesContainingAndEquipeIsNull(@PathVariable String role) {
        List<User> users = userService.findUsersByRolesContainingAndEquipeIsNull(role);
            return ResponseEntity.ok(users);
    }
    ////////////////PUT
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        userService.updateUser(user);
        return ResponseEntity.ok().body(user);
    }


    @PutMapping("/banclient/{userId}")
    public ResponseEntity< ? > banClient(@PathVariable Long userId) {
        try {
            if(userService.findUserbyId(userId)==null){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("ERROR: user does not exist!"));
            }
            userService.blockUser(userId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("This account is blocked successfully!"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(" Failed to block this account!"));
        }
    }

    @PutMapping("/unbanclient/{userId}")
    public ResponseEntity< ? > unbanClient(@PathVariable Long userId) {
        try {
            if(userService.findUserbyId(userId)==null){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("ERROR: user does not exist!"));
            }
            userService.unBlockUser(userId);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(" un blocked successfully!"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(" Failed to unblock employee!"));
        }
    }

    @PutMapping("/assignUsersToEquipe/{equipeId}")
    public ResponseEntity< ? > assignUsersToEquipe(@PathVariable Long equipeId,@RequestBody List<User> users) {
        userService.assignUsersToEquipe(users,equipeId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/unassignUsersFromEquipe")
    public ResponseEntity< ? > unassignUsersFromEquipe(@RequestBody List<User> users) {
        userService.unassignUsersFromEquipe(users);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/check/{userId}")
    public ResponseEntity<?> checkIn(@PathVariable Long userId) {
        User user = userService.findUserbyId(userId);

        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Utilisateur non trouvé."));
        }

        if (user.getCheckin() != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("deja pointé."));
        }

        try {
            userService.checkin(userId);
            return ResponseEntity.ok().body(new MessageResponse("Vous avez pointer a: " + new Date()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erreur de pointage"));
        }
    }


    ////////////////POST
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        if (!userService.changePasswordByUser(changePasswordRequest.getId(), changePasswordRequest.getPassword(), changePasswordRequest.getNewPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: you entered a wrong password!"));
        }
        return ResponseEntity
                .ok()
                .body(new MessageResponse("Password changed successfully !"));
    }

    ////////////////DELETE
    @DeleteMapping("/delete/{idUser}")
    public ResponseEntity<?> deleteUser( @PathVariable Long idUser) {
        try {
            if(userService.findUserbyId(idUser)==null){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("ERROR: user does not exist!"));
            }
            userService.removeUser(idUser);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(" Deleted successfully!"));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to delete Client!"));
        }
    }



}
