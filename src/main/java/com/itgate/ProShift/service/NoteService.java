package com.itgate.ProShift.service;

import com.itgate.ProShift.entity.Note;
import com.itgate.ProShift.repository.NoteRepository;
import com.itgate.ProShift.service.interfaces.INote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoteService implements INote {
    @Autowired
    private NoteRepository noteRepository;
    @Override
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public List<Note> findAllNote() {
        return noteRepository.findAll();
    }

    @Override
    public Note updateNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note findNotebyId(Long idNote) {
        return noteRepository.findById(idNote).get();
    }

    @Override
    public void removeNote(Long idNote) {
    Note note =noteRepository.findById(idNote).get();
        System.out.println(note);
    noteRepository.delete(note);
    }
}
