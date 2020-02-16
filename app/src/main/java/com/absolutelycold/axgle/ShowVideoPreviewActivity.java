package com.absolutelycold.axgle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShowVideoPreviewActivity extends AppCompatActivity implements OrderDialogFragment.Listener{

    String previewVideoUrl;
    String videoUrl;
    String embeddedUrl;
    VideoView videoView;
    View decorView;
    ArrayList<String> fabOptions = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    OrderDialogFragment orderDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video_preview);
        //show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //eliminate the -------- in the action bar
        getSupportActionBar().setElevation(0);
        //hide the action bar
        getSupportActionBar().hide();
        //hide the status bar/fullscreen
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        decorView.setBackgroundColor(Color.BLACK);
        if (savedInstanceState != null) {
            this.previewVideoUrl = savedInstanceState.getString("preview_video_url");
            this.embeddedUrl = savedInstanceState.getString("embedded_url");
            this.videoUrl = savedInstanceState.getString("video_url");
        }
        else {
            Intent intent = getIntent();
            previewVideoUrl = intent.getExtras().getString("preview_url");
            videoUrl = intent.getExtras().getString("video_url");
            embeddedUrl = intent.getExtras().getString("video_embedded_url");
        }

        fabOptions.add("Cancel");
        fabOptions.add("Watch Embedded Video");
        fabOptions.add("Visit Official Website");
        fabOptions.add("Back");



        floatingActionButton = findViewById(R.id.preview_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderDialogFragment = OrderDialogFragment.newInstance(fabOptions);
                orderDialogFragment.setCancelable(false);
                orderDialogFragment.show(getSupportFragmentManager(), OrderDialogFragment.TAG);
            }
        });
        videoView = findViewById(R.id.video_preview);

        //System.out.println("Play Url: " + previewVideoUrl);

    }

    @Override
    protected void onResume() {
        super.onResume();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        videoView.setVideoPath(previewVideoUrl);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        videoView.start();
    }

    @Override
    public void onSortOptionSelected(int position) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        System.out.println("Video Preview: " + position + " Clicked...");
        switch (position) {
            case 0:
                orderDialogFragment.dismiss();
                break;
            case 1:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(embeddedUrl));
                startActivity(intent);
                break;
            case 2:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(videoUrl));
                startActivity(intent1);
                break;
            case 3:
                finish();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("preview_video_url", previewVideoUrl);
        outState.putString("video_url", videoUrl);
        outState.putString("embedded_url", embeddedUrl);
    }
}
