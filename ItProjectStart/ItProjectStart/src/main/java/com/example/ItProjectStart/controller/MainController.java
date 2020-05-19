package com.example.ItProjectStart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping ("/main")
    public String mainPage(Model model){
        model.addAttribute("title","Main page");
        return "main-page";
    }

    @GetMapping("/")
    public String intro(Model model){
        model.addAttribute("title","Introduction");
        return "intro";
    }

    @GetMapping("/profile")
    public String profile(Model model){
        model.addAttribute("title","Profile");
        model.addAttribute("login","dagas_sa");
        model.addAttribute("curse","Marathon");
        model.addAttribute("role","Admin");
        return "profile";
    }



}
