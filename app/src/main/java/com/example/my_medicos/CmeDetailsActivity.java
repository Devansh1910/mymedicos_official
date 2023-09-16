package com.example.my_medicos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CmeDetailsActivity extends AppCompatActivity {


    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_details);

        toolbar = findViewById(R.id.detailtoolbar);

        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView textView = findViewById(R.id.textView);
        final TextView moreButton = findViewById(R.id.moreButton);

        moreButton.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    textView.setMaxLines(3);
                    moreButton.setText("More");
                } else {
                    textView.setMaxLines(Integer.MAX_VALUE); // Expand to show all lines
                    moreButton.setText("Less");
                }
                isExpanded = !isExpanded;
            }
        });
    }
}