package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
