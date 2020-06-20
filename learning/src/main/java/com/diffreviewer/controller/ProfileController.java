package com.diffreviewer.controller;

import com.diffreviewer.entities.*;
import com.diffreviewer.repos.ListTaskRepo;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.TaskRepo;
import com.diffreviewer.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {

    public static final Logger LOGGER  = LoggerFactory.getLogger(ProfileController.class);

    private final MergeRequestRepo mergeRequestRepo;

    private final UserRepo userRepo;

    private final TaskRepo taskRepo;

    private final ListTaskRepo listTaskRepo;

    @Autowired
    public ProfileController(MergeRequestRepo mergeRequestRepo, UserRepo userRepo, TaskRepo taskRepo, ListTaskRepo listTaskRepo) {
        this.mergeRequestRepo = mergeRequestRepo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.listTaskRepo = listTaskRepo;
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

        if (!doneTasks.isEmpty()) {
            List<MergeRequest> mergeRequestList = new ArrayList<>();
            for (Task task : doneTasks) {
                Optional<ListTask> byId = listTaskRepo.findById(task.getReferenceInList().getId());
                byId.ifPresent(listTask -> mergeRequestList
                        .add(mergeRequestRepo.findByTaskReferenceInListAndStatusPR(listTask, Status.NOT_MERGED)));
            }
            if (!mergeRequestList.isEmpty()) {
                model.addAttribute("requests", mergeRequestList.iterator());
            }
        }

        List<MergeRequest> ownersRequests = mergeRequestRepo.findByCreatorPR(user);
        if (!ownersRequests.isEmpty()) {
            model.addAttribute("my_requests", ownersRequests);
        }
        return "profile";
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

    @PostMapping("/show")
    public String translator(User user, String url) {

        GitApi api = new GitApi(url);
        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter();
        String diffURL = api.getPR(user).getDiffURL();
        InputStream in;
        try {
            in = new URL(diffURL).openStream();
            Files.copy(in, Paths.get("input.diff"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage());
        }

        Process p = null;
        try {
            p = Runtime.getRuntime().exec("diff2html -F ./src/main/resources/templates/output-file.html -o stdout -i file -- input.diff");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        if(p != null) {
            LOGGER.info("Process go");
        }
        try {
            String body = htmlReaderWriter.getBody("./src/main/resources/templates/output-file.html");
            htmlReaderWriter.writeToDiffRev(body);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return "redirect:/dif-show";
    }

    @GetMapping("/dif-show")
    public String outputFile() {
        return "dif-show";
    }

}
