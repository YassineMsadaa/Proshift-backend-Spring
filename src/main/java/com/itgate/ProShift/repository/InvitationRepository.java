package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}
