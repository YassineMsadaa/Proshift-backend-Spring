package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.Demande;
import com.itgate.ProShift.service.interfaces.IDemande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/demandes")
    public class DemandeController {

        private final IDemande demandeService;

        @Autowired
        public DemandeController(IDemande demandeService) {
            this.demandeService = demandeService;
        }

        @PostMapping("/{userid}")
        public ResponseEntity<?> createDemande(@RequestBody Demande demande,@PathVariable Long userid) {
            demandeService.createDemande(demande,userid);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @GetMapping
        public ResponseEntity<?> getAllDemandes() {
            List<Demande> demandes = demandeService.findAllDemande();
            if (demandes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Aucune demande trouvée.");
            } else {
                return ResponseEntity.ok(demandes);
            }
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getDemandeById(@PathVariable("id") Long id) {
            Demande demande = demandeService.findDemandebyId(id);
            if (demande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée.");
            } else {
                return ResponseEntity.ok(demande);
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateDemande(@PathVariable("id") Long id, @RequestBody Demande demande) {
            Demande existingDemande = demandeService.findDemandebyId(id);
            if (existingDemande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée.");
            } else {
                demande.setId(id);
                Demande updatedDemande = demandeService.updateDemande(demande);
                return ResponseEntity.ok(updatedDemande);
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteDemande(@PathVariable("id") Long id) {
            Demande demande = demandeService.findDemandebyId(id);
            if (demande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée.");
            } else {
                demandeService.removeDemande(id);
                return ResponseEntity.ok().build();
            }
        }

        @PostMapping("/{id}/accepter")
        public ResponseEntity<?> accepterDemande(@PathVariable("id") Long id) {
            Demande demande = demandeService.findDemandebyId(id);
            if (demande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée.");
            } else {
                demandeService.accepterDemande(demande);
                return ResponseEntity.ok().build();
            }
        }

        @PostMapping("/{id}/refuser")
        public ResponseEntity<?> refuserDemande(@PathVariable("id") Long id) {
            Demande demande = demandeService.findDemandebyId(id);
            if (demande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée.");
            } else {
                demandeService.refuseeDemande(demande);
                return ResponseEntity.ok().build();
            }
        }
        @GetMapping("/user/{id}")
        public ResponseEntity<List<Demande>> getDemandesByUserId(@PathVariable Long id) {
            List<Demande> demandes = demandeService.findDemandeByUserId(id);
            return ResponseEntity.ok(demandes);
        }
    }


