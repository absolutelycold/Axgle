package com.absolutelycold.axgle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserFavVideoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "UserFav";
    private static final int DB_VERSION = 1;

    public UserFavVideoDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE USERFAV (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "VIDEONAME TEXT," +
                    "COVERURL TEXT," +
                    "ISHD BOOLEAN," +
                    "LIKENUM INTEGER," +
                    "DISLIKENUM INTEGER," +
                    "UPDATETIME INTEGER," +
                    "VIDEOLENGTH INTEGER)");
        }
    }
}
