package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.Projet;
import com.itgate.ProShift.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProjetService {
    List<Projet> getAllProjets();
    Optional<Projet> getProjetById(Long id);
    Projet createProjet(Projet projet);
    Projet updateProjet( Projet updatedProjet);
    void deleteProjet(Long id);
    List<Projet> findProjetsByEquipeIsNull();
    List<Projet> findProjetsByEquipe_id(Long id);
    List<Projet> findProjetsByEquipe_IdOrEquipeIsNull(Long id);

    public Projet assignEquipeToProjet( Projet Projet,Long equipeId);
    Projet unassignEquipeFromProjet(Projet projet);
    public List<Projet> assignProjetsToEquipe(List<Projet> projets,Long equipeId);
    public List<Projet> unassignProjetsFromEquipe(List<Projet> projets);
    List<Projet> findProjetsByEquipe_EmployeesContains(User user);
    List<Projet> findProjetsByChef(User user);

}
