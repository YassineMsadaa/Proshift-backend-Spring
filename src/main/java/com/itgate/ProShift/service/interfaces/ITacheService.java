package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.Tache;

import java.util.List;

public interface ITacheService {
    Tache createTache(Tache tache);
    List<Tache> getAllTaches();
    Tache getTacheById(Long id);
    Tache updateTache(Tache tache);
    void deleteTache(Long id);

    Tache assignTacheToProject(Tache tache, Long projectId);
    Tache unassignTacheFromProjet(Tache tache);
    List<Tache> unassignTachesFromProjet(List<Tache> tache);

    List<Tache> findTachesByProjet_Id(Long projetId);

    List<Tache> findTachesByEmployee_id(Long userId);
}
