package com.jica.honeymorning.database.entity;

import java.util.Date;

public class TodoValue {
    int id;
    String note;
    int status;
    String created_at;


    public TodoValue(){ }
    public TodoValue(String note){
        this.note = note;
    }
    public TodoValue(String note, int status){
        this.note = note;
        this.status = status;
    }
    public TodoValue(Date date, String note, int status){
        this.note = note;
        this.status = status;
    }
    public TodoValue(int id, Date date, String note, int status){
        this.id = id;
        this.note = note;
        this.status = status;
    }
    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String toDo) {
        this.note = toDo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreatedAt(String created_at){ this.created_at = created_at; }

    public String getCreated_at() {
        return created_at;
    }

    @Override
    public String toString() {
        return "[id: " + id + " / note: " + note  + " / status: " + status + " / date: " + created_at + "]\n";
    }
}
