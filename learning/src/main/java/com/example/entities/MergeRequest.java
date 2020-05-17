package com.example.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "request")
public class MergeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /// Pull request title.
    private String titlePR;
    /// Pull request description.
    private String descriptionPR;

    @OneToOne
    /// Pull request creator.
    private User creatorPR;

    @OneToMany
    private final List<User> reviewers = new ArrayList<>();

    /// Pull request status.
    private Status statusPR;
    /// URL to diff file.
    private String diffURL;

    @OneToMany
    private List<RequestComment> discussions = new ArrayList<>();

    public MergeRequest() {
    }

    public MergeRequest(String titlePR, String descriptionPR, User creatorPR, Status statusPR, String diffURL) {
        this.titlePR = titlePR;
        this.descriptionPR = descriptionPR;
        this.creatorPR = creatorPR;
        this.statusPR = statusPR;
        this.diffURL = diffURL;
    }

    public String getTitlePR() {
        return titlePR;
    }

    public void setTitlePR(String titlePR) {
        this.titlePR = titlePR;
    }

    public String getDescriptionPR() {
        return descriptionPR;
    }

    public void setDescriptionPR(String descriptionPR) {
        this.descriptionPR = descriptionPR;
    }

    public User getCreatorPR() {
        return creatorPR;
    }

    public void setCreatorPR(User creatorPR) {
        this.creatorPR = creatorPR;
    }

    public Status getStatusPR() {
        return statusPR;
    }

    public void setStatusPR(Status statusPR) {
        this.statusPR = statusPR;
    }

    public String getDiffURL() {
        return diffURL;
    }

    public void setDiffURL(String diffURL) {
        this.diffURL = diffURL;
    }

    public List<RequestComment> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<RequestComment> discussions) {
        this.discussions = discussions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addRequestComment(RequestComment requestComment){
        discussions.add(requestComment);
    }

    public boolean isReviewer(User user) {
        return reviewers.contains(user);
    }

    public List<User> getReviewers() {
        return reviewers;
    }

    public void addReviewer(User user) {
        reviewers.add(user);
    }
}
