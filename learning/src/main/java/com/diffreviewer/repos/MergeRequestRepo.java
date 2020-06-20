package com.diffreviewer.repos;

import com.diffreviewer.entities.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MergeRequestRepo extends CrudRepository<MergeRequest, Long> {
    MergeRequest findByTaskAndStatusPR(Task task, Status status);
    MergeRequest findByTaskReferenceInListAndStatusPR(ListTask taskFromList, Status status);
    List<MergeRequest> findByCreatorPR(User user);
}
