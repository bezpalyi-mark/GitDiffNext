package com.diffreviewer.controller;

import com.diffreviewer.entities.MergeRequest;
import com.diffreviewer.entities.Task;
import com.diffreviewer.entities.User;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.TaskRepo;
import com.diffreviewer.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    public static final Logger LOGGER  = LoggerFactory.getLogger(MainController.class);

    private final MergeRequestRepo mergeRequestRepo;

    private final UserRepo userRepo;

    private final TaskRepo taskRepo;

    private static final String MAIN_TREE = "/main-tree";

    @Autowired
    public MainController(MergeRequestRepo mergeRequestRepo, UserRepo userRepo, TaskRepo taskRepo) {
        this.mergeRequestRepo = mergeRequestRepo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
    }

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "redirect:" + MAIN_TREE;
    }

    @PostMapping(MAIN_TREE)
    public String addRequest(@RequestParam String mr, @RequestParam String selectTask, Model model) {
        User user;
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        user = userRepo.findByUsername(username);
        if(user == null) {
            LOGGER.error("Problems with user");
            return MAIN_TREE;
        }
        Task task = taskRepo.findByReferenceInListNameAndUser(selectTask, user);
        if (task == null) {
            LOGGER.error("No task for this user given!");
            return MAIN_TREE;
        }
        GitApi gitApi = new GitApi(mr);
        MergeRequest mergeRequest = gitApi.getPR(user);
        if (mergeRequest != null) {
            mergeRequest.setTask(task);
            mergeRequestRepo.save(mergeRequest);
        }
        return MAIN_TREE;
    }
}

