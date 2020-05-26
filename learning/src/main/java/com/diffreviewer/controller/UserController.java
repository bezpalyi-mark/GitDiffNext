package com.diffreviewer.controller;

import com.diffreviewer.entities.User;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MergeRequestRepo mergeRequestRepo;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("curse", "example");
        model.addAttribute("login", "example58");
        model.addAttribute("role", "admin");

        model.addAttribute("requests", mergeRequestRepo.findAll());
        return "profile-admin";
    }

    @GetMapping("/admin-panel")
    public String adminPanel(Model model) {

        return "admin-panel";
    }
}
