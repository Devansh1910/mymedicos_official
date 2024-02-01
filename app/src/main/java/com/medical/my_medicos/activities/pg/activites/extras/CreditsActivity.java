package com.medical.my_medicos.activities.pg.activites.extras;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.home.sidedrawer.HomeSideActivity;
import com.medical.my_medicos.activities.news.NewsActivity;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.publications.activity.CartPublicationActivity;
import com.medical.my_medicos.activities.publications.activity.PaymentPublicationActivity;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.databinding.ActivityCreditsBinding;
import org.json.JSONObject;
import java.util.Map;
import java.util.Objects;
import java.util.prefs.Preferences;

public class CreditsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String LAST_CLICK_TIME_VIDEO_1 = "lastClickTimeVideo1";
    private static final String LAST_CLICK_TIME_VIDEO_2 = "lastClickTimeVideo2";
    private static final long COOLDOWN_PERIOD = 24 * 60 * 60 * 1000;
    ActivityCreditsBinding binding;
    private RewardedAd mRewardedAd;
    AlertDialog alertDialog;
    FirebaseDatabase database;
    String currentUid;
    private Context context;
    int coins= 300;
    private ImageView backtothehomesideactivity;
    private int previousCoinCount = 0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreditsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        backtothehomesideactivity = findViewById(R.id.backtothehomesideactivity);
        backtothehomesideactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreditsActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        context = CreditsActivity.this;
        database = FirebaseDatabase.getInstance();
        currentUid = FirebaseAuth.getInstance().getUid();
        loadAd();

        database.getReference().child("profiles")
                .child(currentUid)
                .child("coins")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Integer coinsValue = snapshot.getValue(Integer.class);
                        if (coinsValue != null) {
                            coins = coinsValue;
                            binding.currentcoins.setText(String.valueOf(coins));
                        } else {
                            coins = 0;
                            binding.currentcoins.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        binding.video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canClickVideo("video1")) {
                    showRewardedAd("video1");
                } else {
                    showDialog("Free Redeem Coins will be available after 24 hrs");
                }
            }
        });

        binding.video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canClickVideo("video2")) {
                    showRewardedAd("video2");
                } else {
                    showDialog("Free Redeem Coins will be available after 24 hrs");
                }
            }
        });
        binding.puchase100credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet99();
            }
        });
        binding.puchase250credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet129();
            }
        });
        binding.puchase500credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet199();
            }
        });
        TextView currentCoinsTextView = findViewById(R.id.currentcoins);
        int currentCoins = Integer.parseInt(currentCoinsTextView.getText().toString());

        int increase = currentCoins - previousCoinCount;
        if (increase == 150) {
            // Display the custom popup
            showCustomPopup();
        }

        previousCoinCount = currentCoins;

    }

    private void showRewardedAd(String videoName) {
        if (mRewardedAd != null) {
            Activity activityContext = CreditsActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    loadAd();
                    coins += (videoName.equals("video1") ? 10 : 20);
                    updateCoinsInDatabase(coins);
                    handleRewardedAdCompletion();
                    showDialog("MedCoins Credited: " + (videoName.equals("video1") ? 10 : 20));
                    updateLastClickTime(videoName);
                }
            });
        } else {
            Log.e("Something went wrong", "Error");
        }
    }
    private void handleRewardedAdCompletion() {
        loadAd();
        int updatedCoins = Integer.parseInt(binding.currentcoins.getText().toString());
        Log.d(TAG, "Updated Coins Value: " + updatedCoins);
    }

    private boolean canClickVideo(String videoName) {
        long lastClickTime = getLastClickTime(videoName);
        long currentTime = System.currentTimeMillis();

        // Check if enough time has passed since the last click
        return currentTime - lastClickTime >= COOLDOWN_PERIOD;
    }

    private long getLastClickTime(String videoName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if ("video1".equals(videoName)) {
            return prefs.getLong(LAST_CLICK_TIME_VIDEO_1, 0);
        } else if ("video2".equals(videoName)) {
            return prefs.getLong(LAST_CLICK_TIME_VIDEO_2, 0);
        }
        return 0;
    }
    void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-1452770494559845~3220735688",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });
    }

    private void updateLastClickTime(String videoName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        long currentTime = System.currentTimeMillis();

        if ("video1".equals(videoName)) {
            editor.putLong(LAST_CLICK_TIME_VIDEO_1, currentTime);
        } else if ("video2".equals(videoName)) {
            editor.putLong(LAST_CLICK_TIME_VIDEO_2, currentTime);
        }

        editor.apply();
    }

    private void updateCoinsInDatabase(int updatedCoins) {
        database.getReference().child("profiles")
                .child(currentUid)
                .child("coins")
                .setValue(updatedCoins);
    }
    private void showBottomSheet99() {
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_up_for_payment, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button textClickMe = bottomSheetView.findViewById(R.id.paymentpartcredit);

        textClickMe.setOnClickListener(v -> {
            processCreditsOrderPackage1();
        });
        bottomSheetDialog.show();
    }

    private void showBottomSheet129() {
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_up_250, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button textClickMe = bottomSheetView.findViewById(R.id.paymentpartcredit129);

        textClickMe.setOnClickListener(v -> {
            processCreditsOrderPackage2();
        });
        bottomSheetDialog.show();
    }

    private void showBottomSheet199() {
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_up_500, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button textClickMe = bottomSheetView.findViewById(R.id.paymentpartcredit199);

        textClickMe.setOnClickListener(v -> {
            processCreditsOrderPackage3();
        });
        bottomSheetDialog.show();
    }

    void processCreditsOrderPackage1() {
        progressDialog.show();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersRef = db.collection("users");

            usersRef.whereEqualTo("realtimeid", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                RequestQueue queue = Volley.newRequestQueue(this);

                                String url = ConstantsDashboard.GET_ORDER_ID_99_41 + userId + "/" + "package1";
                                Log.d("API Request URL", url);

                                StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                                    try {
                                        Log.d("API Response", response); // Log the API response

                                        JSONObject requestBody = new JSONObject(response);
                                        if (requestBody.getString("status").equals("success")) {
                                            // Your existing logic for processing the order
                                            Toast.makeText(CreditsActivity.this, "Success order.", Toast.LENGTH_SHORT).show();
                                            String orderNumber = requestBody.getString("order_id");
                                            Log.e("Order ID check", orderNumber);
                                            new AlertDialog.Builder(CreditsActivity.this)
                                                    .setTitle("Order Successful")
                                                    .setCancelable(false)
                                                    .setMessage("Your order number is: " + orderNumber)
                                                    .setPositiveButton("Pay Now", (dialogInterface, i) -> {
                                                        Intent intent = new Intent(CreditsActivity.this, PaymentPublicationActivity.class);
                                                        intent.putExtra("orderCode", orderNumber);
                                                        startActivity(intent);
                                                    }).show();
                                        } else {
                                            // Your existing logic for handling a failed order
                                            Toast.makeText(CreditsActivity.this, "Failed order.", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                        Log.e("res", response);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }, error -> {
                                    // Handle Volley error
                                    error.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(CreditsActivity.this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                                queue.add(request);
                            }
                        } else {
                            // Handle the error when the document is not found
                            progressDialog.dismiss();
                            Toast.makeText(CreditsActivity.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(CreditsActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


    void processCreditsOrderPackage2() {
        progressDialog.show();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersRef = db.collection("users");

            usersRef.whereEqualTo("realtimeid", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                RequestQueue queue = Volley.newRequestQueue(this);

                                String url = ConstantsDashboard.GET_ORDER_ID_99_41 + userId + "/" + "package2";
                                Log.d("API Request URL", url);

                                StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                                    try {
                                        Log.d("API Response", response); // Log the API response

                                        JSONObject requestBody = new JSONObject(response);
                                        if (requestBody.getString("status").equals("success")) {
                                            // Your existing logic for processing the order
                                            Toast.makeText(CreditsActivity.this, "Success order.", Toast.LENGTH_SHORT).show();
                                            String orderNumber = requestBody.getString("order_id");
                                            Log.e("Order ID check", orderNumber);
                                            new AlertDialog.Builder(CreditsActivity.this)
                                                    .setTitle("Order Successful")
                                                    .setCancelable(false)
                                                    .setMessage("Your order number is: " + orderNumber)
                                                    .setPositiveButton("Pay Now", (dialogInterface, i) -> {
                                                        Intent intent = new Intent(CreditsActivity.this, PaymentPublicationActivity.class);
                                                        intent.putExtra("orderCode", orderNumber);
                                                        startActivity(intent);
                                                    }).show();
                                        } else {
                                            // Your existing logic for handling a failed order
                                            Toast.makeText(CreditsActivity.this, "Failed order.", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                        Log.e("res", response);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }, error -> {
                                    // Handle Volley error
                                    error.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(CreditsActivity.this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                                queue.add(request);
                            }
                        } else {
                            // Handle the error when the document is not found
                            progressDialog.dismiss();
                            Toast.makeText(CreditsActivity.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(CreditsActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    void processCreditsOrderPackage3() {
        progressDialog.show();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersRef = db.collection("users");

            usersRef.whereEqualTo("realtimeid", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                RequestQueue queue = Volley.newRequestQueue(this);

                                String url = ConstantsDashboard.GET_ORDER_ID_99_41 + userId + "/" + "package3";
                                Log.d("API Request URL", url);

                                StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
                                    try {
                                        Log.d("API Response", response); // Log the API response

                                        JSONObject requestBody = new JSONObject(response);
                                        if (requestBody.getString("status").equals("success")) {
                                            // Your existing logic for processing the order
                                            Toast.makeText(CreditsActivity.this, "Success order.", Toast.LENGTH_SHORT).show();
                                            String orderNumber = requestBody.getString("order_id");
                                            Log.e("Order ID check", orderNumber);
                                            new AlertDialog.Builder(CreditsActivity.this)
                                                    .setTitle("Order Successful")
                                                    .setCancelable(false)
                                                    .setMessage("Your order number is: " + orderNumber)
                                                    .setPositiveButton("Pay Now", (dialogInterface, i) -> {
                                                        Intent intent = new Intent(CreditsActivity.this, PaymentPublicationActivity.class);
                                                        intent.putExtra("orderCode", orderNumber);
                                                        startActivity(intent);
                                                    }).show();
                                        } else {
                                            // Your existing logic for handling a failed order
                                            Toast.makeText(CreditsActivity.this, "Failed order.", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                        Log.e("res", response);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }, error -> {
                                    // Handle Volley error
                                    error.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(CreditsActivity.this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                                queue.add(request);
                            }
                        } else {
                            // Handle the error when the document is not found
                            progressDialog.dismiss();
                            Toast.makeText(CreditsActivity.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(CreditsActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCustomPopup() {
        final Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.custom_popup);
        TextView closeButton = customDialog.findViewById(R.id.closebtnforthecreditmessage99);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        customDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                customDialog.dismiss();
            }
        }, 3000); // The popup will be dismissed after 3000 milliseconds (3 seconds)
    }


    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreditsActivity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_out_ofmoves_dialogue, null);
        builder.setView(dialogView);

        TextView dialogMessage = dialogView.findViewById(R.id.messageforout);
        dialogMessage.setText(message);

        LottieAnimationView lottieAnimationView = dialogView.findViewById(R.id.correctanswer);
        lottieAnimationView.setAnimation(R.raw.asorryforcredits);
        lottieAnimationView.playAnimation();

        Button okButton = dialogView.findViewById(R.id.okbtnreplacer);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        CardView packageButton = dialogView.findViewById(R.id.tothepackage);
        packageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                showBottomSheet129();
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        alertDialog = dialog;
    }


}