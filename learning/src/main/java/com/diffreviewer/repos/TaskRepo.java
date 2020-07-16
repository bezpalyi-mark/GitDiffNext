package com.diffreviewer.repos;

import com.diffreviewer.entities.Task;
import com.diffreviewer.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends CrudRepository<Task, Integer> {
    List<Task> findByUserAndIsDone(User user, Boolean isDone);

    Task findByReferenceInListNameAndUser(String name, User user);

    Optional<Task> findByReferenceInListName(String name);
}
