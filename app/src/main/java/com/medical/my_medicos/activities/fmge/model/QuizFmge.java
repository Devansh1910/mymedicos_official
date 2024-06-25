package com.medical.my_medicos.activities.fmge.model;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class QuizFmge {
    private String title,title1,id;
    private Timestamp description;

    public QuizFmge(String title, String title1, Timestamp to, String id) {
        this.title = title;
        this.id = id;
        this.title1=title1;
        this.description = to;
    }

    public String getTitle() {
        return title;
    }
    public String getTitle1() {
        return title1;
    }
    //    public Timestamp getTo() {
//        return description;
//    }
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
