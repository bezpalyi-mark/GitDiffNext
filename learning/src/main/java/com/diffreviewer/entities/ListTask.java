package com.diffreviewer.entities;

import javax.persistence.*;

@Entity
@Table(name = "list_task")
public class ListTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int level_task;

    public ListTask(){

    }

    /**
     * @param id
     * @param name
     * @param level_task
     */
    public ListTask(int id, String name, int level_task) {
        this.id = id;
        this.name = name;
        this.level_task = level_task;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
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
    public int getLevel_task() {
        return level_task;
    }

    /**
     * @param level_task the level_task to set
     */
    public void setLevel_task(int level_task) {
        this.level_task = level_task;
    }

    
}