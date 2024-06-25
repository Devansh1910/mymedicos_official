package com.medical.my_medicos.activities.fmge.model;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class QuizFmge {
    private String title,title1,id,type,slot;
    private Timestamp description;

    public QuizFmge(String title, String title1, Timestamp to, String id, String type, String slot) {
        this.title = title;
        this.slot = slot;
        this.id = id;
        this.type = type;
        this.title1=title1;
        this.description = to;
    }

    public String getTitle() {
        return title;
    }

    public String getSlot() {
        return slot;
    }

    public String getTitle1() {
        return title1;
    }
    //    public Timestamp getTo() {
//        return description;
//    }

    public String getType() {
        return type;
    }
    public Timestamp getTo() {
        return description;
    }

    public void setTo(Timestamp to) {
        this.description= to;
    }

    public String getId() {
        return id;
    }
}
