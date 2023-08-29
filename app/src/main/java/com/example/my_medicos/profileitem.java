package com.example.my_medicos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class profileitem {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();



    String info ;


    public profileitem() {
        FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                System.out.println(currentUser);


                // User is signed in
            } else {
                // User is signed out
            }
        };
    }

    public profileitem(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
