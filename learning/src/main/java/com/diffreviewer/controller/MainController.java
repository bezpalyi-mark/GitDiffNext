package com.diffreviewer.controller;

import com.diffreviewer.entities.ListTask;
import com.diffreviewer.entities.Task;
import com.diffreviewer.entities.User;
import com.diffreviewer.entities.request.SaveMergeRequest;
import com.diffreviewer.exception.NoSuchMergeRequestOnGitTea;
import com.diffreviewer.exception.TaskNotAssignedToThisUserException;
import com.diffreviewer.exception.UserNotFoundException;
import com.diffreviewer.repos.UserRepo;
import com.diffreviewer.service.ListTaskCRUD;
import com.diffreviewer.service.MergeRequestCRUD;
import com.diffreviewer.service.MergeRequestService;
import com.diffreviewer.service.TaskCRUD;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    public static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final MergeRequestCRUD mergeRequestCRUD;

    private final UserRepo userRepo;

    private final TaskCRUD taskCRUD;

    private final ListTaskCRUD listTaskCRUD;

    private static final String MAIN_TREE = "/main-tree";

    @Autowired
    public MainController(MergeRequestService mergeRequestCRUD, UserRepo userRepo, TaskCRUD taskCRUD, ListTaskCRUD listTaskCRUD) {
        this.mergeRequestCRUD = mergeRequestCRUD;
        this.userRepo = userRepo;
        this.taskCRUD = taskCRUD;
        this.listTaskCRUD = listTaskCRUD;
    }

    @GetMapping("/")
    public String greeting() {
        return "redirect:" + MAIN_TREE;
    }

    @GetMapping("/main-tree")
    public String main(Model model) {
        model.addAttribute("existTasks", listTaskCRUD.getAll());
        return MAIN_TREE;
    }

    @PostMapping(MAIN_TREE)
    public String addRequest(@RequestParam String mr, @RequestParam String selectTask) {
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
        Task task = taskCRUD.findByReferenceInListNameAndUser(selectTask, user);
        if (task == null) {
            LOGGER.error("No task for this user given!");
            throw new TaskNotAssignedToThisUserException(selectTask, username);
        }
        GitApi gitApi = new GitApi(mr);
        SaveMergeRequest request = gitApi.getPR(user);
        if (request != null) {
            request.setTaskName(task.getReferenceInList().getName());
            mergeRequestCRUD.create(request);
        } else {
            LOGGER.error("No such merge request on Git Tea");
            throw new NoSuchMergeRequestOnGitTea();
        }
        return MAIN_TREE;
    }
}

