package com.example.sge.service;

import com.example.sge.model.Etudiant;
import com.example.sge.model.Note;
import com.example.sge.repository.EtudiantRepository;
import com.example.sge.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    // =========================
    // 1. AJOUT NOTE
    // =========================
    public Note ajouterNote(Note note) {

        if (note.getValeur() < 0 || note.getValeur() > 20) {
            throw new RuntimeException("Note doit être entre 0 et 20");
        }

        Note saved = noteRepository.save(note);

        mettreAJourMoyenne(note.getEtudiant().getId());

        return saved;
    }

    // =========================
    // 2. LISTE PAR ETUDIANT
    // =========================
    public List<Note> listerParEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId);
    }

    // =========================
    // 3. LISTE PAR MODULE
    // =========================
    public List<Note> listerParModule(Long moduleId) {
        return noteRepository.findByModuleId(moduleId);
    }

    // =========================
    // 4. CALCUL MOYENNE
    // =========================
    public Double calculerMoyenne(Long etudiantId) {
        Double moy = noteRepository.calculerMoyenne(etudiantId);
        return (moy != null) ? moy : 0.0;
    }

    // =========================
    // 5. UPDATE MOYENNE ETUDIANT
    // =========================
    public void mettreAJourMoyenne(Long etudiantId) {

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable"));

        Double moyenne = calculerMoyenne(etudiantId);

        etudiant.setMoyenne(moyenne);

        etudiantRepository.save(etudiant);
    }

    // =========================
    // 6. MODIFIER NOTE
    // =========================
    public Note modifier(Long id, Note note) {

        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable"));

        existing.setValeur(note.getValeur());
        existing.setEtudiant(note.getEtudiant());
        existing.setModule(note.getModule());

        Note updated = noteRepository.save(existing);

        mettreAJourMoyenne(updated.getEtudiant().getId());

        return updated;
    }

    // =========================
    // 7. SUPPRIMER NOTE
    // =========================
    public void supprimer(Long id) {

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable"));

        Long etudiantId = note.getEtudiant().getId();

        noteRepository.delete(note);

        mettreAJourMoyenne(etudiantId);
    }
}