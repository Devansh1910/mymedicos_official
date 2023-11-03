//package com.example.my_medicos;
//
//public class jobitem1 {
//}
package com.example.my_medicos;

import android.util.Log;



public class jobitem1 {

    String position,speciality,Location,Organiser;
    String hospital;
    String location;
    String date,user,title;


    public jobitem1(String position, String hospital, String location,String date,String user,String title) {

        this.position = position;
        this.hospital = hospital;
        this.location = location;
        this.date=date;
        this.user=user;
        this.title=title;

        Log.d("speciality1",position);
        Log.d("speciality1",hospital);
        Log.d("speciality1",user);
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
    String getuser(){return user;}

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

