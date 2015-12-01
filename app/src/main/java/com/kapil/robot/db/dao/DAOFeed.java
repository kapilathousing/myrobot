package com.kapil.robot.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.kapil.robot.db.DbConstants;
import com.kapil.robot.db.LSQLiteOpenHelper;
import com.kapil.robot.model.Feed;
import com.kapil.robot.util.MyLog;

import java.util.ArrayList;


public class DAOFeed {
    protected LSQLiteOpenHelper dbHelper;

    private String[] allColumns = DbConstants.AirNews.ALL_COLUMNS;
    private Context mContext;

    public DAOFeed(Context context) {
        mContext = context;
        dbHelper = LSQLiteOpenHelper.getInstance(context);
    }

    public void saveFeed(ArrayList<Feed>  data,int website) {

        dbHelper.getDatabase().beginTransaction();
        SQLiteStatement insert = dbHelper.getDatabase().compileStatement(DbConstants.Feeds.QUERY_INSERT);


        for (int i = 0; i < data.size(); i++) {

            Feed mFeed = data.get(i);
            insert.bindNull(1);
            insert.bindString(2, mFeed.getTitle());
            insert.bindString(3, mFeed.getLink());
            insert.bindLong(4, website);
            insert.execute();

        }
        dbHelper.getDatabase().setTransactionSuccessful();
        dbHelper.getDatabase().endTransaction();

    }



    public ArrayList<Feed> getAllFeeds(int feedType) {

        Cursor cursor = dbHelper.getDatabase().rawQuery(DbConstants.Feeds.QUERY_SELECT+" where "+DbConstants.Feeds.COLUMN_WEBSITE+" ="+feedType, null);
        ArrayList<Feed> arrProjects = convertCursorToArrayList(cursor);
        System.out.println("arrProject count: " + arrProjects.size());

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return arrProjects;
    }





    private ArrayList<Feed> convertCursorToArrayList(Cursor cursor) {
        ArrayList<Feed> arrListing = new ArrayList<Feed>();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            Feed mFeed = new Feed();

            mFeed.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.Feeds.COLUMN_ID)));
            mFeed.setTitle(cursor.getString(cursor.getColumnIndex(DbConstants.Feeds.COLUMN_TITLE)));
            mFeed.setLink(cursor.getString(cursor.getColumnIndex(DbConstants.Feeds.COLUMN_LINK)));
            mFeed.setWebsite(cursor.getInt(cursor.getColumnIndex(DbConstants.Feeds.COLUMN_WEBSITE)));


            arrListing.add(mFeed);

            cursor.moveToNext();
        }

        return arrListing;
    }


    public void clearDatabase() {
        dbHelper.getDatabase().execSQL(DbConstants.AirNews.QUERY_DELETE);

        MyLog.debugLog("DB Flushed");

    }

}
