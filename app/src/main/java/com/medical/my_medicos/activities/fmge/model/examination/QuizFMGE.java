package com.medical.my_medicos.activities.fmge.model.examination;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class QuizFMGE {
    private String title,title1;
    private Timestamp description;

    public QuizFMGE(String title, String title1, Timestamp to) {
        this.title = title;
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
}