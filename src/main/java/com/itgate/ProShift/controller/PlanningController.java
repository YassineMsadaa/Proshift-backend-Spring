package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.Planning;
import com.itgate.ProShift.repository.PlanningRepository;
import com.itgate.ProShift.repository.ProjetRepository;
import com.itgate.ProShift.repository.TacheRepository;
import com.itgate.ProShift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planning")
@CrossOrigin("*")
public class PlanningController {

        @Autowired
        PlanningRepository planningRepository;

        @Autowired
        UserRepository userRepository;


        @Autowired
        TacheRepository tacheRepository;
        @Autowired
         ProjetRepository projectRepository;





        @GetMapping
        public List<Planning> getall(){ return planningRepository.findAll();}

        @PostMapping
        public Planning createPlanning (@RequestBody Planning pl) {return planningRepository.save(pl);}

        @PutMapping ("/{id}")
        public Planning updatePlanning (@RequestBody Planning pl, @PathVariable Long id) {
            Planning pl1 = planningRepository.findById(id).orElse(null);
            if (pl1 != null) {
                pl1.setId(id);
                return planningRepository.saveAndFlush(pl);
            }
            else
            {throw new RuntimeException("Fail");}
        }
        @DeleteMapping("/{id}")
        public ResponseEntity deleteProvider (@PathVariable Long id) {
            planningRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
}
