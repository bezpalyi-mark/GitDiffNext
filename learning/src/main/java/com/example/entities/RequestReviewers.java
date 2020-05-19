package com.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "request_reviewer")
public class RequestReviewers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToOne
    private MergeRequest request;

    public RequestReviewers() {
    }

    public RequestReviewers(User user, MergeRequest request) {
        this.user = user;
        this.request = request;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MergeRequest getRequest() {
        return request;
    }

    public void setRequest(MergeRequest request) {
        this.request = request;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
