package com.absolutelycold.axgle;

import android.graphics.Bitmap;

import androidx.annotation.IntegerRes;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoCollection extends VideosInfo{

    //public final Integer TYPE = 0;
    private int page;
    private int limit;
    private HashMap<Integer, Bitmap> bitmaps = new HashMap<>();
    private HashMap<Integer, Integer> coverStatusCodes = new HashMap<>();

    private ArrayList<HashMap<String,Object>> collections = null;
    private ArrayList<HashMap<String,Object>> tempCollections;
    public VideoCollection(int page, int limit) {
        this.page = page;
        this.limit = limit;

        setType(CoverCardAdapter.VIEW_TYPE_COLLECTIONS);
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
        //System.out.println("LoadMore is called." + ItemsCount());
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
        if (index >= bitmaps.size()) {
            return null;
        }
        return bitmaps.get(index);
    }

    public void addCoverBitmap(Integer index, Bitmap bitmap) {
        if (bitmaps.get(index) == null) {
            this.bitmaps.put(index, bitmap);
        }

    }

    @Override
    public String getCoverUrl(int index) {
        if (index >= collections.size()) {
            return null;
        }

        return (String)collections.get(index).get("cover_url");
    }

    public Integer getCoverStatusCode(int index) {
        if (index >= coverStatusCodes.size()) {
            return null;
        }

        return coverStatusCodes.get(index);
    }

    public void updateCoverStatusCode(Integer index, Integer code) {
        if (coverStatusCodes.get(index) == null) {
            coverStatusCodes.put(index, code);
        }
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
