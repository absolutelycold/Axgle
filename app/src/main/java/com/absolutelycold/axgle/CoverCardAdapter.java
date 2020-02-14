package com.absolutelycold.axgle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.util.HashMap;

public class CoverCardAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int num = 0;

    private Bitmap bitmap_404;

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
            bitmap_404 = BitmapFactory.decodeResource(cardView.getContext().getResources(), R.drawable.axgle_404_cover);
            return cardViewHolder;
        }
        else {
            LinearLayout progressBar = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_circle, parent, false);
            ProgressBarHolder progressBarHolder = new ProgressBarHolder(progressBar);
            System.out.println("Create + " + num + "'s ProgressBar View in Recycler view");
            BitmapFactory.decodeResource(progressBar.getContext().getResources(), R.drawable.axgle_404_cover);
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

            TextView coverName = cardView.findViewById(R.id.cover_name);
            coverName.setText(videoCollection.getTitle(position));

            ImageView imageView = (ImageView)cardView.findViewById(R.id.cover_image);
            HashMap<String, Object> collectionItem = videoCollection.getItem(position);
            ProgressBar coverLoadCircle = (ProgressBar)cardView.findViewById(R.id.cover_load_circle);
            Bitmap coverBitmap = videoCollection.getCoverBitmap(position);
            GetCoverFromURLTask getCoverFromURLTask = new GetCoverFromURLTask(collectionItem, videoCollection, position);


            //get the 404 pic from drawable
            //Bitmap bitmap_404 = BitmapFactory.decodeResource(imageView.getContext().getResources(), R.drawable.axgle_404_cover);
            /*Integer statusCode = videoCollection.getCoverStatusCode(position);
            if (statusCode == null) {

            }
            else if (statusCode >= 200 & statusCode <300) {
                //normal
            }
            else if (statusCode >= 400 & statusCode <500) {
                //cannot reach the cover
                videoCollection.addCoverBitmap(position, bitmap_404);
                videoCollection.updateCoverStatusCode(position, 222);
            }
            else {
                // Sever Problem
            }*/

            if (coverBitmap == null) {
                // no cover, not visit the website yet
                coverLoadCircle.setVisibility(View.VISIBLE);
                getCoverFromURLTask.execute();
            }
            else {
                // load cover success

                coverLoadCircle.setVisibility(View.GONE);

            }




            imageView.setImageBitmap(coverBitmap);
            imageView.setContentDescription(videoCollection.getTitle(position));
        }
        if (holder instanceof ProgressBarHolder) {

        }


    }

    public VideosInfo getVideoInfos() {
        return this.videoCollection;
    }

    public class GetCoverFromURLTask extends AsyncTask<Void, Void, Bitmap> {

        HashMap<String, Object> collectItem = null;
        VideoCollection videoCollection = null;
        int position;
        Bitmap bitmap = null;
        Integer statusCode = null;


        public GetCoverFromURLTask(HashMap<String, Object> collectItem, VideoCollection videoCollection, int position) {
            this.collectItem = collectItem;
            this.videoCollection = videoCollection;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(Void... items) {
            HashMap<String, Object> bitmapBundle = AvgleApiHelper.getImageBitmap((String)collectItem.get("cover_url"));


            if (bitmapBundle != null) {
                bitmap = (Bitmap) bitmapBundle.get("bitmap");
                statusCode = (Integer)bitmapBundle.get("code");
                System.out.println(position + " : " + statusCode );
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            videoCollection.updateCoverStatusCode(position, statusCode);

            if (statusCode == null) {
                videoCollection.addCoverBitmap(position, null);
            }
            else if (statusCode >= 200 & statusCode <300) {
                //normal
                videoCollection.addCoverBitmap(position, bitmap);
            }
            else if (statusCode >= 400 & statusCode <500) {
                //cannot reach the cover
                videoCollection.addCoverBitmap(position, bitmap_404);
            }
            else {
                // Sever Problem
                videoCollection.addCoverBitmap(position, null);
            }


            if (videoCollection.getCoverBitmap(position) != null) {
                notifyItemChanged(position);
            }


        }
    }
}
