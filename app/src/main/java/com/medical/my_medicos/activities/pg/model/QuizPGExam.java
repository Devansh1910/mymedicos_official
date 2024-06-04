package com.medical.my_medicos.activities.pg.model;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class QuizPGExam {
    private String title,title1,id;
    private Timestamp from,to;

    public QuizPGExam(String title, String title1, Timestamp from, String id ,Timestamp to) {
        this.title = title;
        this.id = id;
        this.title1=title1;
        this.to = to;
        this.from = from;
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
        return to;
    }

    public void setTo(Timestamp to) {
        this.to= to;
    }

    public String getId() {
        return id;
    }

    public Timestamp getFrom() {
        return from;
    }

    public void setFrom(Timestamp from) {
        this.from= from;
    }

}
