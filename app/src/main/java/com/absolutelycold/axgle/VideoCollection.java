package com.absolutelycold.axgle;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoCollection extends VideosInfo{

    private int page;
    private int limit;

    private ArrayList<HashMap<String,Object>> collections = null;
    private ArrayList<HashMap<String,Object>> tempCollections;
    public VideoCollection(int page, int limit) {
        this.page = page;
        this.limit = limit;

        ArrayList<HashMap<String,Object>> getData = AvgleApiHelper.allVideosCollection(page, limit);
        if (getData != null) {
            collections = getData;
        }
    }

    public void LoadMore() {
        page++;
        tempCollections = AvgleApiHelper.allVideosCollection(page, limit);
        if (tempCollections != null) {
            collections.addAll(tempCollections);
            System.out.println(collections);
        }
        System.out.println("LoadMore is called." + ItemsCount());
    }


    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Integer ItemsCount() {
        if (collections == null) {
            return 0;
        }
        else {
            return collections.size();
        }
    }

    public String getTitle(int index) {
        if (index >= collections.size()) {
            return null;
        }
        return (String)collections.get(index).get("title");
    }

    public String getKeyword(int index) {
        if (index >= collections.size()) {
            return null;
        }
        return (String)collections.get(index).get("keyword");
    }

    public Bitmap getCoverBitmap(int index) {
        if (index >= collections.size()) {
            return null;
        }
        return (Bitmap)collections.get(index).get("cover_bitmap");
    }

    public Integer getTotalViews(int index) {
        if (index >= collections.size()) {
            return null;
        }
        return (Integer)collections.get(index).get("total_views");
    }

    public Integer getVideoCount(int index) {
        if (index > collections.size()) {
            return null;
        }

        return (Integer)collections.get(index).get("video_count");
    }

    public void addItem(HashMap hashMap) {
        collections.add(hashMap);
    }

    public HashMap getItem(int index) {
        return collections.get(index);
    }
    public void removeItem(int index) {
        collections.remove(index);
    }

}
