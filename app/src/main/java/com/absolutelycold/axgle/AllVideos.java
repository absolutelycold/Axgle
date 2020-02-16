package com.absolutelycold.axgle;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

public class AllVideos extends VideosInfo {

    public final Integer TYPE = 2;
    private Integer page;
    private Integer limit;
    private Integer CHID;
    private String order;
    private ArrayList<HashMap<String, Object>> allVideosInfo = null;
    private HashMap<Integer, Bitmap> bitmaps = new HashMap<>();
    private ArrayList<HashMap<String, Object>> tempAllVideosInfo = null;
    private boolean isReachEnd = false;


    public AllVideos(Integer page, Integer limit, Integer CHID, String order) {
        this.page = page;
        this.limit = limit;
        this.CHID = CHID;
        this.order = order;
        setType(CoverCardAdapter.VIEW_TYPE_ALL_VIDEOS);
        ArrayList<HashMap<String, Object>> getData= AvgleApiHelper.allVideos(this.page, this.limit, this.CHID, this.order);
        if (getData != null) {
            setAllVideosInfo(getData);
            //this.allVideosInfo = getData;
            //System.out.println("AllVideosInfo Created: " + allVideosInfo);
        }

        int totalVideos = getTotalVideoNum();
        int remain = Math.abs(totalVideos - ((page + 1) * limit));
        if (remain < limit) {
            setReachEnd(true);
        }

    }

    public void LoadMore() {

        if (!isReachEnd()) {
            page++;
            int totalVideos = getTotalVideoNum();
            int remain = Math.abs(totalVideos - (page * limit));
            if (remain < limit) {
                limit = remain;
                isReachEnd = true;
            }
            tempAllVideosInfo = AvgleApiHelper.allVideos(page, limit, this.CHID, this.order);
            if (tempAllVideosInfo != null) {
                allVideosInfo.addAll(tempAllVideosInfo);
            }
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

    public String getPreviewUrl(int index) {
        if (index >= allVideosInfo.size()) {
            return null;
        }
        return (String)allVideosInfo.get(index).get("preview_video_url");
    }

    public  String getVideoUrl(int index) {
        if (index >= allVideosInfo.size()) {
            return null;
        }
        return (String)allVideosInfo.get(index).get("video_url");
    }

    public String getEmbeddedVideoUrl(int index) {
        if (index >= allVideosInfo.size()) {
            return null;
        }

        return (String)allVideosInfo.get(index).get("embedded_url");
    }

    public void addItem(HashMap hashMap) {
        allVideosInfo.add(hashMap);
    }

    public void removeItem(int index) {
        allVideosInfo.remove(index);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setCHID(Integer CHID) {
        this.CHID = CHID;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setAllVideosInfo(ArrayList<HashMap<String, Object>> allVideosInfo) {
        this.allVideosInfo = allVideosInfo;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getCHID() {
        return CHID;
    }

    public String getOrder() {
        return order;
    }

    public ArrayList<HashMap<String, Object>> getAllVideosInfo() {
        return allVideosInfo;
    }

    public Integer getTotalVideoNum() {
        if (allVideosInfo == null) {
            return 0;
        }
        return (Integer)allVideosInfo.get(0).get("total_videos");
    };

    public boolean isReachEnd() {
        return isReachEnd;
    }

    public void setReachEnd(boolean reachEnd) {
        isReachEnd = reachEnd;
    }

}
