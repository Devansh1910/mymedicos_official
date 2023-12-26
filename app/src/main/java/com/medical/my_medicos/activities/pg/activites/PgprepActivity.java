package com.medical.my_medicos.activities.pg.activites;

import static androidx.media3.common.MediaLibraryInfo.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.fragments.ClubFragment;
import com.medical.my_medicos.activities.home.fragments.HomeFragment;
import com.medical.my_medicos.activities.home.fragments.SlideshowFragment;
import com.medical.my_medicos.activities.news.News;
import com.medical.my_medicos.activities.news.NewsAdapter;
import com.medical.my_medicos.activities.pg.activites.internalfragments.HomePgFragment;
import com.medical.my_medicos.activities.pg.activites.internalfragments.NeetExamFragment;
import com.medical.my_medicos.activities.pg.activites.internalfragments.PreparationPgFragment;
import com.medical.my_medicos.activities.pg.adapters.QuestionBankPGAdapter;
import com.medical.my_medicos.activities.pg.model.QuestionPG;
import com.medical.my_medicos.activities.publications.model.Category;
import com.medical.my_medicos.activities.slideshow.insider.SpecialitySlideshowInsiderActivity;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.activities.pg.activites.insiders.SpecialityPGInsiderActivity;
import com.medical.my_medicos.activities.pg.adapters.PerDayPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.SpecialitiesPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.VideoPGAdapter;
import com.medical.my_medicos.activities.pg.model.PerDayPG;
import com.medical.my_medicos.activities.pg.model.SpecialitiesPG;
import com.medical.my_medicos.activities.pg.model.VideoPG;
import com.medical.my_medicos.activities.publications.adapters.ProductAdapter;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.publications.utils.Constants;
import com.medical.my_medicos.adapter.cme.items.cmeitem4;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.databinding.ActivityPgprepBinding;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class  PgprepActivity extends AppCompatActivity {
    ActivityPgprepBinding binding;
    BottomNavigationView bottomNavigationPg;
    BottomAppBar bottomAppBarPg;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPgprepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomAppBarPg = findViewById(R.id.bottomappabarpginsider);

        replaceFragmentPg(new HomePgFragment());
        bottomNavigationPg = findViewById(R.id.bottomNavigationViewpginsider);
        bottomNavigationPg.setBackground(null);

        bottomNavigationPg.setOnItemSelectedListener(item -> {
            int frgId = item.getItemId();
            Log.d("Something went wrong..","Code Fucked");
            if (frgId == R.id.home) {
                replaceFragmentPg(new HomePgFragment());
            } else if (frgId == R.id.work) {
                replaceFragmentPg(new NeetExamFragment());
            } else {
                replaceFragmentPg(new PreparationPgFragment());
            }
            return true;
        });

    }

    private void replaceFragmentPg(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_pg, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d("BackStackCount", String.valueOf(fragmentManager.getBackStackEntryCount()));
    }

    @Override
    public void onBackPressed() {
        Log.d("BackPressed", "Back button pressed");
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(); // Pop the fragment from the back stack
            Log.d("BackStackCount", String.valueOf(fragmentManager.getBackStackEntryCount()));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}