package com.example.my_medicos;

public class jobitem {

    String position,speciality,Location,Organiser,date;
    String hospital;
    String location,title,category;

    public jobitem(String position, String hospital, String location,String date,String title,String category) {

        this.position = position;
        this.hospital = hospital;
        this.location = location;
        this.date=date;
        this.title=title;
        this.category= category;

    }

    public String getPosition() {
        return position;
    }
    public String getTitle() {
        return title;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHospital() {
        return hospital;
    }
    public String getDate() {
        return date;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
