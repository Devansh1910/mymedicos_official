package com.medical.my_medicos.activities.publications.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.publications.adapters.PurchasedAdapter;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.adapter.job.AdapterforApplicants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserContentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PurchasedAdapter productAdapter;
    List<Product> productList = new ArrayList<>();

    private static final String FIELD_PHONE_NUMBER = "Phone Number";
    private static final String FIELD_LIBRARY = "library";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_content);
        recyclerView = findViewById(R.id.purchased);

        ImageView backToHomeImageView = findViewById(R.id.backtothepublicationactivity);
        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        MaterialSearchBar searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for now
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for now
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for now
            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // Not needed for now
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                String query = text.toString();
                if (!TextUtils.isEmpty(query)) {
                    Intent intent = new Intent(UserContentActivity.this, SearchPublicationActivity.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    // Handle back button click
                }
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String phoneNumber = currentUser.getPhoneNumber();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        usersCollection.whereEqualTo(FIELD_PHONE_NUMBER, phoneNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot userSnapshot : task.getResult()) {
                            List<Object> libraryArray = (List<Object>) userSnapshot.get(FIELD_LIBRARY);

                            if (libraryArray != null) {
                                List<String> docIds = new ArrayList<>();
                                for (Object item : libraryArray) {
                                    docIds.add(item.toString());
                                }

                                List<Product> productList = new ArrayList<Product>();

                                for (String docId : docIds) {
                                    Log.d("UserContentActivity", "Document ID: " + docId);
                                    CollectionReference publicationsCollection = db.collection("Publications");
                                    DocumentReference publicationDocRef = publicationsCollection.document(docId);

                                    publicationDocRef.get().addOnCompleteListener(publicationTask -> {
                                        if (publicationTask.isSuccessful()) {
                                            DocumentSnapshot document = publicationTask.getResult();

                                            Map<String, Object> data = document.getData();
                                            if (data != null) {
                                                String title = (String) data.get("Title");
                                                String thumbnail = (String) data.get("thumbnail");
                                                String author = (String) data.get("Author");
                                                Double price = document.getLong("Price").doubleValue();
                                                String type = (String) data.get("Type");
                                                String category = (String) data.get("Category");
                                                String subject = (String) data.get("Subject");
                                                String id = document.getId();

                                                Product product = new Product(title, thumbnail, author, price, type, category, id, subject);
                                                productList.add(product);
                                            }

                                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL, false));
                                            recyclerView.setAdapter(new PurchasedAdapter(getApplication(), productList));
                                        } else {
                                            // Handle errors in fetching publication document
                                            Log.e("UserContentActivity", "Error fetching publication document", publicationTask.getException());
                                        }
                                    });
                                }
                            } else {
                                // Handle the case where the libraryMap is null or not present
                                Log.d("UserContentActivity", "Library Array is null or not present");
                            }
                        }
                    } else {
                        // Handle errors in fetching user documents
                        Log.e("UserContentActivity", "Error fetching user documents", task.getException());
                    }
                });
    }
}
