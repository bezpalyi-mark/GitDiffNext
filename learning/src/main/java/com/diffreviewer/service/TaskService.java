package com.diffreviewer.service;

import com.diffreviewer.entities.Task;
import com.diffreviewer.entities.User;
import com.diffreviewer.repos.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements TaskCRUD {

    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public Optional<Task> getTaskByName(String taskName) {
        return taskRepo.findByReferenceInListName(taskName);
    }

    @Override
    public Task findByReferenceInListNameAndUser(String taskName, User user) {
        return taskRepo.findByReferenceInListNameAndUser(taskName, user);
    }

    @Override
    public List<Task> getDoneTasksByUser(User user) {
        return taskRepo.findByUserAndIsDone(user, true);
    }
}
