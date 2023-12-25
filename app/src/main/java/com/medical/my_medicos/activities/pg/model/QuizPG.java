package com.medical.my_medicos.activities.pg.model;
public class QuizPG {
    private String title,title1;
    private String description;

    public QuizPG(String title,String title1) {
        this.title = title;
        this.title1=title1;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }
    public String getTitle1() {
        return title1;
    }

    public String getDescription() {
        return description;
    }
}