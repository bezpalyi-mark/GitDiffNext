package com.diffreviewer.controller;

import com.diffreviewer.entities.*;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.TaskRepo;
import com.diffreviewer.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        if(!adminExists) {
            HashPassword hashPassword = new HashPassword();
            User user = userRepo.findByUsername("admin");
            if(user == null) {
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

    @GetMapping("/profile")
    public String profile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = userRepo.findByUsername(username);
        List<Task> doneTasks = taskRepo.findByUserAndIsDone(currentUser, true);
        List<MergeRequest> mergeRequestList = new ArrayList<>();
        for(Task task : doneTasks) {
            mergeRequestList.add(mergeRequestRepo.findByTaskAndStatusPR(task, Status.NOT_MERGED));
        }
        model.addAttribute("requests", mergeRequestList);
        model.addAttribute("login", currentUser.getUsername());
        model.addAttribute("curse", "novalab");
        model.addAttribute("role", currentUser.getRoles());
        return "profile";
    }

    @PostMapping("/review")
    public String addRequest(@RequestParam String url) {
        if(!url.matches("https?:\\/\\/(www\\.)?" +
                "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")) {
            System.out.println("Bad url!");
            return "/review";
        }
        GitApi gitApi = new GitApi(url);
        MergeRequest mergeRequest = gitApi.GetPR();
        mergeRequestRepo.save(mergeRequest);
        return "review";
    }

    @GetMapping("/main-tree")
    public String main(Map<String, Object> model) {

        return "main-tree";
    }
}

