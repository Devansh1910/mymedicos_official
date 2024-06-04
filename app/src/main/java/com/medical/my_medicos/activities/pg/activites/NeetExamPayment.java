package com.medical.my_medicos.activities.pg.activites;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.login.GetstartedActivity;
import com.medical.my_medicos.activities.login.bottom_controls.PrivacyPolicyActivity;
import com.medical.my_medicos.activities.login.bottom_controls.TermsandConditionsActivity;
import com.medical.my_medicos.activities.pg.activites.Neetexaminsider;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.prefs.Preferences;

import de.hdodenhof.circleimageview.CircleImageView;

public class NeetExamPayment extends AppCompatActivity {

    private DatabaseReference database;
    TextView user_name_dr,user_email_dr;
    CircleImageView profilepicture;
    TextView currentcoinspg;
    ImageView sharebtnforneetexam;
    String examtitle,examdescription;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current_user=user.getPhoneNumber();
    String receivedData;
    private String currentUid;
    private int examFee = 50;
    private boolean dataLoaded = false;
    private int pendingDiscount = 0; // Default is no discount
    private String couponId; // To store the ID of the applied coupon
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neet_exam_payment);

        String examId = getIntent().getStringExtra("qid");
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String title1 = intent.getStringExtra("Title1");
        String Due = intent.getStringExtra("Due");

        if (examId != null && !examId.isEmpty()) {
            // This is when the activity is opened from ExamQuizAdapter
            Log.d(TAG, "Opened from ExamQuizAdapter");


            // Set the title and due date
            TextView quizNameTextView = findViewById(R.id.quizNameTextView);
            TextView dueDateTextView = findViewById(R.id.DueDate);
            quizNameTextView.setText(title);
            dueDateTextView.setText(Due);
            // Proceed with your normal logic here
        } else {
            // This is when the activity is opened from a dynamic link
            Log.d(TAG, "Opened from dynamic link");
            // Extract other required data from the intent and proceed
//            Intent intent = getIntent();
//            String title = intent.getStringExtra("Title");
//            String Due = intent.getStringExtra("Due");
            // Set the title and due date
            TextView quizNameTextView = findViewById(R.id.quizNameTextView);
            TextView dueDateTextView = findViewById(R.id.DueDate);
            quizNameTextView.setText(title);
            dueDateTextView.setText(Due);
            // Set other data as needed
        }

        // Set the exam ID
        TextView qidTextView = findViewById(R.id.quizid);
        qidTextView.setText(examId);

        FirebaseUser current =FirebaseAuth.getInstance().getCurrentUser();
        currentUid =current.getPhoneNumber();

        database = FirebaseDatabase.getInstance().getReference();
        user_name_dr = findViewById(R.id.currentusernamewillcomehere);
        profilepicture = findViewById(R.id.profilepicture);
        user_email_dr = findViewById(R.id.currentuseremailid);

        if (!dataLoaded) {
            fetchdata();
            fetchUserData();
        }

        ImageView backToHomeImageView = findViewById(R.id.backtothehomefrompg);
        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView textView1 = findViewById(R.id.help_support_coupon);
        String htmlText1 = "Having trouble? <strong><u>Reach out</u></strong>";
        textView1.setText(Html.fromHtml(htmlText1, Html.FROM_HTML_MODE_LEGACY));

        TextView help_support = findViewById(R.id.help_support_coupon);
        help_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        TextView qidcomehereText = findViewById(R.id.quizid);
        qidcomehereText.setText(examId);

        currentcoinspg = findViewById(R.id.currentcoinspg);

        CheckBox agreeCheckbox = findViewById(R.id.agreeCheckbox);
        LinearLayout startExamLayout = findViewById(R.id.startexamination);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUid = currentUser.getPhoneNumber();
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            database.getReference().child("profiles")
                    .child(currentUid)
                    .child("coins")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer coinsValue = snapshot.getValue(Integer.class);
                            if (coinsValue != null) {
                                currentcoinspg.setText(String.valueOf(coinsValue));
                            } else {
                                currentcoinspg.setText("0");
                            }
                        }

                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Error loading coins from database: " + error.getMessage());
                        }
                    });
        }

        startExamLayout.setEnabled(false); // Initially disable the layout

        // Set an OnClickListener on the CheckBox
        agreeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the CheckBox is checked
                if (agreeCheckbox.isChecked()) {
                    startExamLayout.setEnabled(true); // Enable the layout
                } else {
                    Toast.makeText(NeetExamPayment.this, "Please agree to the terms to start the examination", Toast.LENGTH_SHORT).show();
                    startExamLayout.setEnabled(false); // Disable the layout
                }
            }
        });

        // Set OnClickListener on start examination layout
        startExamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(title, title1);
            }
        });

        sharebtnforneetexam = findViewById(R.id.sharebtnforneetexam);

        sharebtnforneetexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensure documentid is initialized
                if (examId == null || examId.isEmpty()) {
                    Toast.makeText(NeetExamPayment.this, "Document ID is not available.", Toast.LENGTH_SHORT).show();
                    return;
                }
                createlink(current_user, examId, examtitle, examdescription); // Pass examId here
            }
        });


        TextView coupon_submit = findViewById(R.id.coupon_submit);
        EditText coupon_apply=findViewById(R.id.coupon_apply);
        coupon_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCouponCode = coupon_apply.getText().toString();
                validateAndApplyCoupon(enteredCouponCode);
            }
        });

        handleDeepLink();
        configureWindow();

    }
    public void createlink(String custid, String examId, String examtitle, String examdescription) {
        Log.e("main", "create link");
        Log.d("createlink", "custid: " + custid + ", examId: " + examId);

        if (examId == null || examId.isEmpty()) {
            Log.e("createlink", "Exam ID is null or empty");
            return;
        }

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.mymedicos.in/examdetails?custid=" + custid + "&examId=" + examId + "&title=" + examtitle + "&due=" + examdescription))
                .setDomainUriPrefix("https://app.mymedicos.in")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main", "Long refer " + dynamicLinkUri);

        createreferlink(custid, examId);
    }
    public void createreferlink(String custid, String examId) {
        if (examId == null || examId.isEmpty()) {
            Log.e(ContentValues.TAG, "Exam ID is null or empty");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("PGupload")
                .document("Weekley")
                .collection("Quiz")
                .document(examId);

        Log.d("createreferlink", "Fetching document for examId: " + examId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                String quizTitle = documentSnapshot.getString("title");
                Timestamp startTimeStamp = (Timestamp) documentSnapshot.get("from");
                Timestamp endTimeStamp = (Timestamp) documentSnapshot.get("to");
                long timeUntilOpen = startTimeStamp.getSeconds() - System.currentTimeMillis() / 1000;

                String encodedExamTitle = encode(examtitle);
                String encodedExamDescription = encode(examdescription);

                String shareLinkText = "Checkout this Exam" + quizTitle + " \uD83E\uDE7A" + " at " +
                        timeUntilOpen + " in " +
                        "https://app.mymedicos.in/?" +
                        "link=http://www.mymedicos.in/examdetails?examId=" + examId +
                        "&st=" + encodedExamTitle +
                        "&sd=" + encodedExamDescription +
                        "&apn=" + getPackageName() +
                        "&si=" + "https://res.cloudinary.com/dlgrxj8fp/image/upload/v1709416117/mwkegbnreoldjn4lksnn.png";
                Log.e("Exam Detailed Activity", "Sharelink - " + shareLinkText);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, shareLinkText);
                intent.setType("text/plain");
                startActivity(intent);
            } else {
                Log.e(ContentValues.TAG, "No such document with documentId: " + examId);
            }
        }).addOnFailureListener(e -> {
            Log.e(ContentValues.TAG, "Error fetching exam details for documentId: " + examId, e);
        });
    }
    private String encode(String s) {
        if (s == null) {
            return "";
        }

        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    //....
    private void handleDeepLink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink != null) {
                            String examId = deepLink.getQueryParameter("examId");
                            String title = deepLink.getQueryParameter("title");
                            String due = deepLink.getQueryParameter("due");

                            Intent intent = new Intent(NeetExamPayment.this, NeetExamPayment.class);
                            intent.putExtra("Title", title);
                            intent.putExtra("Due", due);
                            intent.putExtra("qid", examId);
                            startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(this, e -> Log.w("DeepLink", "getDynamicLink:onFailure", e));
    }
    private void configureWindow() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NeetExamPayment.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_login_layout, null);
        bottomSheetView.findViewById(R.id.check_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.check_privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PrivacyPolicyActivity.class);
                v.getContext().startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.check_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TermsandConditionsActivity.class);
                v.getContext().startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@mymedicos.in"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Facing issue in {Problem here}");

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    private void fetchdata() {
        Preferences preferences = Preferences.userRoot();
        if (preferences.get("username", null) != null) {
            System.out.println("Key '" + "username" + "' exists in preferences.");
            String username = preferences.get("username", null);
            Log.d("usernaem", username);
        }
        String username = preferences.get("username", "");
        String email = preferences.get("email", "");
        String location = preferences.get("location", "");
        String interest = preferences.get("interest", "");
        String phone = preferences.get("userphone", "");
        String prefix = preferences.get("prefix", "");
        user_name_dr.setText(username);
        user_email_dr.setText(email);
    }
    private void fetchUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    String docID = document.getId();
                                    Map<String, Object> dataMap = document.getData();
                                    String field1 = (String) dataMap.get("Phone Number");

                                    if (field1 != null && currentUser.getPhoneNumber() != null) {
                                        int a = field1.compareTo(currentUser.getPhoneNumber());
                                        if (a == 0) {
                                            String userName = (String) dataMap.get("Name");
                                            String userEmail = (String) dataMap.get("Email ID");
                                            String userLocation = (String) dataMap.get("Location");
                                            String userInterest = (String) dataMap.get("Interest");
                                            String userPhone = (String) dataMap.get("Phone Number");
                                            String userPrefix = (String) dataMap.get("Prefix");
                                            String userAuthorized = (String) dataMap.get("authorized");
                                            Boolean mcnVerified = (Boolean) dataMap.get("MCN verified");
                                            Preferences preferences = Preferences.userRoot();
                                            preferences.put("username", userName);
                                            preferences.put("email", userEmail);
                                            preferences.put("location", userLocation);
                                            preferences.put("interest", userInterest);
                                            preferences.put("userphone", userPhone);
                                            preferences.put("docId", docID);
                                            preferences.put("prefix", userPrefix);
                                            user_name_dr.setText(userName);

                                            fetchdata();
                                            fetchUserProfileImage(userId);
                                        }
                                    } else {
                                        Log.e(TAG, "Field1 or currentUser.getPhoneNumber() is null");
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
    @SuppressLint("RestrictedApi")
    private void fetchUserProfileImage(String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("users").child(userId).child("profile_image.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(profilepicture);
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Error fetching profile image: " + exception.getMessage());
        });
    }
    @SuppressLint("RestrictedApi")
    public void validateAndApplyCoupon(String code) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getPhoneNumber() != null) {
            String currentPhoneNumber = currentUser.getPhoneNumber();

            // First check if the coupon has been used by this user
            DocumentReference userCouponDoc = db.collection("CouponUsed").document(currentPhoneNumber);
            userCouponDoc.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    List<String> usedCoupons = (List<String>) documentSnapshot.get("coupon_used");
                    if (usedCoupons != null && usedCoupons.contains(code)) {
                        // Coupon has already been used by this user
                        Toast.makeText(NeetExamPayment.this, "You have already used this coupon.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Proceed to validate the coupon if not used
                        checkCouponValidity(code, db);
                    }
                } else {
                    // No coupons used yet, proceed to validate new coupon
                    checkCouponValidity(code, db);
                }
            }).addOnFailureListener(e -> Log.e(TAG, "Error checking used coupons", e));
        } else {
            Toast.makeText(this, "User not logged in or phone number unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCouponValidity(String code, FirebaseFirestore db) {
        db.collection("Coupons")
                .whereEqualTo("code", code)
                .whereEqualTo("status", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                couponId = document.getId(); // Store the coupon ID when it's validated
                                pendingDiscount = Objects.requireNonNull(document.getLong("discount")).intValue();
                                Log.d(TAG, "Coupon ID: " + couponId); // Check coupon ID
                                Toast.makeText(NeetExamPayment.this, "Coupon validated, discount will be applied at exam start", Toast.LENGTH_SHORT).show();
                                addCouponToUsedList(currentUser.getPhoneNumber(), code);
                            }
                        } else {
                            Toast.makeText(NeetExamPayment.this, "Not a valid coupon code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    private void addCouponToUsedList(String phoneNumber, String couponId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userCouponDoc = db.collection("CouponUsed").document(phoneNumber);
        userCouponDoc.update("coupon_used", FieldValue.arrayUnion(couponId))
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Coupon added to user's used list"))
                .addOnFailureListener(e -> Log.e(TAG, "Error adding coupon to used list", e));
    }

    private void applyCoupon(int discount, String couponId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("profiles").child(currentUid).child("coins");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer coinsValue = snapshot.getValue(Integer.class);
                if (coinsValue != null && coinsValue >= discount) {
                    int newCoinsValue = coinsValue - Math.max(discount, examFee);
                    ref.setValue(newCoinsValue);
                    Toast.makeText(NeetExamPayment.this, "Coupon applied successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NeetExamPayment.this, "Insufficient Coins", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NeetExamPayment.this, "Error fetching coins", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void startExamination(String title, String title1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("profiles").child(currentUid).child("coins");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer coinsValue = snapshot.getValue(Integer.class);
                if (coinsValue != null) {
                    int coinsAfterDiscount = Math.max(0, examFee - pendingDiscount);
                    int newCoinsValue = coinsValue - coinsAfterDiscount;

                    if (newCoinsValue >= 0) {
                        ref.setValue(newCoinsValue);

                        // Check if couponId is not null and discount is applied
                        if (couponId != null && pendingDiscount > 0) {
                            addCouponUsedToUser(currentUid);
                        }

                        showQuizInsiderActivity(title, title1);
                        Toast.makeText(NeetExamPayment.this, "Welcome to the exam. Fee applied: " + coinsAfterDiscount + " coins", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NeetExamPayment.this, "Insufficient Credits", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NeetExamPayment.this, "Error fetching coins", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCouponUsedToUser(String docID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("CouponUsed").document(docID);

        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userDoc.update("coupon_used", FieldValue.arrayUnion(couponId))
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Coupon ID added to user's used coupons."))
                                .addOnFailureListener(e -> Log.e(TAG, "Error adding coupon ID to user's used coupons.", e));
                    } else {
                        // Handle case where document does not exist
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("coupon_used", Arrays.asList(couponId));
                        db.collection("CouponUsed").document(docID).set(newUser)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "New user document created with coupon usage."))
                                .addOnFailureListener(e -> Log.e(TAG, "Failed to create new user document.", e));
                    }
                } else {
                    Log.e(TAG, "Failed to get document: ", task.getException());
                }
            }
        });
    }



    private void showConfirmationDialog(String title, String title1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Starting this quiz will deduct 50 med coins from your account. Do you want to proceed?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startExamination(title, title1);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    // Method to show the quiz instructions activity
    private void showQuizInsiderActivity(String title, String title1) {
        Intent intent = new Intent(NeetExamPayment.this, Neetexaminsider.class);
        intent.putExtra("Title1", title1);
        intent.putExtra("Title", title);

        startActivity(intent);
    }
}
