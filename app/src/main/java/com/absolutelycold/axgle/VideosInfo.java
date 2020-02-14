package com.absolutelycold.axgle;

import android.graphics.Bitmap;
import android.widget.TextView;

import java.util.HashMap;

public abstract class VideosInfo {
    public int TYPE;
    public abstract String getTitle(int index);

    public void setType(int type) {
        this.TYPE = type;
    }
    public abstract String getKeyword(int index);

//    public abstract Bitmap getCoverBitmap(int index);

    public abstract Integer getTotalViews(int index);

    public abstract Integer getVideoCount(int index);

    public abstract HashMap getItem(int index);

    public abstract Integer ItemsCount();

    public abstract Bitmap getCoverBitmap(int index);

    public abstract void addCoverBitmap(Integer index, Bitmap bitmap);

    public abstract String getCoverUrl(int index);
}
