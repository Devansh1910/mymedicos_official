package com.medical.my_medicos.activities.pg.activites.extras;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.publications.activity.PaymentPublicationActivity;
import com.medical.my_medicos.databinding.ActivityCreditsBinding;

import java.util.Map;
import java.util.Objects;

public class CreditsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String LAST_CLICK_TIME_VIDEO_1 = "lastClickTimeVideo1";
    private static final String LAST_CLICK_TIME_VIDEO_2 = "lastClickTimeVideo2";
    private static final long COOLDOWN_PERIOD = 00 * 00 * 60 * 1000; // 24 hours in milliseconds
    ActivityCreditsBinding binding;
    private RewardedAd mRewardedAd;
    AlertDialog alertDialog;
    FirebaseDatabase database;
    String currentUid;
    private Context context;
    int coins= 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreditsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                showBottomSheet100();
            }
        });
        binding.puchase250credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet250();
            }
        });
        binding.puchase500credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet500();
            }
        });
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
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
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
    private void showBottomSheet100() {
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_up_for_payment, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        LinearLayout textClickMe = bottomSheetView.findViewById(R.id.paymentpartcredit);

        textClickMe.setOnClickListener(v -> {
            if (mRewardedAd != null) {
                Activity activityContext = CreditsActivity.this;
                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        loadAd();
                        coins = coins + 100;
                        database.getReference().child("profiles")
                                .child(currentUid)
                                .child("coins")
                                .setValue(coins);
                        binding.vide1oicon.setImageResource(R.drawable.baseline_check_24);
                        handleRewardedAdCompletion();
                        Toast.makeText(CreditsActivity.this, "Yahhoo!, 100 MedCoins Credited", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e("Something went wrong","Error");
            }
        });
        bottomSheetDialog.show();
    }

    private void showBottomSheet250() {
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_up_250, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        LinearLayout textClickMe = bottomSheetView.findViewById(R.id.paymentpartcredit250);

        textClickMe.setOnClickListener(v -> {
            if (mRewardedAd != null) {
                Activity activityContext = CreditsActivity.this;
                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        loadAd();
                        coins = coins + 250;
                        database.getReference().child("profiles")
                                .child(currentUid)
                                .child("coins")
                                .setValue(coins);
                        binding.vide1oicon.setImageResource(R.drawable.baseline_check_24);
                        handleRewardedAdCompletion();
                        Toast.makeText(CreditsActivity.this, "Yahhoo!, 100 MedCoins Credited", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e("Something went wrong","Error");
            }
        });
        bottomSheetDialog.show();
    }

    private void showBottomSheet500() {
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_up_500, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        LinearLayout textClickMe = bottomSheetView.findViewById(R.id.paymentpartcredit500);

        textClickMe.setOnClickListener(v -> {
            if (mRewardedAd != null) {
                Activity activityContext = CreditsActivity.this;
                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        loadAd();
                        coins = coins + 100;
                        database.getReference().child("profiles")
                                .child(currentUid)
                                .child("coins")
                                .setValue(coins);
                        binding.vide1oicon.setImageResource(R.drawable.baseline_check_24);
                        handleRewardedAdCompletion();
                        Toast.makeText(CreditsActivity.this, "Yahhoo!, 100 MedCoins Credited", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e("Something went wrong","Error");
            }
        });
        bottomSheetDialog.show();
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
                showBottomSheet250();
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        alertDialog = dialog;
    }


}