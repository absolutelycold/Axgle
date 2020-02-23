package com.absolutelycold.axgle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowUserCollectionActivity extends AppCompatActivity implements CoverCardAdapter.onAdditionalBoxListener, OrderDialogFragment.Listener{

    private RecyclerView recyclerView;
    private VideoUserCollection videoUserCollection;
    private SQLiteOpenHelper dbHelper;
    private CoverCardAdapter recyclerAdapter;
    private Boolean isBlur = true;
    private String selectedVideoName;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_collection);
        Intent intent = getIntent();
        isBlur = intent.getBooleanExtra("needBlur", true);
        dbHelper = new UserFavVideoDatabaseHelper(this);

        recyclerView = findViewById(R.id.user_collect_videos_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = getNewRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void AdditionalBoxClicked(int position, View view) {
        selectedVideoName = videoUserCollection.getTitle(position);
        selectedPosition = position;
        ArrayList<String> options = new ArrayList<>();
        options.add("remove this video");
        OrderDialogFragment.newInstance(options).show(getSupportFragmentManager(), OrderDialogFragment.TAG);
    }

    @Override
    public void onSortOptionSelected(int position) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("UserFav", "VIDEONAME = ?", new String[] {selectedVideoName});
        db.close();
        Toast.makeText(this,"Sucessfully remove this video from your collection.", Toast.LENGTH_SHORT).show();

        recyclerView.setAdapter(getNewRecyclerAdapter());
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public CoverCardAdapter getNewRecyclerAdapter() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        videoUserCollection = new VideoUserCollection(db);
        db.close();
        CoverCardAdapter recyclerAdapter = new CoverCardAdapter(videoUserCollection, isBlur, this);
        recyclerAdapter.SetAllVideoListener(new CoverCardAdapter.AllVideoListener() {
            @Override
            public void onSingleVideoChoose(int position) {
                Intent intent = new Intent(ShowUserCollectionActivity.this, ShowVideoPreviewActivity.class);
                intent.putExtra("preview_url", videoUserCollection.getPreviewUrl(position));
                intent.putExtra("video_url",videoUserCollection.getVideoUrl(position));
                intent.putExtra("video_embedded_url", videoUserCollection.getEmbeddedVideoUrl(position));
                startActivity(intent);
            }
        });
        return recyclerAdapter;
    }
}
