package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.Evennement;
import com.itgate.ProShift.entity.User;
import com.itgate.ProShift.payload.response.MessageResponse;
import com.itgate.ProShift.service.interfaces.IEvennement;
import com.itgate.ProShift.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EvennementController {
    @Autowired
    IUserService userService;

    @Autowired
    IEvennement serviceEvennement;
    @GetMapping("/findAll")
    public ResponseEntity<?> findAllNote(){
        try {
            System.out.println("here");
            List<Evennement> evennements =  serviceEvennement.findAllEvennements();
            System.out.println("here");

            return ResponseEntity.ok().body(evennements);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to load the notes list!"));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> addEvennement(@RequestBody Evennement evennement){
        try {
            System.out.println(evennement);
            Evennement event =  serviceEvennement.createEvennement(evennement);
            return ResponseEntity.ok().body(event);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to add event!"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEvennement(@RequestBody Evennement evennement){
        try {
            Evennement evennement1 =  serviceEvennement.updateEvennement(evennement);
            return ResponseEntity.ok().body(evennement1);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to update event!"));
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvennement(@PathVariable Long id){
        try {
            System.out.println(id);
            serviceEvennement.removeEvennement(id);
            return ResponseEntity.ok().body(new MessageResponse("Success"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to delete the event!"));
        }
    }


    ////////////////GET
    @GetMapping("/getAllUsers")
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

    @PutMapping("/updateEmpl")
    @Transactional

    public ResponseEntity<?> updateUser(@RequestBody User user){
        try {
            System.out.println(user);
            User user1 =userService.updateEmployee(user);
            System.out.println("here");
            return ResponseEntity.ok().body(user1);
        } catch (Exception e) {
            if (e.getCause() != null) {
                System.out.println(Arrays.toString(e.getCause().getStackTrace()));
            }
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }
}
