package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.Presence;

import java.util.Date;
import java.util.List;

public interface IPresenceService {

    Presence createPresence(Presence presence,Long userId);
    List<Presence> findAllPresence();
    List<Presence> findPresenceByUserId(Long userId);
    List<Presence> findPresenceByDateCreation(Date date);
    Presence findPresencebyId(Long idPresence);
    public Double calculeWorkedHours(Date date1,Date date2,Long userId);
    void removePresence(Long idPresence);
}
