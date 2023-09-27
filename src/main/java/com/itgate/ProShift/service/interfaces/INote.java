package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.Note;

import java.util.List;

public interface INote {
    Note createNote(Note note);
    List<Note> findAllNote();
    Note updateNote (Note note);
    Note findNotebyId(Long idNote);

    void removeNote(Long idNote);
}
