package com.diffreviewer.controller;

import com.diffreviewer.entities.MergeRequest;
import com.diffreviewer.entities.Role;
import com.diffreviewer.entities.Task;
import com.diffreviewer.entities.User;
import com.diffreviewer.repos.ListTaskRepo;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.TaskRepo;
import com.diffreviewer.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;

@Controller
public class MainController {

    private boolean adminExists = false;

    @Autowired
    private MergeRequestRepo mergeRequestRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        if (!adminExists) {
            HashPassword hashPassword = new HashPassword();
            User user = userRepo.findByUsername("admin");
            if (user == null) {
                user = new User();
            } else {
                return "redirect:/main-tree";
            }
            user.setActive(true);
            user.setUsername("admin");
            user.setPassword(hashPassword.encode("admin"));
            user.setRoles(Collections.singleton(Role.ADMIN));
            userRepo.save(user);
            adminExists = true;
        }
        return "redirect:/main-tree";
    }

    @PostMapping("/main-tree")
    public String addRequest(@AuthenticationPrincipal User user, @RequestParam String MR,
            @RequestParam String taskChoise, Model model) {
        // if (!MR.matches("https?:\\/\\/(www\\.)?" +
        // "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)"))
        // {
        // System.out.println("Bad url!");
        // return "/main-tree";
        // }
        System.out.println("Hello world. This is button click click ;)");
        Task task = taskRepo.findByNameAndUser(taskChoise, user);
        if (task == null) {
            System.out.println("No task for this user given!");
            return "/main-tree";
        }
        GitApi gitApi = new GitApi(MR);
        MergeRequest mergeRequest = gitApi.GetPR(user);
        if (mergeRequest != null) {
            mergeRequest.setTask(task);
            mergeRequestRepo.save(mergeRequest);
        }
        return "/main-tree";
    }
}

