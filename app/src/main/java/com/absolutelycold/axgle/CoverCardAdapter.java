package com.absolutelycold.axgle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CoverCardAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int num = 0;

    private VideoCollection videoCollection = null;

    public CoverCardAdapter(VideosInfo vc) {
        this.videoCollection = (VideoCollection)vc;
    }

    public static class ProgressBarHolder extends RecyclerView.ViewHolder {

        public LinearLayout progressBar;
        public ProgressBarHolder(@NonNull LinearLayout itemView) {
            super(itemView);
            progressBar = itemView;
        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public CardViewHolder(@NonNull CardView itemView) {
            super(itemView);
            this.cardView = itemView;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        num++;



        if (viewType == VIEW_TYPE_ITEM) {
            CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cover_card_view, parent, false);
            CardViewHolder cardViewHolder = new CardViewHolder(cardView);
            System.out.println("Create + " + num + "'s Normal View in Recycler view");
            return cardViewHolder;
        }
        else {
            LinearLayout progressBar = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_circle, parent, false);
            ProgressBarHolder progressBarHolder = new ProgressBarHolder(progressBar);
            System.out.println("Create + " + num + "'s ProgressBar View in Recycler view");
            return progressBarHolder;
        }


    }

    @Override
    public int getItemCount() {
        return videoCollection.ItemsCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (videoCollection.getItem(position) == null) {
            return VIEW_TYPE_LOADING;
        }
        else{
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardViewHolder) {
            CardView cardView = ((CardViewHolder)holder).cardView;
            ImageView imageView = (ImageView)cardView.findViewById(R.id.cover_image);
            imageView.setImageBitmap(videoCollection.getCoverBitmap(position));
            imageView.setContentDescription(videoCollection.getTitle(position));
            TextView coverName = cardView.findViewById(R.id.cover_name);
            coverName.setText(videoCollection.getTitle(position));
        }
        if (holder instanceof ProgressBarHolder) {

        }


    }

    public VideosInfo getVideoInfos() {
        return this.videoCollection;
    }
}
