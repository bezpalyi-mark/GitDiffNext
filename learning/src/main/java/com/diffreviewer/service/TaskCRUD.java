package com.diffreviewer.service;

import com.diffreviewer.entities.Task;
import com.diffreviewer.entities.User;

import java.util.List;
import java.util.Optional;

public interface TaskCRUD {
    Optional<Task> getTaskByName(String taskName);

    Task findByReferenceInListNameAndUser(String taskName, User user);

    List<Task> getDoneTasksByUser(User user);
}
