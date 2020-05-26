package com.diffreviewer.repos;

import com.diffreviewer.entities.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MergeRequestRepo extends CrudRepository<MergeRequest, Long> {
    MergeRequest findByTaskAndStatusPR(Task task, Status status);
    MergeRequest findByTaskNameAndStatusPR(String taskName, Status status);
    MergeRequest findByTask_TaskAndStatusPR(ListTask taskFromList, Status status);
    List<MergeRequest> findByCreatorPR(User user);
}
