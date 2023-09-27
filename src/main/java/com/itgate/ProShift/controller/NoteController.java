package com.itgate.ProShift.controller;

import com.itgate.ProShift.entity.Note;
import com.itgate.ProShift.payload.response.MessageResponse;
import com.itgate.ProShift.service.interfaces.INote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Autowired
    INote noteService;
    @GetMapping("/findAllNote")
    public ResponseEntity<?> findAllNote(){
        try {
            List<Note> notes =  noteService.findAllNote();
            return ResponseEntity.ok().body(notes);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to load the notes list!"));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> addNote(@RequestBody Note note){
        try {
            System.out.println(note);
            Note notee =  noteService.createNote(note);
            return ResponseEntity.ok().body(notee);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to load the notes list!"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateNote(@RequestBody Note note){
        try {
            System.out.println(note);

            Note notee =  noteService.updateNote(note);
            return ResponseEntity.ok().body(notee);
        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to load the notes list!"));
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id){
        try {
            System.out.println(id);
           noteService.removeNote(id);

            return ResponseEntity.ok().body(new MessageResponse("Success"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e.getCause());
            if (e.getCause() != null) {
                System.out.println(Arrays.toString(e.getCause().getStackTrace()));
            }
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Failed to delete the note!"));
        }
    }
}
