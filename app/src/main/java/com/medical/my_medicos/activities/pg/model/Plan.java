package com.medical.my_medicos.activities.pg.model;

public class Plan {
    private String title;
    private String description;
    private String price;

    public Plan(String title, String description, String price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}
