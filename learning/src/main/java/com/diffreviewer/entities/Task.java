package com.diffreviewer.entities;


import javax.persistence.*;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isDone;

    @OneToOne
    @JoinColumn(name = "task_from_list_id")
    private ListTask task;

    public Task() {
    }

    public Task(Long id, String name, ListTask task) {
        this.id = id;
        this.name = name;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ListTask getTask() {
        return task;
    }

    public void setTask(ListTask task) {
        this.task = task;
    }
}
