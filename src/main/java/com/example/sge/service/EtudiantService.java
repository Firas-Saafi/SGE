package com.example.sge.service;

import com.example.sge.event.InscriptionEvent;
import com.example.sge.model.Etudiant;
import com.example.sge.model.Filiere;
import com.example.sge.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    public Etudiant ajouter(Etudiant e) {

        Etudiant saved = etudiantRepository.save(e);

        if (e.getFiliere() != null) {
            publisher.publishEvent(
                    new InscriptionEvent(this,
                            e.getNom() + " " + e.getPrenom(),
                            e.getFiliere().getNom()));
        }
        return saved ;
    }

    public List<Etudiant> listerTous() {

        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> trouverParId(Long id) {
        return etudiantRepository.findById(id);
    }

    public void supprimer(Long id) {

        etudiantRepository.deleteById(id);
    }


    public Etudiant modifier (Long id, Etudiant e){
        return  etudiantRepository.findById(id).map(existing -> {
            existing.setNom(e.getNom());
            existing.setPrenom(e.getPrenom());
            existing.setEmail(e.getEmail());
            existing.setCin(e.getCin());
            existing.setDateNaissance(e.getDateNaissance());
            existing.setMoyenne(e.getMoyenne());
            existing.setGroupe(e.getGroupe());
            return etudiantRepository.save(existing);
        }).orElseThrow(() ->
                new RuntimeException ("Etudiant introuvable :" + id));
    }


    public List<Etudiant> rechercherParNom(String nom) {
        return etudiantRepository.findByNomContainingIgnoreCase(nom);
    }

    public List <Etudiant> trouverParGroupe(String groupe) {
        return  etudiantRepository.findByGroupe(groupe);

    }
    public List <Etudiant> trouverAdmis(double seuil) {
        return  etudiantRepository.findByMoyenneGreaterThanEqual(seuil);
    }

    public List <Etudiant> trouverMeilleur(double seuil) {
        return  etudiantRepository.findMeilleurs(seuil);
    }

}