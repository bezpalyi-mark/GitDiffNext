package com.diffreviewer.entities;

import javax.persistence.*;

@Entity
@Table(name = "list_task")
public class ListTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "level_task")
    private int taskLevel;

    @OneToOne
    private ListTask previous;

    public ListTask(){

    }

    /**
     * @param id
     * @param name
     * @param taskLevel
     */
    public ListTask(Long id, String name, int taskLevel, ListTask listTask) {
        this.id = id;
        this.name = name;
        this.taskLevel = taskLevel;
        previous = listTask;
    }

    public ListTask getPrevious() {
        return previous;
    }

    public void setPrevious(ListTask previous) {
        this.previous = previous;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        name = name;
    }

    /**
     * @return the level_task
     */
    public int getTaskLevel() {
        return taskLevel;
    }

    /**
     * @param level_task the level_task to set
     */
    public void setTaskLevel(int level_task) {
        this.taskLevel = level_task;
    }

    
}