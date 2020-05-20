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
    public String profile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = userRepo.findByUsername(username);
        model.addAttribute("login", currentUser.getUsername());
        model.addAttribute("curse", "novalab");
        model.addAttribute("role", currentUser.getRoles());
        if (currentUser.getRoles().contains(Role.ADMIN)) {
            model.addAttribute("requests", mergeRequestRepo.findAll());
            return "profile";
        }
        List<Task> doneTasks = taskRepo.findByUserAndIsDone(currentUser, true);
        List<MergeRequest> mergeRequestList = new ArrayList<>();
        for (Task task : doneTasks) {
            mergeRequestList.add(mergeRequestRepo.findByTaskAndStatusPR(task, Status.NOT_MERGED));
        }
        model.addAttribute("requests", mergeRequestList);
        return "profile";
    }

    @PostMapping("/main-tree")
    public String addRequest(@AuthenticationPrincipal User user,
                             @RequestParam String mrInput,
                             @RequestParam String selectTask) {
        if (!mrInput.matches("https?:\\/\\/(www\\.)?" +
                "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")) {
            System.out.println("Bad url!");
            return "/main-tree";
        }
        Task task = taskRepo.findByNameAndUser(selectTask, user);
        if (task == null) {
            System.out.println("No task for this user given!");
            return "/main-tree";
        }
        GitApi gitApi = new GitApi(mrInput);
        MergeRequest mergeRequest = gitApi.GetPR();
        mergeRequest.setTask(task);
        mergeRequestRepo.save(mergeRequest);
        return "/main-tree";
    }

    @GetMapping("/main-tree")
    public String main(Model model) {
        List<ListTask> tasks = (List<ListTask>)listTaskRepo.findAll();
        if(tasks.size() == 0)
        {
            tasks.add(new ListTask(1, "No tasks", 0));
        }
        model.addAttribute("existTasks", tasks);
        return "main-tree";
    }

    @GetMapping("/tree")
    public String tree(Map<String, Object> model) {

        return "redirect:/main-tree";
    }

    @PostMapping("/profile")
    public String logoutPage(Model model) {

        return "redirect:/login?logout";
    }
}

