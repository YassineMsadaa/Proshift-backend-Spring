package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.Projet;
import com.itgate.ProShift.entity.Tache;
import com.itgate.ProShift.entity.User;
import com.itgate.ProShift.service.TacheService;
import com.itgate.ProShift.service.interfaces.ITacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taches")
public class TacheController {

    private final ITacheService tacheService;

    @Autowired
    public TacheController(ITacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTaches() {
        List<Tache> taches = tacheService.getAllTaches();
        return ResponseEntity.status(HttpStatus.OK).body(taches);
    }

    @GetMapping("/findTachesByProjet_Id/{projetId}")
    public ResponseEntity<List<Tache>> findTachesByProjet_Id(@PathVariable Long projetId) {
        List<Tache> taches = tacheService.findTachesByProjet_Id(projetId);
        return ResponseEntity.ok(taches);
    }
    @GetMapping("/findTachesByEmployee_id/{userId}")
    public ResponseEntity<List<Tache>> findTachesByEmployee_id(@PathVariable Long userId) {
        List<Tache> taches = tacheService.findTachesByEmployee_id(userId);
        return ResponseEntity.ok(taches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTacheById(@PathVariable Long id) {
        Tache tache = tacheService.getTacheById(id);
        if (tache == null) {
            String message = "La tâche est introuvable.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(tache);
        }
    }

    @PostMapping
    public ResponseEntity<?> createTache(@RequestBody Tache tache) {
        Tache createdTache = tacheService.createTache(tache);
        System.out.println(createdTache.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTache);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTache( @RequestBody Tache tache) {
        Tache existingTache = tacheService.getTacheById(tache.getId());
        if (existingTache == null) {
            String message = "La tâche est introuvable.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            tache.setId(tache.getId());
            Tache updatedTache = tacheService.updateTache(tache);
            return ResponseEntity.status(HttpStatus.OK).body(updatedTache);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTache(@PathVariable Long id) {
        Tache existingTache = tacheService.getTacheById(id);
        if (existingTache == null) {
            String message = "La tâche est introuvable.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            tacheService.deleteTache(id);
            return ResponseEntity.ok().build();
        }
    }
    @PutMapping("/assignTacheToProjet/{projetId}")
    public ResponseEntity< ? > assignTacheToProjet(@RequestBody Tache taches, @PathVariable Long projetId) {
        tacheService.assignTacheToProject(taches,projetId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/unassignTacheFromProjet")
    public ResponseEntity< ? > unassignTacheFromProjet(@RequestBody Tache tache) {
        tacheService.unassignTacheFromProjet(tache);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/unassignTachesFromProjet")
    public ResponseEntity< ? > unassignTachesFromProjet(@RequestBody List<Tache> taches) {
        tacheService.unassignTachesFromProjet(taches);
        return ResponseEntity.ok().build();
    }
}
