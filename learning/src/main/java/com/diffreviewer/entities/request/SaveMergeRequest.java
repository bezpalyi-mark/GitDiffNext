package com.diffreviewer.entities.request;

import com.diffreviewer.entities.RequestComment;

import java.util.ArrayList;
import java.util.List;

public class SaveMergeRequest {

    private String titlePRName;

    private String descriptionPR = "";

    private String creatorPRName;

    private String statusPRName;

    private String diffURLName;

    private String taskName;

    private Integer approveCount;

    private final List<RequestComment> comments = new ArrayList<>();

    public String getTitlePRName() {
        return titlePRName;
    }

    public void setTitlePRName(String titlePRName) {
        this.titlePRName = titlePRName;
    }

    public String getDescriptionPR() {
        return descriptionPR;
    }

    public void setDescriptionPR(String descriptionPR) {
        this.descriptionPR = descriptionPR;
    }

    public String getCreatorPRName() {
        return creatorPRName;
    }

    public void setCreatorPRName(String creatorPRName) {
        this.creatorPRName = creatorPRName;
    }

    public String getStatusPRName() {
        return statusPRName;
    }

    public void setStatusPRName(String statusPRName) {
        this.statusPRName = statusPRName;
    }

    public String getDiffURLName() {
        return diffURLName;
    }

    public void setDiffURLName(String diffURLName) {
        this.diffURLName = diffURLName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getApproveCount() {
        return approveCount;
    }

    public void setApproveCount(Integer approveCount) {
        this.approveCount = approveCount;
    }

    public void addRequestComment(RequestComment requestComment) {
        comments.add(requestComment);
    }

}
