package com.jica.honeymorning.database.entity;

public class CategoryValue {
    private int id;
    private String category_Info;
    private int status;

    public CategoryValue(){

    }
    public CategoryValue(String category_Info){
        this.category_Info = category_Info;
    }
    public CategoryValue(int id, String category_Info){
        this.id = id;
        this.category_Info = category_Info;
    }
    public CategoryValue(int id, String category_Info, int status){
        this.id = id;
        this.category_Info = category_Info;
        this.status = status;
    }
    //getters, setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_Info() {
        return category_Info;
    }

    public void setCategory_Info(String category_Info) {
        this.category_Info = category_Info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[id: " + id + " / category: " + category_Info  + " / available: " + status + "]\n";
    }
}
