package com.example.sge.controller;

import com.example.sge.service.BulletinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/templates/bullettins/bulletins")
public class BulletinController {

    private final BulletinService bulletinService;

    public BulletinController(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }


    @GetMapping
    public String getAllBulletins(Model model) {

        model.addAttribute("bulletins",
                bulletinService.genererTousLesBulletins());

        return "liste";
    }


    @GetMapping("/{id}")
    public String getBulletin(@PathVariable Long id, Model model) {

        model.addAttribute("bulletin",
                bulletinService.genererBulletin(id));

        return "details";
    }
}