package com.diffreviewer.repos;

import com.diffreviewer.entities.MergeRequest;
import com.diffreviewer.entities.Status;
import com.diffreviewer.entities.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MergeRequestRepo extends CrudRepository<MergeRequest, Long> {
    MergeRequest findByTaskAndStatusPR(Task task, Status status);
}
