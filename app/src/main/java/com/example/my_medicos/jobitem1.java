//package com.example.my_medicos;
//
//public class jobitem1 {
//}
package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;



public class jobitem1 {

    String position,speciality,Location,Organiser;
    String hospital;
    String location;


    public jobitem1(String position, String hospital, String location) {

        this.position = position;
        this.hospital = hospital;
        this.location = location;
        Log.d("speciality1",position);
        Log.d("speciality1",hospital);
        Log.d("speciality1",location);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHospital() {
        return hospital;
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
}

