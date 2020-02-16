package com.absolutelycold.axgle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
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

    public static final int VIEW_TYPE_COLLECTIONS = 0;
    public static final int VIEW_TYPE_LOADING = 1;
    public static final int VIEW_TYPE_ALL_VIDEOS = 2;
    private int num = 0;

    private AllVideoListener allVideoListener;
    private Bitmap bitmap_404;

    //private VideoCollection videoCollection = null;
    private VideosInfo videosInfo = null;


    public CoverCardAdapter(VideosInfo vc) {
        //this.videoCollection = (VideoCollection)vc;
        this.videosInfo = vc;
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



        if (viewType == VIEW_TYPE_COLLECTIONS) {
            CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cover_card_view, parent, false);
            CardViewHolder cardViewHolder = new CardViewHolder(cardView);
            bitmap_404 = BitmapFactory.decodeResource(cardView.getContext().getResources(), R.drawable.axgle_404_cover);
            return cardViewHolder;
        }
        else if (viewType == VIEW_TYPE_ALL_VIDEOS) {
            CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.all_video_card_view, parent, false);
            CardViewHolder cardViewHolder = new CardViewHolder(cardView);
            bitmap_404 = BitmapFactory.decodeResource(cardView.getContext().getResources(), R.drawable.axgle_404_cover);
            return cardViewHolder;
        }
        else {
            LinearLayout progressBar = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_circle, parent, false);
            ProgressBarHolder progressBarHolder = new ProgressBarHolder(progressBar);
            BitmapFactory.decodeResource(progressBar.getContext().getResources(), R.drawable.axgle_404_cover);
            return progressBarHolder;
        }


    }

    @Override
    public int getItemCount() {
        if (videosInfo == null) {
            return 0;
        }
        return videosInfo.ItemsCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (videosInfo.getItem(position) == null) {
            return VIEW_TYPE_LOADING;
        }
        else if (videosInfo.TYPE == VIEW_TYPE_COLLECTIONS){
            return VIEW_TYPE_COLLECTIONS;
        }
        else {
            return VIEW_TYPE_ALL_VIDEOS;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CardViewHolder) {
            if (videosInfo.TYPE == VIEW_TYPE_COLLECTIONS) {
                CardView cardView = ((CardViewHolder)holder).cardView;
                TextView coverName = cardView.findViewById(R.id.cover_name);
                coverName.setText(((VideoCollection)videosInfo).getTitle(position));
                TextView totalView = cardView.findViewById(R.id.total_view);
                totalView.setText(((VideoCollection)videosInfo).getTotalViews(position).toString());
                TextView videoNum = cardView.findViewById(R.id.video_num);
                videoNum.setText(((VideoCollection)videosInfo).getVideoCount(position).toString());

                ImageView imageView = (ImageView)cardView.findViewById(R.id.cover_image);
                //HashMap<String, Object> collectionItem = ((VideoCollection)videosInfo).getItem(position);
                ProgressBar coverLoadCircle = (ProgressBar)cardView.findViewById(R.id.cover_load_circle);
                Bitmap coverBitmap = ((VideoCollection)videosInfo).getCoverBitmap(position);
                GetCoverFromURLTask getCoverFromURLTask = new GetCoverFromURLTask(((VideoCollection)videosInfo), position);


                //get the 404 pic from drawable
                //Bitmap bitmap_404 = BitmapFactory.decodeResource(imageView.getContext().getResources(), R.drawable.axgle_404_cover);
            /*Integer statusCode = ((VideoCollection)videosInfo).getCoverStatusCode(position);
            if (statusCode == null) {

            }
            else if (statusCode >= 200 & statusCode <300) {
                //normal
            }
            else if (statusCode >= 400 & statusCode <500) {
                //cannot reach the cover
                ((VideoCollection)videosInfo).addCoverBitmap(position, bitmap_404);
                ((VideoCollection)videosInfo).updateCoverStatusCode(position, 222);
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
                imageView.setContentDescription(((VideoCollection)videosInfo).getTitle(position));
            }else {
                CardView cardView = ((CardViewHolder)holder).cardView;

                TextView videoName = cardView.findViewById(R.id.video_name);
                videoName.setText(((AllVideos)videosInfo).getTitle(position));
                TextView videoDuration = cardView.findViewById(R.id.video_duration);
                videoDuration.setText(((AllVideos)videosInfo).getDuration(position));
                TextView videoUploadTime = cardView.findViewById(R.id.upload_time);
                videoUploadTime.setText(((AllVideos)videosInfo).getUploadTime(position));
                ImageView imageView = (ImageView)cardView.findViewById(R.id.all_video_image);
                TextView playNumView = cardView.findViewById(R.id.play_num);
                playNumView.setText(((AllVideos)videosInfo).getTotalViews(position).toString());
                //HashMap<String, Object> collectionItem = ((AllVideos)videosInfo).getItem(position);
                ProgressBar coverLoadCircle = (ProgressBar)cardView.findViewById(R.id.all_video_load_circle);
                Bitmap coverBitmap = videosInfo.getCoverBitmap(position);

                GetCoverFromURLTask getCoverFromURLTask = new GetCoverFromURLTask(videosInfo, position);

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
                imageView.setContentDescription(videosInfo.getTitle(position));
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        allVideoListener.onSingleVideoChoose(position);
                    }
                });

            }

        }
        if (holder instanceof ProgressBarHolder) {

        }


    }

    public interface AllVideoListener {
        void onSingleVideoChoose(int position);
    }

    public void SetAllVideoListener(AllVideoListener allVideoListener) {
        this.allVideoListener = allVideoListener;
    }

    public VideosInfo getVideoInfos() {
        return this.videosInfo;
    }

    public class GetCoverFromURLTask extends AsyncTask<Void, Void, Bitmap> {

        //VideoCollection videoCollection = null;
        VideosInfo videosInfo = null;
        int position;
        Bitmap bitmap = null;
        Integer statusCode = null;


        public GetCoverFromURLTask(VideosInfo videosInfo, int position) {
            //this.videoCollection = (VideoCollection) videosInfo;
            this.videosInfo = videosInfo;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(Void... items) {
            HashMap<String, Object> bitmapBundle = AvgleApiHelper.getImageBitmap(videosInfo.getCoverUrl(position));


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

            //videoCollection.updateCoverStatusCode(position, statusCode);

            if (statusCode == null) {
                videosInfo.addCoverBitmap(position, null);
            }
            else if (statusCode >= 200 & statusCode <300) {
                //normal
                videosInfo.addCoverBitmap(position, bitmap);
            }
            //else if (statusCode >= 400 & statusCode <500) {
            else if (statusCode == 404){
                //cannot reach the cover
                videosInfo.addCoverBitmap(position, bitmap_404);
            }
            else {
                // Sever Problem
                videosInfo.addCoverBitmap(position, null);
            }


            if (videosInfo.getCoverBitmap(position) != null) {
                notifyItemChanged(position);
            }


        }
    }
}
