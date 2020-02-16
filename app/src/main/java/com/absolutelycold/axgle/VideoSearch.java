package com.absolutelycold.axgle;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoSearch extends AllVideos {
    public VideoSearch(String searchContent, Integer page, Integer limit, Integer CHID, String order) {
        super(page, limit, CHID, order);

        setPage(page);
        setLimit(limit);
        setCHID(CHID);
        setOrder(order);
        setType(CoverCardAdapter.VIEW_TYPE_ALL_VIDEOS);

        ArrayList<HashMap<String, Object>> getData = AvgleApiHelper.searchVideos(searchContent, page,limit, CHID, order);
        //ArrayList<HashMap<String, Object>> getData= AvgleApiHelper.allVideos(this.page, this.limit, this.CHID, this.order);
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
}
