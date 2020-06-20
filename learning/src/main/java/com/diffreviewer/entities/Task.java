package com.diffreviewer.entities;


import javax.persistence.*;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isDone;

    @OneToOne
    @JoinColumn(name = "task_from_list_id")
    private ListTask referenceInList;

    public Task() {
    }

    public Task(Long id, ListTask referenceInList) {
        this.id = id;
        this.referenceInList = referenceInList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public ListTask getReferenceInList() {
        return referenceInList;
    }

    public void setReferenceInList(ListTask task) {
        this.referenceInList = task;
    }
}
