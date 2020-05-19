package com.diffreviewer.controller;

import com.diffreviewer.entities.MergeRequest;
import com.diffreviewer.entities.Message;
import com.diffreviewer.entities.Role;
import com.diffreviewer.entities.User;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.MessageRepo;
import com.diffreviewer.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;


@Controller
public class MainController {

    private boolean adminExists = false;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private MergeRequestRepo mergeRequestRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        if(!adminExists) {
            HashPassword hashPassword = new HashPassword();
            User user = userRepo.findByUsername("admin");
            if(user == null) {
                user = new User();
            } else {
                return "greeting";
            }
            user.setActive(true);
            user.setUsername("admin");
            user.setPassword(hashPassword.encode("admin"));
            user.setRoles(Collections.singleton(Role.ADMIN));
            userRepo.save(user);
            adminExists = true;
        }
        return "greeting";
    }

    @GetMapping("/review")
    public String review(Map<String, Object> model) {
        Iterable<MergeRequest> requestList = mergeRequestRepo.findAll();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = userRepo.findByUsername(username);
        for(MergeRequest request : requestList) {
//            if(request.isReviewer(currentUser)) {
//                model.put("request", request);
//            }
        }
        return "review";
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

//    @PostMapping("/main-tree")
//    public String add(
//            @AuthenticationPrincipal User user,
//            @RequestParam String text,
//            @RequestParam String tag,
//            Map<String, Object> model) {
//
//        return "main-tree";
//    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter,
                         Map<String, Object> model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.put("messages", messages);
        return "main";
    }
}

