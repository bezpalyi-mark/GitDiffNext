package com.diffreviewer.controller;

import com.diffreviewer.entities.*;
import com.diffreviewer.repos.ListTaskRepo;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.TaskRepo;
import com.diffreviewer.repos.UserRepo;
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

    @Autowired
    private MergeRequestRepo mergeRequestRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private ListTaskRepo listTaskRepo;

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

        List<MergeRequest> ownersRequests = mergeRequestRepo.findByCreatorPR(user);
        if(ownersRequests.size() > 0) {
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

//    @PostMapping("/show")
    @PostMapping("/show")
    public String Translator(User user, String url) {

        GitApi api = new GitApi(url);
        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter();
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
        try
        {
            p = Runtime.getRuntime().exec("diff2html -F ./src/main/resources/templates/output-file.html -o stdout -i file -- input.diff");
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        try {
            String body = htmlReaderWriter.getBody("./src/main/resources/templates/output-file.html");
            htmlReaderWriter.writeToDiffRev(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/dif-show";
    }

    @GetMapping("/dif-show")
    public String outputFile() {
        return "dif-show";
    }

}
