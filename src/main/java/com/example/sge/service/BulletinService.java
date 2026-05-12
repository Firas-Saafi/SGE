package com.example.sge.service;

import com.example.sge.dto.BulletinDTO;
import com.example.sge.model.Etudiant;
import com.example.sge.model.Note;
import com.example.sge.repository.EtudiantRepository;
import com.example.sge.repository.NoteRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulletinService {

    private final EtudiantRepository etudiantRepository;
    private final NoteRepository noteRepository;

    public BulletinService(EtudiantRepository etudiantRepository,
                           NoteRepository noteRepository) {
        this.etudiantRepository = etudiantRepository;
        this.noteRepository = noteRepository;
    }

    // ================= BULLETIN ONE ETUDIANT =================
    public BulletinDTO genererBulletin(Long etudiantId) {

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable"));

        List<Note> notes = noteRepository.findByEtudiantId(etudiantId);

        double somme = notes.stream()
                .mapToDouble(Note::getValeur)
                .sum();

        double moyenne = notes.isEmpty() ? 0 : somme / notes.size();

        BulletinDTO dto = new BulletinDTO();
        dto.setEtudiant(etudiant);
        dto.setNotes(notes);
        dto.setMoyenneGenerale(moyenne);
        dto.setMention(calculerMention(moyenne));
        dto.setAdmis(moyenne >= 10);

        return dto;
    }

    // ================= ALL BULLETINS =================
    public List<BulletinDTO> genererTousLesBulletins() {

        return etudiantRepository.findAll()
                .stream()
                .map(e -> genererBulletin(e.getId()))
                .toList();
    }

    // ================= MENTION =================
    private String calculerMention(double moyenne) {

        if (moyenne >= 16) return "Très bien";
        else if (moyenne >= 14) return "Bien";
        else if (moyenne >= 12) return "Assez bien";
        else if (moyenne >= 10) return "Passable";
        else return "Ajourné";
    }
}