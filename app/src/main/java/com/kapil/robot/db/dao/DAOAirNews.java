package com.kapil.robot.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.kapil.robot.db.DbConstants;
import com.kapil.robot.db.LSQLiteOpenHelper;
import com.kapil.robot.model.AirNews;
import com.kapil.robot.util.MyLog;

import java.util.ArrayList;


public class DAOAirNews {
    protected LSQLiteOpenHelper dbHelper;

    private String[] allColumns = DbConstants.AirNews.ALL_COLUMNS;
    private Context mContext;

    public DAOAirNews(Context context) {
        mContext = context;
        dbHelper = LSQLiteOpenHelper.getInstance(context);
    }

    public void saveAirNews(AirNews data) {

        ContentValues cv=new ContentValues();
        cv.put(DbConstants.AirNews.COLUMN_NAME,data.getName());
        cv.put(DbConstants.AirNews.COLUMN_URL,  data.getUrl());
        cv.put(DbConstants.AirNews.COLUMN_CREATED,data.getCreated());
        cv.put(DbConstants.AirNews.COLUMN_IS_READY,data.getIsReady());
        cv.put(DbConstants.AirNews.COLUMN_DOWNLOAD_ID,data.getDownloadId());
        dbHelper.getDatabase().delete(DbConstants.AirNews.TABLE_NAME,DbConstants.AirNews.COLUMN_NAME + "='" + data.getName()+"'",null);

        dbHelper.getDatabase().insert(DbConstants.AirNews.TABLE_NAME,null,cv);

    }
public boolean checkNewsStatus(String url){

    MyLog.debugLog("checking previously loaded news status");
    Cursor cursor = dbHelper.getDatabase().rawQuery(DbConstants.AirNews.QUERY_SELECT_ON_URL+"'"+url+"'", null);
    if(cursor.getCount()>0)
        return  true;
    else return false;
}


    public ArrayList<AirNews> getAllNews() {

        Cursor cursor = dbHelper.getDatabase().rawQuery(DbConstants.AirNews.QUERY_SELECT, null);
        ArrayList<AirNews> arrProjects = convertCursorToArrayList(cursor);
        System.out.println("arrProject count: " + arrProjects.size());

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return arrProjects;
    }


public void updateDownloadStatus(long downloadId){
    ContentValues cv=new ContentValues();
    cv.put(DbConstants.AirNews.COLUMN_DOWNLOAD_ID, downloadId);
    cv.put(DbConstants.AirNews.COLUMN_IS_READY, 1);
    int rowAffected=dbHelper.getDatabase().update(DbConstants.AirNews.TABLE_NAME,cv,DbConstants.AirNews.COLUMN_DOWNLOAD_ID+" = "+downloadId,null);
    MyLog.debugLog("#Download updated rows:"+rowAffected);


}


    private ArrayList<AirNews> convertCursorToArrayList(Cursor cursor) {
        ArrayList<AirNews> arrListing = new ArrayList<AirNews>();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            AirNews mAirNews = new AirNews();

            mAirNews.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.AirNews.COLUMN_ID)));
            mAirNews.setName(cursor.getString(cursor.getColumnIndex(DbConstants.AirNews.COLUMN_NAME)));
            mAirNews.setUrl(cursor.getString(cursor.getColumnIndex(DbConstants.AirNews.COLUMN_URL)));
            mAirNews.setCreated(cursor.getString(cursor.getColumnIndex(DbConstants.AirNews.COLUMN_CREATED)));
            mAirNews.setIsReady(cursor.getInt(cursor.getColumnIndex(DbConstants.AirNews.COLUMN_IS_READY)));


            arrListing.add(mAirNews);

            cursor.moveToNext();
        }

        return arrListing;
    }


    public void clearDatabase() {
        dbHelper.getDatabase().execSQL(DbConstants.AirNews.QUERY_DELETE);

        MyLog.debugLog("DB Flushed");

    }

}
