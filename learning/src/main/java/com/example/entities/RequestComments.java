package com.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "request_comment")
public class RequestComments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private RequestComment comment;

    @OneToOne
    private MergeRequest request;

    public RequestComments() {
    }

    public RequestComments(RequestComment comment, MergeRequest request) {
        this.comment = comment;
        this.request = request;
    }

    public RequestComment getComment() {
        return comment;
    }

    public void setComment(RequestComment comment) {
        this.comment = comment;
    }

    public MergeRequest getRequest() {
        return request;
    }

    public void setRequest(MergeRequest request) {
        this.request = request;
    }
}
