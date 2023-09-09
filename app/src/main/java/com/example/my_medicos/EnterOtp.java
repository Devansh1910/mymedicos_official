package com.example.my_medicos;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {


    EditText input1, input2, input3, input4, input5, input6;
    Button submit;

    String getotpbackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        input1 = findViewById(R.id.inputotp1);
        input2 = findViewById(R.id.inputotp2);
        input3 = findViewById(R.id.inputotp3);
        input4 = findViewById(R.id.inputotp4);
        input5 = findViewById(R.id.inputotp5);
        input6 = findViewById(R.id.inputotp6);

        final ProgressBar progressBar=findViewById(R.id.otpprogressbar);

        TextView shownumber = findViewById(R.id.showmobilenumber);
        shownumber.setText(String.format(getIntent().getStringExtra("mobile")));

        TextView countrycode = findViewById(R.id.Showcountrycode);
        countrycode.setText(String.format("+" + getIntent().getStringExtra("Countrycode")));

        submit=findViewById(R.id.submit_otp);

        getotpbackend=getIntent().getStringExtra("backendotp");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!input1.getText().toString().trim().isEmpty() && !input2.getText().toString().trim().isEmpty() && !input3.getText().toString().trim().isEmpty() && !input4.getText().toString().trim().isEmpty() && !input5.getText().toString().trim().isEmpty() && !input6.getText().toString().trim().isEmpty())
                {
                    String enterotp=input1.getText().toString()+input2.getText().toString()+
                            input3.getText().toString()+input4.getText().toString()+input5.getText().toString()+input6.getText().toString();
                    if(getotpbackend!=null){
                        progressBar.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(getotpbackend,enterotp);

                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);

                                if(task.isSuccessful()){
                                    Intent i=new Intent(EnterOtp.this, HomeActivity.class);
                                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Toast.makeText(EnterOtp.this, "Enter Correct OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(EnterOtp.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(EnterOtp.this, "Enter all digits", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        numberotpmove();

        findViewById(R.id.resend_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + shownumber.getText().toString().trim(), 60, TimeUnit.SECONDS, EnterOtp.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(EnterOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(newbackendotp, forceResendingToken);
                        getotpbackend=newbackendotp;
                        Toast.makeText(EnterOtp.this, "OTP Sent successfully", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    private void numberotpmove() {

        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if(!s.toString().trim().isEmpty() ){
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if(!s.toString().trim().isEmpty() ){
                    input3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if(!s.toString().trim().isEmpty() ){
                    input4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if(!s.toString().trim().isEmpty() ){
                    input5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if(!s.toString().trim().isEmpty() ){
                    input6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
