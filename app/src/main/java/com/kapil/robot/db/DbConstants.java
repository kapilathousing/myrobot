package com.kapil.robot.db;


/**
 * Created by housing on 13/07/15.
 */
public class DbConstants {
    public static final String DB_NAME = "myrobot.db";

    public static final int DATABASE_VERSION = 1;


    public static class Feeds {
        public static final String TABLE_NAME = "tbl_feed";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_WEBSITE = "website";




        public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME + " ( "
                + COLUMN_ID + " INTEGER primary key AUTOINCREMENT, "
                + COLUMN_TITLE + " text, "
                + COLUMN_LINK + " text, "
                + COLUMN_WEBSITE + " int "
                + "); ";

        public static final String[] ALL_COLUMNS = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_LINK,
                COLUMN_WEBSITE

        };

        //public static final String QUERY_INSERT = "INSERT OR REPLACE INTO " + TABLE_NAME + " ( " +
        public static final String QUERY_INSERT = "INSERT INTO " + TABLE_NAME + " ( " +
                COLUMN_ID + ", " +
                COLUMN_TITLE + ", " +
                COLUMN_LINK + ", " +
                COLUMN_WEBSITE +
                " ) VALUES (?,?, ?,?);";

        public static final String QUERY_SELECT = "SELECT * FROM " + TABLE_NAME;


        public static final String QUERY_DELETE = "DELETE FROM " + TABLE_NAME;

        public static final String QUERY_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }



    public static class AirNews {
        public static final String TABLE_NAME = "tbl_airnews";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_IS_READY = "is_ready";
        public static final String COLUMN_DOWNLOAD_ID = "download_id";



        public static final String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME + " ( "
                + COLUMN_ID + " INTEGER primary key AUTOINCREMENT, "
                + COLUMN_NAME + " text, "
                + COLUMN_URL + " text, "
                + COLUMN_CREATED + " text not null, "
                + COLUMN_IS_READY + " int default 0, "
                + COLUMN_DOWNLOAD_ID + " long "
                + "); ";

        public static final String[] ALL_COLUMNS = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_URL,
                COLUMN_CREATED,
                COLUMN_IS_READY,
                COLUMN_DOWNLOAD_ID

        };

        //public static final String QUERY_INSERT = "INSERT OR REPLACE INTO " + TABLE_NAME + " ( " +
        public static final String QUERY_INSERT = "INSERT INTO " + TABLE_NAME + " ( " +
                COLUMN_ID + ", " +
                COLUMN_NAME + ", " +
                COLUMN_URL + ", " +
                COLUMN_CREATED + ", " +
                COLUMN_IS_READY + ", " +
                COLUMN_DOWNLOAD_ID+
                " ) VALUES (?,?, ?,?, ?,?);";

        public static final String QUERY_SELECT = "SELECT * FROM " + TABLE_NAME+" order by "+COLUMN_ID+" DESC";

        public static final String QUERY_SELECT_ON_URL = "SELECT * FROM " + TABLE_NAME+" where "+COLUMN_URL+" =";


        public static final String QUERY_DELETE = "DELETE FROM " + TABLE_NAME;

        public static final String QUERY_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }



}
