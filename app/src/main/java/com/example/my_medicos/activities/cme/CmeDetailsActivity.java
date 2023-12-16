package com.example.my_medicos.activities.cme;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.my_medicos.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmeDetailsActivity extends AppCompatActivity {
    private ExoPlayer player;
    String videoURL;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    Toolbar toolbar;
    String field3;
    String field7;
    String email;
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    String receivedDataCme;
    Button reservecmebtn;
    Button reservedcmebtn;
    String current = user.getPhoneNumber();
    private boolean isReserved;
    LinearLayout playerlayout;
    String field4;
    String pdf = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme_details);

        Toolbar toolbar = findViewById(R.id.detailtoolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.arrowbackforappbar);
        }

        StyledPlayerView playerView = findViewById(R.id.player_view);
        player = new ExoPlayer.Builder(CmeDetailsActivity.this).build();
        playerView.setPlayer(player);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

       TextView presenter3 = findViewById(R.id.presenters3);

        final TextView textView = findViewById(R.id.description);
        final TextView moreButton = findViewById(R.id.moreButton);
        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time1);
        TextView Name = findViewById(R.id.speakername);
        TextView Speciality = findViewById(R.id.speciality);
        TextView Type = findViewById(R.id.type);
        reservecmebtn = findViewById(R.id.reservecmebtn);
        reservedcmebtn = findViewById(R.id.reservedcmebtn);
        Button liveendpost=findViewById(R.id.liveendpost);
        TextView addpresent=findViewById(R.id.addpresenter);
        addpresent.setVisibility(View.GONE);

        String field1 = getIntent().getExtras().getString("documentid");
        String name = getIntent().getExtras().getString("name");
        String field5 = getIntent().getExtras().getString("type");
        String field6=getIntent().getExtras().getString("time");
        Type.setText(field5);
        LinearLayout downloadPdfButton = findViewById(R.id.downloadPdfButton);
        TextView presenters2=findViewById(R.id.presenters1);

        LinearLayout reservebtn = findViewById(R.id.reservbtn);
        LinearLayout playerlayout = findViewById(R.id.playerlayout);
        boolean isCreator = current.equals(name);

        SharedPreferences sharedPref = getSharedPreferences("CmeDetails", Context.MODE_PRIVATE);

        isReserved = sharedPref.getBoolean("isReserved", false);
        reservecmebtn.setVisibility(isReserved ? View.GONE : View.VISIBLE);
        reservedcmebtn.setVisibility(isReserved ? View.VISIBLE : View.GONE);
        presenter3.setVisibility(View.GONE);

        checkReservationStatusFirestore(current, field1);

        LinearLayout livebtn = findViewById(R.id.livebtn);
        LinearLayout pastbtn = findViewById(R.id.pastbtn);
        ImageView defaultimageofcme = findViewById(R.id.defaultimage);
        LinearLayout textpartforlive = findViewById(R.id.textpartforlive);
        LinearLayout textpartforlivecreator = findViewById(R.id.textpartforlivecreator);
        Button attendcmebtn = findViewById(R.id.attendcmebtn);
        Button livecmebtn=findViewById(R.id.livecmebtn);
        liveendpost.setVisibility(View.GONE);
        Button Schedule=findViewById(R.id.schedulemeet);

        LinearLayout textpartforupcoming = findViewById(R.id.textpartforupcoming);
        RelativeLayout relativeboxnotforpast = findViewById(R.id.relativeboxnotforpast);
        Query query = db.collection("CME");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        field3 = ((String) dataMap.get("documentId"));
                        if (field3 != null) {
                            int r = field1.compareTo(field3);
                            if (r == 0) {
                                String speciality = ((String) dataMap.get("Speciality"));
                                String venue = ((String) dataMap.get("CME Venue"));
                                String date1 = ((String) dataMap.get("Selected Date"));
                                String time1 = ((String) dataMap.get("Selected Time"));
                                videoURL = document.getString("youtubelink");

                                List<String> presenters = (List<String>) dataMap.get("CME Presenter");
                                int count = 0;
                                String pres = null;
                                if (presenters != null && !presenters.isEmpty()) {
                                    for (String presenter : presenters) {
                                        if (pres == null) {
                                            pres = presenter + "   ";
                                        } else {
                                            pres = pres + presenter + "   ";
                                        }
                                        Log.d("Presenter", presenter);
                                        count++;
                                    }
                                }
                                presenter3.setText(pres);

                                presenters2.setText(presenters.get(0));
                                if (count > 1) {
                                    addpresent.setVisibility(View.VISIBLE);
                                    addpresent.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            presenter3.setVisibility(View.VISIBLE);
                                            presenters2.setVisibility(View.GONE);
                                            addpresent.setVisibility(View.GONE);

                                        }
                                    });
                                }

                                pdf = ((String) dataMap.get("Cme pdf"));
                                if (pdf == null) {
                                    downloadPdfButton.setVisibility(View.GONE);
                                } else {
                                    downloadPdfButton.setVisibility(View.VISIBLE);
                                    downloadPdfButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            downloadPdf(pdf);
                                        }
                                    });
                                }
                                StyledPlayerView playerView = findViewById(R.id.player_view);
                                player = new ExoPlayer.Builder(CmeDetailsActivity.this).build();
                                playerView.setPlayer(player);

                                MediaItem mediaItem = null;
                                if (videoURL != null) {
                                    mediaItem = MediaItem.fromUri(videoURL);
                                }

                                if (mediaItem != null) {
                                    player.setMediaItem(mediaItem);
                                    player.prepare();
                                    player.setPlayWhenReady(true);
                                }


                                String title = ((String) dataMap.get("CME Title"));
                                String virtuallink = ((String) dataMap.get("Virtual Link"));

                                setSupportActionBar(toolbar);
                                getSupportActionBar().setTitle(title);
                                textView.setText(venue);
                                date.setText(date1);
                                time.setText(time1);
                                Speciality.setText(speciality);
                            }
                        }
                    }
                    } else {
                        Toast.makeText(CmeDetailsActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        long eventStartTime=convertTimeToMillis(field6);

        if ("UPCOMING".equals(field5)) {
            reservebtn.setVisibility(View.VISIBLE);
            livebtn.setVisibility(View.GONE);
            liveendpost.setVisibility(View.GONE);
            textpartforlivecreator.setVisibility(View.GONE);
            textpartforlive.setVisibility(View.GONE);
            pastbtn.setVisibility(View.GONE);
            reservecmebtn.setVisibility(View.GONE);
            reservedcmebtn.setVisibility(View.GONE);
            if (isCreator){
                reservecmebtn.setVisibility(View.GONE);
                Schedule.setVisibility(View.VISIBLE);
            }
            else{
                reservecmebtn.setVisibility(View.VISIBLE);
                Schedule.setVisibility(View.GONE);

            }

            reservecmebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isReserved) {
                        Toast.makeText(CmeDetailsActivity.this, "You have already reserved a seat", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("isReserved", true);
                        editor.apply();

                        reservecmebtn.setVisibility(View.GONE);
                        reservedcmebtn.setVisibility(View.VISIBLE);
                        apply(field1);
                    }
                }
            });
            reservedcmebtn.setVisibility(View.GONE);
            playerlayout.setVisibility(View.GONE);
            defaultimageofcme.setVisibility(View.VISIBLE);
            textpartforlive.setVisibility(View.INVISIBLE);
            textpartforupcoming.setVisibility(View.VISIBLE);
            relativeboxnotforpast.setVisibility(View.VISIBLE);

        } else if ("LIVE".equals(field5)) {
            String currentTime = getCurrentTime();
            Log.d("CurrentTime", currentTime);

            long currenttimeformatted = convertTimeToMillis(currentTime);


            if (isCreator) {
                reservebtn.setVisibility(View.GONE);
                pastbtn.setVisibility(View.GONE);
                playerlayout.setVisibility(View.GONE);
                defaultimageofcme.setVisibility(View.VISIBLE);
                textpartforlive.setVisibility(View.GONE);
                textpartforlivecreator.setVisibility(View.VISIBLE);

                attendcmebtn.setVisibility(View.GONE);
                textpartforupcoming.setVisibility(View.INVISIBLE);
                relativeboxnotforpast.setVisibility(View.VISIBLE);
                Log.d("timenew", String.valueOf(eventStartTime));
                livebtn.setVisibility(View.VISIBLE);
                Log.d("CmeDetailsActivity", "Time difference: " + (currenttimeformatted - eventStartTime));

                if ((currenttimeformatted - eventStartTime) >= 10 * 60 * 1000) {

                    liveendpost.setVisibility(View.VISIBLE);
                    livecmebtn.setVisibility(View.GONE);
                    liveendpost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("CME").document(field1);
                            docRef.update("endtime", getCurrentTime(),
                                            "youtubelink",null)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            Toast.makeText(CmeDetailsActivity.this, "Successfully Ended", Toast.LENGTH_SHORT).show();
                                            liveendpost.setVisibility(View.GONE);
                                            Log.d("abc", "bcd");

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        }
                    });
                } else {
                    liveendpost.setVisibility(View.GONE);
                    livecmebtn.setVisibility(View.VISIBLE);
                }

            } else {
                // Hide the buttons for non-creators
                reservebtn.setVisibility(View.GONE);
                livebtn.setVisibility(View.VISIBLE);

                livecmebtn.setVisibility(View.GONE);
                pastbtn.setVisibility(View.GONE);
                playerlayout.setVisibility(View.GONE);
                defaultimageofcme.setVisibility(View.VISIBLE);
                textpartforlive.setVisibility(View.VISIBLE);
                textpartforlive.setVisibility(View.VISIBLE);
                attendcmebtn.setVisibility(View.VISIBLE);
                textpartforupcoming.setVisibility(View.INVISIBLE);
                relativeboxnotforpast.setVisibility(View.VISIBLE);
                liveendpost.setVisibility(View.GONE);

                attendcmebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Query query = db.collection("CME").whereEqualTo("User", current);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String virtualLink = document.getString("Virtual Link");
//                                        String virtualLink = "https://us04web.zoom.us/j/71840187333?pwd=BMFoNKz3xaQDpSq5yBxd4yL2MQq61a.1";

                                        try {
                                            Uri uri = Uri.parse(virtualLink);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e("VirtualLinkError", "Error opening virtual link", e);
                                        }

                                    }
                                } else {
                                    // Handle the error
                                    Toast.makeText(CmeDetailsActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        }
        else if ("PAST".equals(field5)) {
            // Show the livebtn and hide the reservebtn for Ongoing events
            reservebtn.setVisibility(View.GONE);
            livebtn.setVisibility(View.GONE);
            pastbtn.setVisibility(View.VISIBLE);
            playerlayout.setVisibility(View.VISIBLE);
            defaultimageofcme.setVisibility(View.GONE);
            textpartforlive.setVisibility(View.INVISIBLE);
            textpartforupcoming.setVisibility(View.GONE);
            relativeboxnotforpast.setVisibility(View.GONE);
        } else {
            // Handle other cases or set a default behavior if needed
            reservebtn.setVisibility(View.GONE);
            livebtn.setVisibility(View.GONE);
        }

        Query query1 = db.collection("users");
        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        field3 = ((String) dataMap.get("Phone Number"));
                        boolean areEqualIgnoreCase = name.equalsIgnoreCase(field3);
                        Log.d("vivek4", String.valueOf(areEqualIgnoreCase));
                        if ((name!=null)&&(field3!=null)) {
                            int r = name.compareTo(field3);
                            if (r == 0) {
                                field4 = ((String) dataMap.get("Name"));
                                Name.setText(field4);
                                break;
                            }
                        }
                    }
                } else {
                    // Handle the error
                    Toast.makeText(CmeDetailsActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    protected void onStop() {
        super.onStop();
        player.setPlayWhenReady(false);
        player.release();
        player = null;
    }
    private void checkReservationStatusFirestore(String userId, String eventId) {
        dc.collection("CMEsReserved")
                .whereEqualTo("User", userId)
                .whereEqualTo("documentidapply", eventId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // If a document is found, the seat is already reserved
                                isReserved = true;
                                reservecmebtn.setVisibility(View.GONE);
                                reservedcmebtn.setVisibility(View.VISIBLE);
                                break;
                            }
                        } else {
                            // Handle the error
                            Log.w(TAG, "Error checking reservation status", task.getException());
                        }
                    }
                });
    }

    private void showReservationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loaderofmeeting);
        // Customize the dialog or handle button clicks as needed
        dialog.show();
    }
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }

    public static void main(String[] args) {
        String currentTime = getCurrentTime();
        System.out.println("Current Time: " + currentTime);
    }
    private long convertTimeToMillis(String time) {
        if (time == null) {
            // Handle the case where time is null
            return 0; // or any default value
        }
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        Log.d("timenew",time);
        return hours * 60 * 60 * 1000 + minutes * 60 * 1000;
    }
    private boolean checkIfReserved(String userId, String eventId) {
        SharedPreferences preferences = getSharedPreferences("ReservedStatus", Context.MODE_PRIVATE);
        return preferences.getBoolean(userId + eventId, false);
    }
    public void downloadPdf(String pdfUrl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));

        // Title and description of the download notification
        request.setTitle("Downloading PDF");
        request.setDescription("Please wait...");

        // Set destination in the external public directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "your_pdf_filename.pdf");

        // Set notification visibility to show the download progress in the notification bar
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }

        // Optionally, you can show a toast or notification to indicate the download has started
        Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
    }


    // Method to save the reservation status
    private void saveReservationStatus(String userId, String eventId, boolean isReserved) {
        SharedPreferences preferences = getSharedPreferences("ReservedStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(userId + eventId, isReserved);
        editor.apply();
    }

    public void apply(String name) {
        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("User", current);
        usermap.put("documentidapply",name);
        usermap.put("isReserved",true);
        Log.d(receivedDataCme,"abcdef");
        dc.collection("CMEsReserved")
                .add(usermap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}