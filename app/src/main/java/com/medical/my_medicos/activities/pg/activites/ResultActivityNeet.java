//package com.medical.my_medicos.activities.pg.activites;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.medical.my_medicos.R;
//import com.medical.my_medicos.activities.pg.adapters.ResultReportAdapter;
//import com.medical.my_medicos.activities.pg.model.QuizPGinsider;
//
//import java.util.ArrayList;
//
//public class ResultActivityNeet {
//}
package com.medical.my_medicos.activities.pg.activites;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.medical.my_medicos.R;
        import com.medical.my_medicos.activities.pg.adapters.ResultReportAdapter;
        import com.medical.my_medicos.activities.pg.adapters.ResultReportNeetAdapter;
        import com.medical.my_medicos.activities.pg.model.Neetpg;
        import com.medical.my_medicos.activities.pg.model.QuizPGinsider;

        import java.util.ArrayList;

public class ResultActivityNeet extends AppCompatActivity {

    private RecyclerView resultRecyclerView;
    private ResultReportNeetAdapter resultAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultRecyclerView = findViewById(R.id.resultRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        ArrayList<Neetpg> questions = (ArrayList<Neetpg>) intent.getSerializableExtra("questions");

        resultAdapter = new  ResultReportNeetAdapter(this, questions);
        resultRecyclerView.setAdapter(resultAdapter);
    }
}

