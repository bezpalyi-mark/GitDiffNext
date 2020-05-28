package com.diffreviewer.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.diffreviewer.entities.ListTask;
import com.diffreviewer.entities.User;
import com.diffreviewer.repos.ListTaskRepo;
import com.diffreviewer.repos.MergeRequestRepo;
import com.diffreviewer.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUserRow(@ModelAttribute("ListTaskModel") User toDeleteUser) 
    {
        userRepo.delete(toDeleteUser);

        return "admin-panel";
    }

    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    public String insertUserRow(@ModelAttribute("ListTaskModel") User insertUser) 
    {
        userRepo.save(insertUser);

        return "admin-panel";
    }

    @RequestMapping(value = "/changeUser", method = RequestMethod.POST)
    public String changeUserRow(@ModelAttribute("ListTaskModel") User toChangeUser) 
    {
        List<User> users = new ArrayList<User>();
        userRepo.findAll().forEach(users::add);

        for(User user : users)
        {
            if(user.getUsername() == toChangeUser.getUsername() ||
                user.getPassword() == toChangeUser.getPassword())
                {
                    toChangeUser.setId(user.getId());
                    break;
                }
        }

        userRepo.save(toChangeUser);

        return "admin-panel";
    }
//
//    @PostMapping("/show")
//    public String Translator(@AuthenticationPrincipal User user, String url) {
//
//        GitApi api = new GitApi(url);
//        HtmlReaderWriter htmlReaderWriter = new HtmlReaderWriter();
//        String diff_url = api.GetPR(user).getDiffURL();
//        InputStream in;
//        try {
//            in = new URL(diff_url).openStream();
//            Files.copy(in, Paths.get("input.diff"), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//        Process p;
//        try
//        {
//            p = Runtime.getRuntime().exec("diff2html -F ./src/main/resources/templates/output-file.html -o stdout -i file -- input.diff");
//        } catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//        try {
//            String body = htmlReaderWriter.getBody("./src/main/resources/templates/output-file.html");
//            htmlReaderWriter.writeToDiffRev(body);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return "redirect:/dif-show";
//    }
}
