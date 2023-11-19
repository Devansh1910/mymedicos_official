package com.example.my_medicos.activities.pg.model;

public class SpecialitiesPG {
    private String pgname, pgicon, pgcolor, pgbrief;
    private int pgid;

    public SpecialitiesPG(String name, String icon, String color, String brief, int id) {
        this.pgname = name;
        this.pgicon = icon;
        this.pgcolor = color;
        this.pgbrief = brief;
        this.pgid = id;
    }

    public String getName() {
        return pgname;
    }

    public void setName(String name) {
        this.pgname = name;
    }

    public String getIcon() {
        return pgicon;
    }

    public void setIcon(String icon) {
        this.pgicon = icon;
    }

    public String getColor() {
        return pgcolor;
    }

    public void setColor(String color) {
        this.pgcolor = color;
    }

    public String getBrief() {
        return pgbrief;
    }

    public void setBrief(String brief) {
        this.pgbrief = brief;
    }

    public int getId() {
        return pgid;
    }

    public void setId(int id) {
        this.pgid = id;
    }
}
