package com.diffreviewer.controller;

import com.diffreviewer.entities.*;
import com.diffreviewer.repos.ListTaskRepo;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.TaskRepo;
import com.diffreviewer.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
public class MainController {

    private boolean adminExists = false;

    @Autowired
    private MergeRequestRepo mergeRequestRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private ListTaskRepo listTaskRepo;

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

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        if (user.getRoles().contains(Role.ADMIN)) {
            return "redirect:/user";
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = userRepo.findByUsername(username);
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("login", currentUser.getUsername());
        model.addAttribute("curse", "novalab");
        model.addAttribute("role", currentUser.getRoles());
        List<Task> doneTasks = taskRepo.findByUserAndIsDone(currentUser, true);

        if (doneTasks.size() > 0) {
            List<MergeRequest> mergeRequestList = new ArrayList<>();
            for (Task task : doneTasks) {
                Optional<ListTask> byId = listTaskRepo.findById((long) task.getTask().getId());
                byId.ifPresent(listTask -> mergeRequestList
                        .add(mergeRequestRepo.findByTask_TaskAndStatusPR(listTask, Status.NOT_MERGED)));
            }
            if (mergeRequestList.size() > 0) {
                model.addAttribute("requests", mergeRequestList.iterator());
            }
        }
        return "profile";
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

    @GetMapping("/main-tree")
    public String main(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        Iterable<ListTask> listTasks = listTaskRepo.findAll();
        model.addAttribute("existTasks", listTasks);
        return "main-tree";
    }

    public void Translator(User user, String url)
    {
        GitApi api = new GitApi(url);
        String diff_url = api.GetPR(user).getDiffURL();
        InputStream in;
        try {
            in = new URL(diff_url).openStream();
            Files.copy(in, Paths.get("input.diff"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Process p;
        //NEED INSTALL THROUGH NPM
        try
        {
            p = Runtime.getRuntime().exec("diff2html -F output-file.html -i file -- input.diff");
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @GetMapping("/diff_rev")
    public void Diff2Html(@AuthenticationPrincipal User user) {
        Translator(user, "https://try.gitea.io/AlexKushch/test/pulls/2");
    }
}

