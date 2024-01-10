package com.jkfd.oopii.Database.Models;

public class Todo extends Element {
    private String title;
    private String description;


    public Todo(){

    }
    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getter und Setter
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
}
