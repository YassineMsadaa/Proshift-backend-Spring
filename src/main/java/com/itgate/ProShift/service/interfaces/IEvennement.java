package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.Evennement;

import java.util.List;

public interface IEvennement {
    Evennement createEvennement(Evennement evennement);
    List<Evennement> findAllEvennements();
    Evennement updateEvennement (Evennement evennement);
    Evennement findEvennementbyId(Long id);
    void removeEvennement(Long id);
}
