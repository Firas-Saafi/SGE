package com.example.sge.controller;

import com.example.sge.dto.AuthResponse;
import com.example.sge.dto.LoginRequest;
import com.example.sge.model.Role;
import com.example.sge.model.Utilisateur;
import com.example.sge.repository.UtilisateurRepository;
import com.example.sge.security.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.User;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest req) {

        if (utilisateurRepo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Username deja utilise");
        }

        Utilisateur user = new Utilisateur();

        user.setUsername(req.getUsername());

        user.setPassword(
                passwordEncoder.encode(req.getPassword())
        );


        if (req.getRole() != null) {
            user.setRole(req.getRole());
        } else {
            user.setRole(Role.USER);
        }

        utilisateurRepo.save(user);

        User springUser = new User(
                user.getUsername(),
                user.getPassword(),
                List.of()
        );


        String token = jwtService.generateToken(springUser);

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        user.getRole().name()
                )
        );
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        try {

            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()
                    )
            );

            Utilisateur user = utilisateurRepo
                    .findByUsername(req.getUsername())
                    .orElseThrow();


            User springUser = new User(
                    user.getUsername(),
                    user.getPassword(),
                    List.of()
            );


            String token = jwtService.generateToken(springUser);

            return ResponseEntity.ok(
                    new AuthResponse(
                            token,
                            user.getRole().name()
                    )
            );

        } catch (BadCredentialsException e) {

            return ResponseEntity
                    .status(401)
                    .body("Username ou password incorrect");
        }
    }
}