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
    private Task previous;

    public Task() {
    }

    public Task(Long id, String name, Task previous) {
        this.id = id;
        this.name = name;
        this.previous = previous;
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

    public Task getPrevious() {
        return previous;
    }

    public void setPrevious(Task previous) {
        this.previous = previous;
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
}
