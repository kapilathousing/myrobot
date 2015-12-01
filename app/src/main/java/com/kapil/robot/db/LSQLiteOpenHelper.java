package com.kapil.robot.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by mahendraliya on 13/07/15.
 */
public class LSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private Context mContext;
    private static LSQLiteOpenHelper dbHelperInstance = null;
    private static SQLiteDatabase database;

    private LSQLiteOpenHelper(Context context) {
        super(context, DbConstants.DB_NAME, null, DbConstants.DATABASE_VERSION);
        mContext = context;
    }

    public static LSQLiteOpenHelper getInstance(Context context) {
        if (dbHelperInstance == null) {
            dbHelperInstance = new LSQLiteOpenHelper(context);
            database = dbHelperInstance.getWritableDatabase();
        }

        return dbHelperInstance;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.Feeds.CREATE_SCRIPT);
        db.execSQL(DbConstants.AirNews.CREATE_SCRIPT);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbConstants.AirNews.QUERY_DROP_TABLE);
        db.execSQL(DbConstants.Feeds.QUERY_DROP_TABLE);


        onCreate(db);
    }

    public void clearDatabase(SQLiteDatabase db) {
        //db.execSQL(DbConstants.Locations.QUERY_DELETE);

    }

    public void clearOldData(SQLiteDatabase db) {
        // db.execSQL(DbConstants.Listings.getQueryForRemovingOldData(HCUtils.getPreference(mContext, HCConstants.PREF_CLEAR_DATA_AFTER_DAYS, HCConfig.DELETE_DATA_AFTER_DAYS)));
    }
}
