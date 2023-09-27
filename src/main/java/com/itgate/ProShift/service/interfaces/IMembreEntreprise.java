package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.MembreEntreprise;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface IMembreEntreprise {

    MembreEntreprise createMembreEtreprise(MembreEntreprise membreEntreprise);
    public void save(MultipartFile file);
    ByteArrayInputStream load();
    List<MembreEntreprise> findAllMembreEntreprise();
    List<MembreEntreprise> findMembreEntrepriseByRole(String role);
    MembreEntreprise findMembreEntrepriseByCin(String cin);
    MembreEntreprise updateMembreEntreprise (MembreEntreprise membre);
    void removeMembreEntreprise(MembreEntreprise membreEntreprise);
}
