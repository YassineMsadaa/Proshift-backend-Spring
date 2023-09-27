package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.MembreEntreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembreEntrepriseRepository extends JpaRepository<MembreEntreprise, String> {
    boolean existsByCin(String cin);
    MembreEntreprise findByCin(String cin);
    List<MembreEntreprise> findByRole(String Role);
    void deleteByCin(String cin);
    void delete(MembreEntreprise membreEntreprise);
}
