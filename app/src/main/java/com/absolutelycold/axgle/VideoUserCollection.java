package com.absolutelycold.axgle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class VideoUserCollection extends VideosInfo {

    private ArrayList<HashMap<String, Object>> userCollectionInfo = new ArrayList<>();
    private HashMap<Integer, Bitmap> bitmaps = new HashMap<>();

    private UserFavVideoDatabaseHelper dbHelper = null;
    public VideoUserCollection(SQLiteDatabase db) {
        setType(CoverCardAdapter.VIEW_TYPE_FAV_COLLECTION);
        Cursor cursor = db.rawQuery("SELECT * FROM UserFav", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String videoName = cursor.getString(1);
            String coverUrl = cursor.getString(2);
            String previewUrl = cursor.getString(3);
            String embededUrl = cursor.getString(4);
            String videoUrl = cursor.getString(5);
            String keywords = cursor.getString(6);
            Boolean isHD = (cursor.getInt(7) == 1);
            Integer totalViews = cursor.getInt(8);
            Integer likeNum = cursor.getInt(9);
            Integer disLikeNum = cursor.getInt(10);
            Integer updateTimeStamp = cursor.getInt(11);
            Integer videoDurationInt = cursor.getInt(12);

            Date uploadDate = new Date((long) updateTimeStamp* 1000);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String updateTimeString = dateFormat.format(uploadDate);

            int hours = videoDurationInt / 3600;
            int minutes = (videoDurationInt % 3600) / 60;
            int seconds = videoDurationInt % 60;
            String durationString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            HashMap<String, Object> singleCollectionInfo = new HashMap<>();
            singleCollectionInfo.put("videoName", videoName);
            singleCollectionInfo.put("coverUrl", coverUrl);
            singleCollectionInfo.put("previewUrl", previewUrl);
            singleCollectionInfo.put("embeddedUrl", embededUrl);
            singleCollectionInfo.put("videoUrl", videoUrl);
            singleCollectionInfo.put("keywords", keywords);
            singleCollectionInfo.put("isHD", isHD);
            singleCollectionInfo.put("TotalView", totalViews);
            singleCollectionInfo.put("likeNum", likeNum);
            singleCollectionInfo.put("disLikeNum", disLikeNum);
            singleCollectionInfo.put("updateTime", updateTimeString);
            singleCollectionInfo.put("duration", durationString);

            System.out.println("User Collection" + singleCollectionInfo);
            userCollectionInfo.add(singleCollectionInfo);
        }

    }

    @Override
    public String getTitle(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (String) userCollectionInfo.get(index).get("videoName");
    }

    @Override
    public String getKeyword(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (String) userCollectionInfo.get(index).get("keywords");
    }

    @Override
    public Integer getTotalViews(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (Integer) userCollectionInfo.get(index).get("TotalView");
    }

    @Override
    public Integer getVideoCount(int index) {
        return null;
    }

    @Override
    public HashMap getItem(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return new HashMap();
    }

    @Override
    public Integer ItemsCount() {
        return userCollectionInfo.size();
    }

    @Override
    public Bitmap getCoverBitmap(int index) {
        if (index >= bitmaps.size()) {
            return null;
        }
        return bitmaps.get(index);
    }

    @Override
    public void addCoverBitmap(Integer index, Bitmap bitmap) {
        if (bitmaps.get(index) == null) {
            this.bitmaps.put(index, bitmap);
        }
    }

    @Override
    public String getCoverUrl(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (String) userCollectionInfo.get(index).get("coverUrl");
    }

    public String getDuration(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (String) userCollectionInfo.get(index).get("duration");
    }

    public String getUploadTime(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }

        return (String) userCollectionInfo.get(index).get("updateTime");
    }

    public Boolean getIsHD(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (Boolean) userCollectionInfo.get(index).get("isHD");
    }

    public Integer getLikeNumber(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (Integer) userCollectionInfo.get(index).get("likeNum");
    }

    public Integer getDislikeNumber(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (Integer) userCollectionInfo.get(index).get("disLikeNum");
    }

    public String getPreviewUrl(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (String) userCollectionInfo.get(index).get("previewUrl");
    }

    public String getVideoUrl(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (String) userCollectionInfo.get(index).get("videoUrl");
    }

    public String getEmbeddedVideoUrl(int index) {
        if (index >= userCollectionInfo.size()) {
            return null;
        }
        return (String) userCollectionInfo.get(index).get("embeddedUrl");
    }
}
