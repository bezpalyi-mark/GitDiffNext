package com.diffreviewer.controller;

import com.diffreviewer.entities.*;
import com.diffreviewer.exception.UserNotFoundException;
import com.diffreviewer.repos.UserRepo;
import com.diffreviewer.service.ListTaskCRUD;
import com.diffreviewer.service.MergeRequestCRUD;
import com.diffreviewer.service.TaskCRUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    private final MergeRequestCRUD mergeRequestCRUD;

    private final UserRepo userRepo;

    private final TaskCRUD taskCRUD;

    private final ListTaskCRUD listTaskCRUD;

    private Integer waitTimeInSeconds = 20;

    private final HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter();

    @Autowired
    public ProfileController(MergeRequestCRUD mergeRequestCRUD, UserRepo userRepo, TaskCRUD taskCRUD, ListTaskCRUD listTaskCRUD) {
        this.mergeRequestCRUD = mergeRequestCRUD;
        this.userRepo = userRepo;
        this.taskCRUD = taskCRUD;
        this.listTaskCRUD = listTaskCRUD;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User currentUser = getUser();
        if (currentUser.getRoles().contains(Role.ADMIN)) {
            return "redirect:/user";
        }

        model.addAttribute("login", currentUser.getUsername());
        model.addAttribute("curse", "novalab");
        model.addAttribute("role", currentUser.getRoles());
        List<Task> doneTasks = taskCRUD.getDoneTasksByUser(currentUser);

        if (!doneTasks.isEmpty()) {
            List<MergeRequest> mergeRequestList = new ArrayList<>();
            for (Task task : doneTasks) {
                Optional<ListTask> byId = listTaskCRUD.getById(task.getReferenceInList().getId());
                byId.ifPresent(listTask -> mergeRequestList
                        .add(mergeRequestCRUD.findByTaskReferenceInListAndStatusPR(listTask, Status.NOT_MERGED)));
            }
            if (!mergeRequestList.isEmpty()) {
                model.addAttribute("requests", mergeRequestList.iterator());
            }
        }

        List<MergeRequest> ownersRequests = mergeRequestCRUD.getByCreator(currentUser);
        if (!ownersRequests.isEmpty()) {
            model.addAttribute("my_requests", ownersRequests);
        }
        return "profile";
    }

    @PostMapping("/show")
    public String translator(String url) {
        InputStream in;
        try {
            in = new URL(url + ".diff").openStream();
            Files.copy(in, Paths.get("input.diff"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage());
        }

        Process p;
        try {
            p = Runtime.getRuntime().exec("diff2html -F ./src/main/resources/templates/output-file.html -o stdout -i file -- input.diff");
            if (p != null) {
                LOGGER.info("Process go");
                try(BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                    String outputText = output.readLine();
                    int times = 0;
                    while(outputText.equals("") && times < waitTimeInSeconds) {
                        Thread.sleep(1000);
                        times++;
                    }
                    LOGGER.info(outputText);
                    if(p.isAlive()) {
                        p.destroy();
                        if( outputText.equals("")) {
                            LOGGER.error("Process was destroyed");
                        } else {
                            LOGGER.warn("Process done but was terminated");
                        }
                    } else {
                        return "redirect:/review";
                    }
                }
            } else {
                LOGGER.error("Cannot run process");
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        return "redirect:/review";
    }

    @GetMapping("/dif-show")
    public String outputFile() {
        return "dif-show";
    }

    @GetMapping(value = "/review", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String review() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String body = htmlReaderWriter.getBody("./src/main/resources/templates/output-file.html");
        stringBuilder.append(HtmlReaderWriter.HEAD);
        stringBuilder.append(HtmlReaderWriter.HEADER);
        stringBuilder.append(body);
        return stringBuilder.toString();
    }

    private User getUser() {
        User user;
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        user = userRepo.findByUsername(username);
        if (user == null) {
            LOGGER.error("Problems with user");
            throw new UserNotFoundException(username);
        }
        return user;
    }

    public Integer getWaitTimeInSeconds() {
        return waitTimeInSeconds;
    }

    public void setWaitTimeInSeconds(Integer waitTimeInSeconds) {
       this.waitTimeInSeconds = waitTimeInSeconds;
    }

}
