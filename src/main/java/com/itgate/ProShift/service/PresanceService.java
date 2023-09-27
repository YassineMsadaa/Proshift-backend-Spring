package com.itgate.ProShift.service;

import com.itgate.ProShift.entity.Presence;
import com.itgate.ProShift.entity.User;
import com.itgate.ProShift.repository.PresenceRepository;
import com.itgate.ProShift.repository.UserRepository;
import com.itgate.ProShift.service.interfaces.IPresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PresanceService implements IPresenceService {
   @Autowired
    PresenceRepository presenceRepository;
    @Autowired
    UserRepository userRepository;
   @Override
    public Presence createPresence(Presence presence,Long userId) {

        return presenceRepository.save(presence);
    }

    @Override
    public List<Presence> findAllPresence() {
        return presenceRepository.findAll();
    }

    @Override
    public List<Presence> findPresenceByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return presenceRepository.findByUser(user);
    }

    @Override
    public List<Presence> findPresenceByDateCreation(Date date) {
        return presenceRepository.findByDateCreation(date);
    }

    @Override
    public Presence findPresencebyId(Long idPresence) {
        return presenceRepository.findById(idPresence).orElse(null);
    }

    @Override
    public Double calculeWorkedHours(Date date1, Date date2,Long userId) {
        List<Presence> presences = presenceRepository.findByDateCreationBetweenAndUser_Id(date1, date2, userId);
        double totalWorkedHours = 0.0;

        for (Presence presence : presences) {
            double presenceWorkedHours = presence.getHoursWorked();
            totalWorkedHours += presenceWorkedHours;
        }

        return totalWorkedHours;
    }

    @Override
    public void removePresence(Long idPresence) {
       presenceRepository.deleteById(idPresence);

    }
}
