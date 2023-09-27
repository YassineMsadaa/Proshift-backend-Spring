package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.Presence;
import com.itgate.ProShift.entity.User;
import com.itgate.ProShift.payload.request.CalculeWorkedHoursRequest;
import com.itgate.ProShift.payload.response.MessageResponse;
import com.itgate.ProShift.service.interfaces.IPresenceService;
import com.itgate.ProShift.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/presence")
public class PresenceController {

    @Autowired
    IPresenceService presenceService;
    @Autowired
    IUserService userService;
    ////////////////GET
    @GetMapping("/findAllPresence")
    public ResponseEntity<?> findAllPresence(){
        try {
            List<Presence> presences =  presenceService.findAllPresence();
            return ResponseEntity.ok().body(presences);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERREUR: Failed to load the presence list!"));
        }
    }
    @PostMapping("/createPresence/{userId}")
    public ResponseEntity<?> createPresence(@PathVariable Long userId) {
        Presence presence=new Presence();
        User user = userService.findUserbyId(userId);
        Date checkInTime = user.getCheckin();

        if (checkInTime == null) {
            return ResponseEntity.badRequest().body("Erreur: Vous n'avez pas pointer");
        }

        presence.setUser(user);
        presence.setCheckInTime(checkInTime);
        presence.setCheckOutTime(new Date());
        long timeWorkedMillis = presence.getCheckOutTime().getTime() - presence.getCheckInTime().getTime();
        double hoursWorked = timeWorkedMillis / (1000.0 * 60 * 60);
        presence.setHoursWorked(hoursWorked);
        presenceService.createPresence(presence, userId);
        userService.checkOut(userId);

        return ResponseEntity.ok().body(presence);
    }


    @GetMapping("/findPresenceByUserId/{userId}")
    public ResponseEntity<?> findPresenceByUserId(@PathVariable Long userId){
        List<Presence> presences =  presenceService.findPresenceByUserId(userId);
        return ResponseEntity.ok().body(presences);
    }

    @PostMapping("/findPresenceByDateCreation")
    public ResponseEntity<?> findPresenceByDateCreation(@RequestBody Date date){
        List<Presence> presences =  presenceService.findPresenceByDateCreation(date);
        return ResponseEntity.ok().body(presences);
    }

    @GetMapping("/findPresencebyId/{idPresence}")
    public ResponseEntity<?> findPresencebyId(@PathVariable Long idPresence){
        Presence presence =  presenceService.findPresencebyId(idPresence);
        return ResponseEntity.ok().body(presence);
    }

    @PostMapping("/calculeWorkedHours/{userId}")
    public ResponseEntity<?> calculeWorkedHours(@RequestBody CalculeWorkedHoursRequest request,@PathVariable Long userId){
        Double workedHours =  presenceService.calculeWorkedHours(request.getDate1(),request.getDate2(),userId);
        return ResponseEntity.ok().body(workedHours);
    }


    @DeleteMapping("/delete/{idPresence}")
    public ResponseEntity<?> removePresence( @PathVariable Long idPresence) {
        try {
            presenceService.removePresence(idPresence);
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
