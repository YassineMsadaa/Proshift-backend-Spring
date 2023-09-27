package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.Projet;
import com.itgate.ProShift.entity.User;
import com.itgate.ProShift.payload.response.MessageResponse;
import com.itgate.ProShift.service.UserService;
import com.itgate.ProShift.service.interfaces.IProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projets")
public class ProjetController {

    private final IProjetService projetService;

    @Autowired
    public ProjetController(IProjetService projetService) {
        this.projetService = projetService;
    }
    @Autowired
    public UserService userService;

    @GetMapping
    public ResponseEntity<List<Projet>> getAllProjets() {
        List<Projet> projets = projetService.getAllProjets();
        if (projets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projet> getProjetById(@PathVariable Long id) {
        Optional<Projet> projet = projetService.getProjetById(id);
        return projet.map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/byEquipeIsNull")
    public ResponseEntity<List<Projet>> findProjetsByEquipeIsNull() {
        List<Projet> projets = projetService.findProjetsByEquipeIsNull();
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/findProjetsByEquipe_EmployeesContains/{userId}")
    public ResponseEntity<List<Projet>> findProjetsByEquipe_EmployeesContains(@PathVariable Long userId) {
        User user = userService.findUserbyId(userId);
        List<Projet> projets = projetService.findProjetsByEquipe_EmployeesContains(user);
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/findProjetsByChef/{userId}")
    public ResponseEntity<List<Projet>> findProjetsByChef(@PathVariable Long userId) {
        User user = userService.findUserbyId(userId);
        List<Projet> projets = projetService.findProjetsByChef(user);
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/byEquipe/{id}")
    public ResponseEntity<List<Projet>> findProjetsByEquipe_id(@PathVariable Long id) {
        List<Projet> projets = projetService.findProjetsByEquipe_id(id);
        return ResponseEntity.ok(projets);
    }
    @GetMapping("/byEquipe_idOrNull/{id}")
    public ResponseEntity<List<Projet>> findProjetsByEquipe_idOrEquipeIsNull(@PathVariable Long id) {
        List<Projet> projets = projetService.findProjetsByEquipe_id(id);
        return ResponseEntity.ok(projets);
    }

    @PostMapping
    public ResponseEntity<String> createProjet(@RequestBody Projet projet) {
        Projet createdProjet = projetService.createProjet(projet);
        if (createdProjet == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Échec de la création du projet.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProjet(@PathVariable Long id, @RequestBody Projet updatedProjet) {
        updatedProjet.setId(id);
        Projet projet = projetService.updateProjet(updatedProjet);
        if (projet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projet non trouvé.");
        }
        return ResponseEntity.ok().build();
    }


    @PutMapping("/assignProjetsToEquipe/{equipeId}")
    public ResponseEntity< ? > assignProjetToEquipe(@RequestBody List<Projet> projets,@PathVariable Long equipeId) {
        projetService.assignProjetsToEquipe(projets,equipeId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/unassignProjetsFromEquipe")
    public ResponseEntity< ? > unassignProjetsFromEquipe(@RequestBody List<Projet> projets) {
        projetService.unassignProjetsFromEquipe(projets);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProjet(@PathVariable Long id) {
        projetService.deleteProjet(id);
        return ResponseEntity.ok().build();
    }
}
