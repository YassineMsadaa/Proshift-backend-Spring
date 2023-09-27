package com.itgate.ProShift.service;


import com.itgate.ProShift.entity.MembreEntreprise;
import com.itgate.ProShift.repository.MembreEntrepriseRepository;
import com.itgate.ProShift.service.interfaces.IMembreEntreprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class MembreEntrepriseService implements IMembreEntreprise {
  @Autowired
  MembreEntrepriseRepository membresOfCompanyRepo;

  @Override
  public MembreEntreprise createMembreEtreprise(MembreEntreprise membreEntreprise) {
    return membresOfCompanyRepo.save(membreEntreprise);
  }

  @Override
  public void save(MultipartFile file) {
    try {
      List<MembreEntreprise> MOCs = CSVHelper.csvToMOCs(file.getInputStream());
      membresOfCompanyRepo.saveAll(MOCs);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  @Override
  public ByteArrayInputStream load() {
    List<MembreEntreprise> MOCs = membresOfCompanyRepo.findAll();

    ByteArrayInputStream in = CSVHelper.MOCsToCSV(MOCs);
    return in;
  }




  @Override
  public List<MembreEntreprise> findAllMembreEntreprise() {
    return membresOfCompanyRepo.findAll();
  }

  @Override
  public List<MembreEntreprise> findMembreEntrepriseByRole(String role) {
    return membresOfCompanyRepo.findByRole(role);
  }

  @Override
  public MembreEntreprise findMembreEntrepriseByCin(String cin) {
    return membresOfCompanyRepo.findByCin(cin);
  }

  @Override
  public MembreEntreprise updateMembreEntreprise(MembreEntreprise membre) {
    return membresOfCompanyRepo.save(membre);
  }

  @Override
  public void removeMembreEntreprise(MembreEntreprise membreEntreprise) {
    membresOfCompanyRepo.delete(membreEntreprise);
  }
}
