package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.Equipe;
import com.itgate.ProShift.service.interfaces.IEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

    private final IEquipeService equipeService;

    @Autowired
    public EquipeController(IEquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @PostMapping
    public ResponseEntity<?> createEquipe(@RequestBody Equipe equipe) {
        Equipe createdEquipe = equipeService.createEquipe(equipe);
        if (createdEquipe != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création de l'équipe.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEquipes() {
        List<Equipe> equipes = equipeService.getAllEquipes();
        if (equipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Aucune équipe trouvée.");
        } else {
            return ResponseEntity.ok(equipes);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipeById(@PathVariable("id") Long id) {
        Equipe equipe = equipeService.getEquipeById(id).orElse(null);
        if (equipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Équipe non trouvée.");
        } else {
            return ResponseEntity.ok(equipe);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEquipe(@PathVariable("id") Long id, @RequestBody Equipe equipe) {
        Equipe existingEquipe = equipeService.getEquipeById(id).orElse(null);
        if (existingEquipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Équipe non trouvée.");
        } else {
            equipe.setId(id);
            Equipe updatedEquipe = equipeService.updateEquipe(equipe);
            if (updatedEquipe != null) {
                return ResponseEntity.ok(updatedEquipe);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour de l'équipe.");
            }
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipe(@PathVariable("id") Long id) {
        Equipe equipe = equipeService.getEquipeById(id).orElse(null);
        if (equipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Équipe non trouvée.");
        } else {
            equipeService.deleteEquipe(id);
            return ResponseEntity.ok().build();
        }
    }
}

