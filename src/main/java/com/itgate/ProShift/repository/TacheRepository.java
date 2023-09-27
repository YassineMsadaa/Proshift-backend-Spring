package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TacheRepository extends JpaRepository<Tache, Long> {
    List<Tache> findTachesByProjet_Id(Long projetId);
    List<Tache> findTachesByEmployee_id(Long userId);
}
