package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.Presence;
import com.itgate.ProShift.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    List<Presence> findByUser(User user);
    List<Presence> findByDateCreation(Date date);
    List<Presence> findByDateCreationBetweenAndUser_Id(Date date1,Date date2,Long userId);

  }
