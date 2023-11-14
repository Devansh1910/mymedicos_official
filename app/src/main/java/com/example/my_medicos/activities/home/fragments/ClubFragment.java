package com.example.my_medicos.activities.home.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import androidx.fragment.app.Fragment;
import com.example.my_medicos.R;

public class ClubFragment extends Fragment {
    private VideoView videoView;
    private int currentVideoIndex = 0;
    private int[] videoResources = {R.raw.underconstruction}; // Replace with your video resources

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_club, container, false);

        // Initialize VideoView
        videoView = rootView.findViewById(R.id.underconstructionid);

        // Start playing the first video
        playVideo(currentVideoIndex);

        // Set a completion listener to play the next video when the current one finishes
        videoView.setOnCompletionListener(mp -> {
            // Increment index and play the next video in the loop
            currentVideoIndex = (currentVideoIndex + 1) % videoResources.length;
            playVideo(currentVideoIndex);
        });

        return rootView;
    }

    private void playVideo(int index) {
        // Set the video path
        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + videoResources[index];
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Start playing the video
        videoView.start();
    }
}
