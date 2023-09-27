package com.itgate.ProShift.service;

import com.itgate.ProShift.entity.Equipe;
import com.itgate.ProShift.entity.Projet;
import com.itgate.ProShift.entity.User;
import com.itgate.ProShift.repository.EquipeRepository;
import com.itgate.ProShift.repository.ProjetRepository;
import com.itgate.ProShift.repository.UserRepository;
import com.itgate.ProShift.service.interfaces.IEquipeService;
import com.itgate.ProShift.service.interfaces.IProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EquipeService implements IEquipeService {
    private final EquipeRepository equipeRepository;
    @Autowired
    private IProjetService projetService;
    @Autowired
    private UserService userService;
    @Autowired
    public EquipeService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    @Override
    public List<Equipe> getAllEquipes() {
        return equipeRepository.findAll();
    }
    @Override
    public Optional<Equipe> getEquipeById(Long id) {
        return equipeRepository.findById(id);
    }

    @Override
    public Equipe createEquipe(Equipe equipe) {
        equipe.setDateCreation(new Date());
        List<Projet> projets = equipe.getProjects();
        List<User> users=equipe.getEmployees();
        equipe.setProjects(null);
        equipe.setEmployees(null);
        equipeRepository.save(equipe);
        if (projets != null && !projets.isEmpty()) {
            for (Projet projet : projets) {
                projet.setEquipe(equipe);
                projetService.assignEquipeToProjet(projet, equipe.getId());
            }
        }
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                user.setEquipe(equipe);
                userService.assignUserToEquipe(user, equipe.getId());
            }
        }

        return equipe;
    }

    @Override
    public Equipe updateEquipe( Equipe updatedEquipe) {
        Optional<Equipe> existingEquipe = equipeRepository.findById(updatedEquipe.getId());
        if (existingEquipe.isPresent()) {
            Equipe equipe = existingEquipe.get();
            equipe.setName(updatedEquipe.getName());
            equipe.setProjectMaster(updatedEquipe.getProjectMaster());
            equipe.setEmployees(updatedEquipe.getEmployees());
            equipe.setProjects(updatedEquipe.getProjects());
            return equipeRepository.save(equipe);
        }
        return null;
    }

    @Override
    public void deleteEquipe(Long id) {
        List<User> users =userService.findUsersByEquipe_id(id);
        for (User user: users
             ) {
            userService.unAssignUserFromEquipe(user);
        }
        List<Projet> projets =projetService.findProjetsByEquipe_id(id);
        for (Projet projet: projets
        ) {
            projetService.unassignEquipeFromProjet(projet);
        }
        equipeRepository.deleteById(id);
    }
}
