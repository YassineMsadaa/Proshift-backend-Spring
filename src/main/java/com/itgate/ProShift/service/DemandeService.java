package com.itgate.ProShift.service;

import com.itgate.ProShift.entity.Demande;
import com.itgate.ProShift.repository.DemandeRepository;
import com.itgate.ProShift.service.interfaces.IDemande;
import com.itgate.ProShift.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandeService implements IDemande {

    private final DemandeRepository demandeRepository;
    @Autowired
    IUserService userService;

    @Autowired
    public DemandeService(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public Demande createDemande(Demande demande ,Long userid) {
        demande.setUser(userService.findUserbyId(userid));
        return demandeRepository.save(demande);
    }

    @Override
    public List<Demande> findAllDemande() {
        return demandeRepository.findAll();
    }



    @Override
    public Demande updateDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public Demande findDemandebyId(Long idDemande) {
        Optional<Demande> demande = demandeRepository.findById(idDemande);
        return demande.orElse(null);
    }

    @Override
    public Demande accepterDemande(Demande demande) {
        demande.setStatus(Demande.StatusDemande.Acceptee);
        return demandeRepository.save(demande);
    }
    @Override
    public Demande refuseeDemande(Demande demande){
        demande.setStatus(Demande.StatusDemande.Refusee);
        return demandeRepository.save(demande);
    }

    @Override
    public void removeDemande(Long idDemande) {
        demandeRepository.deleteById(idDemande);
    }

    @Override
    public List<Demande> findDemandeByUserId(Long id) {
        return demandeRepository.findDemandesByUserId(id);
    }
}
