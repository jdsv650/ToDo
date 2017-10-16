package com.jdsv650.todo;

/**
 * Created by james on 9/14/17.
 */

// ToDo item
public class ToDo {

    private Long id = -1l;
    private String title = "";
    private String description = "";
    private String date = "";
    private Integer status;


    public ToDo(String title, String description, String date, Integer status)
    {
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
