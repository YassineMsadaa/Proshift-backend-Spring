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
public class ProjetService implements IProjetService {

    private final ProjetRepository projetRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    EquipeRepository equipeRepository;


    @Autowired
    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    @Override
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    @Override
    public Optional<Projet> getProjetById(Long id) {
        return projetRepository.findById(id);
    }

    @Override
    public Projet createProjet(Projet projet) {
        projet.setDateCreation(new Date());
        return projetRepository.save(projet);
    }

    @Override
    public Projet updateProjet(Projet updatedProjet) {
        System.out.println(updatedProjet.toString());
        Optional<Projet> existingProjet = projetRepository.findById(updatedProjet.getId());
        if (existingProjet.isPresent()) {
            Projet projet = existingProjet.get();
            projet.setNom(updatedProjet.getNom());
            projet.setDateFinEstimee(updatedProjet.getDateFinEstimee());
            projet.setDateFin(updatedProjet.getDateFin());
            projet.setDateDebut(updatedProjet.getDateDebut());
            projet.setDescription(updatedProjet.getDescription());
            if (projet.getEtat().equals(Projet.Etat.ENCOURS) && updatedProjet.getEtat().equals(Projet.Etat.ANNULEE) || updatedProjet.getEtat().equals(Projet.Etat.TERMINER)) {
                projet.setEtat(Projet.Etat.TERMINER);
                projet.setDateFin(new Date());
            } else if ((projet.getEtat().equals(Projet.Etat.ANNULEE) || projet.getEtat().equals(Projet.Etat.TERMINER)) &&
                    updatedProjet.getEtat().equals(Projet.Etat.ENCOURS)) {
                projet.setEtat(Projet.Etat.ENCOURS);
                projet.setDateFin(null);
            } else {
                projet.setEtat(updatedProjet.getEtat());
            }

            projet.setChef(userRepository.findById(updatedProjet.getChef().getId()).get());
            projet.setTaches(updatedProjet.getTaches());
            return projetRepository.save(projet);
        }
        return null;
    }

    @Override
    public Projet assignEquipeToProjet( Projet Projet,Long equipeId) {
        Optional<Projet> existingProjet = projetRepository.findById(Projet.getId());
        Equipe equipe= equipeRepository.findById(equipeId).get();
        equipe.setId(equipeId);

        if (existingProjet.isPresent()) {
            Projet projet = existingProjet.get();
            projet.setEquipe(equipe);
            return projetRepository.save(projet);
        }
        return null;
    }
    @Override
    public Projet unassignEquipeFromProjet( Projet Projet) {
        Optional<Projet> existingProjet = projetRepository.findById(Projet.getId());
        if (existingProjet.isPresent()) {
            Projet projet = existingProjet.get();
            projet.setEquipe(null);
            return projetRepository.save(projet);
        }
        return null;
    }

    @Override
    public List<Projet> assignProjetsToEquipe(List<Projet> projets,Long equipeid){
        for (Projet projet: projets ) {

            assignEquipeToProjet(projet,equipeid);
        }
        return projets;
    }

    @Override
    public List<Projet> unassignProjetsFromEquipe(List<Projet> projets){
        for (Projet projet: projets ) {
            unassignEquipeFromProjet(projet);
        }
        return projets;
    }

    @Override
    public List<Projet> findProjetsByEquipe_EmployeesContains(User user) {
        return projetRepository.findProjetsByEquipe_EmployeesContains(user);
    }

    @Override
    public List<Projet> findProjetsByChef(User user) {
        return projetRepository.findProjetsByChef(user);
    }

    @Override
    public void deleteProjet(Long id) {
            projetRepository.deleteById(id);
        }

    @Override
    public List<Projet> findProjetsByEquipeIsNull() {
        return projetRepository.findProjetsByEquipeIsNull();
    }

    @Override
    public List<Projet> findProjetsByEquipe_id(Long id) {
        return projetRepository.findProjetsByEquipe_Id(id);
    }

    @Override
    public List<Projet> findProjetsByEquipe_IdOrEquipeIsNull(Long id) {
        return projetRepository.findProjetsByEquipe_IdOrEquipeIsNull(id);
    }

}
