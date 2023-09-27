package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.Demande;


import java.util.List;

public interface IDemande {

    Demande createDemande(Demande demande,Long userid);
    List<Demande> findAllDemande();
    Demande updateDemande (Demande demande);
    Demande findDemandebyId(Long idDemande);
    public Demande accepterDemande(Demande demande);
    public Demande refuseeDemande(Demande demande);
    void removeDemande(Long idDemande);
    public List<Demande> findDemandeByUserId(Long id);
}
