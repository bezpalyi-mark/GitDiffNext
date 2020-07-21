package com.diffreviewer.service;

import com.diffreviewer.entities.ListTask;
import com.diffreviewer.entities.MergeRequest;
import com.diffreviewer.entities.Status;
import com.diffreviewer.entities.User;
import com.diffreviewer.entities.request.SaveMergeRequest;
import com.diffreviewer.exception.MergeRequestNotFoundException;
import com.diffreviewer.exception.NoSuchStatusException;
import com.diffreviewer.exception.TaskNotFoundException;
import com.diffreviewer.exception.UserNotFoundException;
import com.diffreviewer.repos.MergeRequestRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MergeRequestService implements MergeRequestCRUD {

    private final UserCRUD userCRUD;

    private final TaskCRUD taskCRUD;

    private final MergeRequestRepo mergeRequestRepo;

    public MergeRequestService(UserCRUD userCRUD, TaskCRUD taskCRUD, MergeRequestRepo mergeRequestRepo) {
        this.userCRUD = userCRUD;
        this.taskCRUD = taskCRUD;
        this.mergeRequestRepo = mergeRequestRepo;
    }

    @Override
    public MergeRequest create(SaveMergeRequest request) {
        MergeRequest mergeRequest = new MergeRequest();
        return mergeRequestRepo.save(createMergeRequest(mergeRequest, request));
    }

    @Override
    public void update(Long id, SaveMergeRequest request) {
        MergeRequest mergeRequest = mergeRequestRepo.findById(id).orElseThrow(() -> new MergeRequestNotFoundException(id));
        createMergeRequest(mergeRequest, request);
        mergeRequestRepo.save(mergeRequest);
    }

    @Override
    public List<MergeRequest> getAll() {
        List<MergeRequest> list = new ArrayList<>();
        mergeRequestRepo.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Optional<MergeRequest> getById(Long id) {
        return mergeRequestRepo.findById(id);
    }

    @Override
    public Optional<MergeRequest> deleteById(Long id) {
        Optional<MergeRequest> mergeRequest = mergeRequestRepo.findById(id);
        mergeRequest.ifPresent(mergeRequestRepo::delete);
        return mergeRequest;
    }

    @Override
    public MergeRequest findByTaskReferenceInListAndStatusPR(ListTask listTask, Status status) {
        return mergeRequestRepo.findByTaskReferenceInListAndStatusPR(listTask, status);
    }

    @Override
    public List<MergeRequest> getByCreator(User user) {
        return mergeRequestRepo.findByCreatorPR(user);
    }

    @Override
    public MergeRequest getByUrl(String url) {
        return mergeRequestRepo.findByDiffURL(url);
    }

    private MergeRequest createMergeRequest(MergeRequest mergeRequest, SaveMergeRequest request) {
        User user = userCRUD.getUserByUsername(request.getCreatorPRName());
        if (user == null) {
            throw new UserNotFoundException(request.getCreatorPRName());
        }
        mergeRequest.setCreatorPR(user);
        mergeRequest.setApproveCount(request.getApproveCount());
        mergeRequest.setDescriptionPR(request.getDescriptionPR());
        mergeRequest.setDiffURL(request.getDiffURLName());
        mergeRequest.setTitlePR(request.getTitlePRName());
        mergeRequest.setTask(taskCRUD.getTaskByName(request.getTaskName())
                .orElseThrow(() -> new TaskNotFoundException(request.getTaskName())));
        try {
            mergeRequest.setStatusPR(Status.valueOf(request.getStatusPRName().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new NoSuchStatusException(request.getStatusPRName().toUpperCase());
        }
        return mergeRequest;
    }
}
