package com.diffreviewer.service;

import com.diffreviewer.entities.ListTask;
import com.diffreviewer.entities.MergeRequest;
import com.diffreviewer.entities.Status;
import com.diffreviewer.entities.User;
import com.diffreviewer.entities.request.SaveMergeRequest;

import java.util.List;
import java.util.Optional;

public interface MergeRequestCRUD {

    MergeRequest create(SaveMergeRequest request);

    void update(Long id, SaveMergeRequest request);

    List<MergeRequest> getAll();

    Optional<MergeRequest> getById(Long id);

    Optional<MergeRequest> deleteById(Long id);

    MergeRequest findByTaskReferenceInListAndStatusPR(ListTask listTask, Status status);

    List<MergeRequest> getByCreator(User user);

    MergeRequest getByUrl(String url);
}
