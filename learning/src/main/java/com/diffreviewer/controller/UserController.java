package com.diffreviewer.controller;

import java.util.ArrayList;
import java.util.List;

import com.diffreviewer.entities.ListTask;
import com.diffreviewer.entities.User;
import com.diffreviewer.repos.ListTaskRepo;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ListTaskRepo listTaskRepo;

    @Autowired
    private MergeRequestRepo mergeRequestRepo;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("curse", "example");
        model.addAttribute("login", "example58");
        model.addAttribute("role", "admin");

        model.addAttribute("requests", mergeRequestRepo.findAll());
        return "profile-admin";
    }

    @GetMapping("/admin-panel")
    public String adminPanel(Model model) {
        Iterable<User> users = userRepo.findAll();
        Iterable<ListTask> tasks = listTaskRepo.findAll();

        model.addAttribute("users", users);
        model.addAttribute("tasks", tasks);

        return "admin-panel";
    }

    @RequestMapping(value = "/deleteTask", method = RequestMethod.POST)
    public String deleteTaskRow(@ModelAttribute("ListTaskModel") ListTask toDeleteTask) 
    {
        listTaskRepo.delete(toDeleteTask);

        return "admin-panel";
    }

    @RequestMapping(value = "/insertTask", method = RequestMethod.POST)
    public String insertTaskRow(@ModelAttribute("ListTaskModel") ListTask insertTask) 
    {
        listTaskRepo.save(insertTask);

        return "admin-panel";
    }

    @RequestMapping(value = "/changeTask", method = RequestMethod.POST)
    public String changeTaskRow(@ModelAttribute("ListTaskModel") ListTask toChangeTask) 
    {
        List<ListTask> tasks = new ArrayList<ListTask>();
        listTaskRepo.findAll().forEach(tasks::add);

        for(ListTask task : tasks)
        {
            if(task.getName() == toChangeTask.getName() ||
                task.getPrevious() == toChangeTask.getPrevious() || 
                task.getTaskLevel() == toChangeTask.getTaskLevel())
                {
                    toChangeTask.setId(task.getId());
                    break;
                }
        }

        listTaskRepo.save(toChangeTask);

        return "admin-panel";
    }
}
