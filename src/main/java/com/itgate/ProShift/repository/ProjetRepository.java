package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.Projet;
import com.itgate.ProShift.entity.Tache;
import com.itgate.ProShift.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
        List<Projet> findProjetsByEquipeIsNull();
        List<Projet> findProjetsByEquipe_Id(Long id);
        List<Projet> findProjetsByEquipe_IdOrEquipeIsNull(Long id);
        List<Projet> findProjetsByTachesContaining(Tache tache);
        List<Projet> findProjetsByChef_id(Long id);
        @Query("SELECT DISTINCT p FROM Projet p JOIN p.equipe e JOIN e.employees emp WHERE :user MEMBER OF emp")
        List<Projet> findProjetsByEquipe_EmployeesContains(@Param("user") User user);
        List<Projet> findProjetsByChef(User user);
}
