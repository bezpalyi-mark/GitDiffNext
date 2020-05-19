package com.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class RequestComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String text;

    @OneToOne
    @JoinColumn(name = "request_id")
    private MergeRequest request;

    public RequestComment() {
    }

    public RequestComment(User author, String text) {
        this.author = author;
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MergeRequest getRequest() {
        return request;
    }

    public void setRequest(MergeRequest request) {
        this.request = request;
    }
}
