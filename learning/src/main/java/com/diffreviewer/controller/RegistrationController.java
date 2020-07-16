package com.diffreviewer.controller;

import com.diffreviewer.entities.Role;
import com.diffreviewer.entities.User;
import com.diffreviewer.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUSer(User user, Map<String, Object> model) {
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userFromDB != null) {
            model.put("message", "User exists!");
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        HashPassword hashPassword = new HashPassword();
        String hash = hashPassword.encode(user.getPassword());
        user.setPassword(hash);
        if (!hash.isEmpty()) {
            userRepo.save(user);
        }
        return "redirect:/login";
    }
}
