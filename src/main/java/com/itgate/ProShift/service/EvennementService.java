package com.itgate.ProShift.service;

import com.itgate.ProShift.entity.Evennement;
import com.itgate.ProShift.entity.Invitation;
import com.itgate.ProShift.repository.EvennementRepository;
import com.itgate.ProShift.service.interfaces.IEvennement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EvennementService implements IEvennement {
    @Autowired
    EvennementRepository evennementRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override

    public Evennement createEvennement(Evennement evennement) {
        Evennement savedEvent = evennementRepository.save(evennement);
        List<Invitation> listInvitation = new ArrayList<>();

        for (Invitation i : savedEvent.getInvitations()){
            i.setEvennement(savedEvent);
            listInvitation.add(i);
        }
        savedEvent.setInvitations(listInvitation);
        return evennementRepository.save(savedEvent);

    }

    @Override
    @Transactional

    public List<Evennement> findAllEvennements() {

        List<Evennement> evennements = evennementRepository.findAll();

        // Iterate over the evennements and access the lazily loaded collection within the session
        for (Evennement evennement : evennements) {
            // Access the lazily loaded collection to trigger its loading
            List<Invitation> invitations = evennement.getInvitations();
            System.out.println(invitations);//Dont delete
        }

        return evennements;
    }

    @Override
    public Evennement updateEvennement(Evennement evennement) {
        List<Invitation> listInvitation = new ArrayList<>();

        for (Invitation i : evennement.getInvitations()){
            i.setEvennement(evennement);
            listInvitation.add(i);
        }
        evennement.setInvitations(listInvitation);
        System.out.println(evennement);
        return evennementRepository.save(evennement);
    }

    @Override
    public Evennement findEvennementbyId(Long id) {
        return evennementRepository.findById(id).get();
    }

    @Override
    public void removeEvennement(Long id) {
         evennementRepository.deleteById(id);

    }
}
