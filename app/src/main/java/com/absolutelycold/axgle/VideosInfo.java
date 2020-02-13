package com.absolutelycold.axgle;

import android.graphics.Bitmap;

import java.util.HashMap;

public abstract class VideosInfo {
    public abstract String getTitle(int index);

    public abstract String getKeyword(int index);

    public abstract Bitmap getCoverBitmap(int index);

    public abstract Integer getTotalViews(int index);

    public abstract Integer getVideoCount(int index);

    public abstract HashMap getItem(int index);

    public abstract Integer ItemsCount();
}
