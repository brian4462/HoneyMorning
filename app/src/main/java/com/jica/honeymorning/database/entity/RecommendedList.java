package com.jica.honeymorning.database.entity;

public class RecommendedList {
    private int id;
    private int category_id;
    private String todo;
    private int status;

    public RecommendedList(){}
    public RecommendedList(int category_id, String todo, int status){
        this.category_id = category_id;
        this.todo = todo;
        this.status = status;
    }
    public RecommendedList(int id, int category_id, String todo, int status){
        this.id = id;
        this.category_id = category_id;
        this.todo = todo;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[id: " + id + " / categoryId: " + category_id  + " / value: " + todo + " / status: " + status + "]\n";
    }
}
