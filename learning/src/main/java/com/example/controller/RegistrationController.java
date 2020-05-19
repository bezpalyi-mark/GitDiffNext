package com.example.controller;

import com.example.entities.Role;
import com.example.entities.User;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUSer(User user, Map<String, Object> model) {
        System.out.println(user.getUsername());
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userFromDB != null) {
            model.put("message", "User exists!");
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        HashPassword hashPassword = new HashPassword();
        String hash = hashPassword.encode(user.getPassword());
        user.setPassword(hash);
        if(!hash.isEmpty()){
            userRepo.save(user);
        }
        return "redirect:/login";
    }
}
