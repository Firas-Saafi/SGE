package com.example.sge.controller;

import com.example.sge.model.Note;
import com.example.sge.service.NoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;


    @PostMapping
    public Note ajouter(@RequestBody Note note) {
        return noteService.ajouterNote(note);
    }


    @GetMapping("/etudiant/{id}")
    public List<Note> byEtudiant(@PathVariable Long id) {
        return noteService.listerParEtudiant(id);
    }


    @GetMapping("/module/{id}")
    public List<Note> byModule(@PathVariable Long id) {
        return noteService.listerParModule(id);
    }


    @GetMapping("/moyenne/{id}")
    public Double moyenne(@PathVariable Long id) {
        return noteService.calculerMoyenne(id);
    }
}