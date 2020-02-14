package com.absolutelycold.axgle;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

public class AllVideos extends VideosInfo {

    public final Integer TYPE = 2;
    private Integer page;
    private Integer limit;
    private ArrayList<HashMap<String, Object>> allVideosInfo = null;
    private HashMap<Integer, Bitmap> bitmaps = new HashMap<>();
    private ArrayList<HashMap<String, Object>> tempAllVideosInfo = null;


    public AllVideos(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;
        setType(CoverCardAdapter.VIEW_TYPE_ALL_VIDEOS);
        ArrayList<HashMap<String, Object>> getData= AvgleApiHelper.allVideos(this.page, this.limit);
        if (getData != null) {
            this.allVideosInfo = getData;
            System.out.println("AllVideosInfo Created: " + allVideosInfo);
        }

    }

    public void LoadMore() {
        page++;
        tempAllVideosInfo = AvgleApiHelper.allVideos(page, limit);
        if (tempAllVideosInfo != null) {
            allVideosInfo.addAll(tempAllVideosInfo);
        }
    }
    @Override
    public String getTitle(int index) {
        if (index >= allVideosInfo.size()) {
            return null;
        }

        return (String)allVideosInfo.get(index).get("title");
    }

    @Override
    public String getKeyword(int index) {
        if (index >= allVideosInfo.size()) {
            return null;
        }
        return (String)allVideosInfo.get(index).get("keyword");
    }

    @Override
    public Integer getTotalViews(int index) {
        if(index >= allVideosInfo.size()) {
            return null;
        }
        return (Integer)allVideosInfo.get(index).get("viewnum");
    }

    public String getDuration(int index) {
        if (index >= allVideosInfo.size()) {
            return null;
        }
        return (String)allVideosInfo.get(index).get("duration");
    }

    public String getUploadTime(int index) {
        if (index >= allVideosInfo.size()) {
            return null;
        }
        return (String)allVideosInfo.get(index).get("uploadTime");
    }
    @Override
    public Integer getVideoCount(int index) {
        return null;
    }

    @Override
    public HashMap getItem(int index) {
        if (index >= allVideosInfo.size()) {
            return null;
        }
        return allVideosInfo.get(index);
    }

    @Override
    public Integer ItemsCount() {
        if (allVideosInfo == null) {
            return 0;
        }
        else {
            return allVideosInfo.size();
        }
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
        if (index >= allVideosInfo.size()) {
            return null;
        }
        return (String) allVideosInfo.get(index).get("preview_url");
    }

    public void addItem(HashMap hashMap) {
        allVideosInfo.add(hashMap);
    }

    public void removeItem(int index) {
        allVideosInfo.remove(index);
    }
}
