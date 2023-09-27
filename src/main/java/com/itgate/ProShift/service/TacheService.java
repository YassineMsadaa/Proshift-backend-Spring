package com.itgate.ProShift.service;

import com.itgate.ProShift.entity.Projet;
import com.itgate.ProShift.entity.Tache;
import com.itgate.ProShift.repository.ProjetRepository;
import com.itgate.ProShift.repository.TacheRepository;
import com.itgate.ProShift.service.interfaces.ITacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TacheService implements ITacheService {

    private final TacheRepository tacheRepository;

    @Autowired
    public ProjetRepository projetRepository;

    @Autowired
    public TacheService(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }

    @Override
    public Tache createTache(Tache tache) {
        if (tache.getEmployee() != null) {
            tache.setDateAssignation(LocalDateTime.now());
        }
        return tacheRepository.save(tache);
    }

    @Override
    public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
    }

    @Override
    public Tache getTacheById(Long id) {
        Optional<Tache> optionalTache = tacheRepository.findById(id);
        return optionalTache.orElse(null);
    }

    @Override
    public Tache updateTache(Tache tache) {
        Tache tache1 = tacheRepository.findById(tache.getId()).get();
        LocalDateTime currentDate = LocalDateTime.now();

        if (tache.getEmployee() == null && tache1.getEmployee() != null) {
            tache.setDateAssignation(null);
        } else if (tache.getEmployee() != null && tache1.getEmployee() == null) {
            tache.setDateAssignation(currentDate);
        }
        if (tache.isDone() == true && tache1.isDone() == false) {
            tache.setDateFin(currentDate); // Task marked as done, set dateFin to the current date
        } else if (tache.isDone() == false && tache1.isDone() == true) {
            tache.setDateFin(null); // Task marked as not done, set dateFin to null
        }

        return tacheRepository.save(tache);
    }

    @Override
    public void deleteTache(Long id) {

        Tache tache=tacheRepository.findById(id).get();
        unassignTacheFromProjet(tache);
        tacheRepository.deleteById(id);
    }

    @Override
    public Tache assignTacheToProject(Tache tache, Long projectId) {
        Projet projet = projetRepository.findById(projectId).get();
        tache.setProjet(projet);
        return tacheRepository.save(tache);
    }

    @Override
    public Tache unassignTacheFromProjet(Tache tache) {
        tache.setProjet(null);
        return tacheRepository.save(tache);
    }

    @Override
    public List<Tache> unassignTachesFromProjet(List<Tache> taches) {
        for (Tache tache:taches) {
            unassignTacheFromProjet(tache);
        }
        return taches;
    }

    @Override
    public List<Tache> findTachesByProjet_Id(Long projetId) {
        return tacheRepository.findTachesByProjet_Id(projetId);
    }

    @Override
    public List<Tache> findTachesByEmployee_id(Long userId) {
        return tacheRepository.findTachesByEmployee_id(userId);
    }
}
