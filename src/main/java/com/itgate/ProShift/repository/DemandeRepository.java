package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    @Query("SELECT d FROM Demande d WHERE d.user.id = ?1")
    List<Demande> findDemandesByUserId(Long userId);
}
