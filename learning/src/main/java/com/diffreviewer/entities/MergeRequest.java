package com.diffreviewer.entities;

import javax.persistence.*;

@Entity
@Table(name = "request")
public class MergeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// Pull request title.
    @Column(name = "title_pr")
    private String titlePR;

    /// Pull request description.
    @Column(name = "description_pr")
    private String descriptionPR;

    @OneToOne
    /// Pull request creator.
    @JoinColumn(name = "creator_pr_id")
    private User creatorPR;

    /// Pull request status.
    @CollectionTable(name = "pr_status", joinColumns = @JoinColumn(name = "request_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status statusPR;

    /// URL to diff file.
    @Column(name = "diff_url")
    private String diffURL;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
