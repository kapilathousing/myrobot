package com.kapil.robot.services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kapil.robot.db.dao.DAOAirNews;
import com.kapil.robot.db.dao.DAOFeed;
import com.kapil.robot.model.AirNews;
import com.kapil.robot.model.Feed;
import com.kapil.robot.ui.MainActivity;
import com.kapil.robot.ui.R;
import com.kapil.robot.util.Constants;
import com.kapil.robot.util.Misc;
import com.kapil.robot.util.MyLog;
import com.kapil.robot.util.NetworkController;
import com.kapil.robot.util.XmlParsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;



public class FeedSyncService extends IntentService {
    Uri external_path;
    private DownloadManager dm;
    private ArrayList<Long> enqueue;
    Calendar mCalendar = Calendar.getInstance();

    public FeedSyncService() {
        super("FeedSyncService");
    }

    protected void callServer(String url, int website) {
        NetworkController nc = new NetworkController();
        String response = nc.doGet(url);

        if (response != null) {
            XmlParsing parser = new XmlParsing();

            ArrayList<Feed> feeds = parser.getFeedList(response, getApplicationContext(), website);
            MyLog.debugLog("Total Feed:" + feeds.size());

            DAOFeed daoFeeds = new DAOFeed(getApplicationContext());

            daoFeeds.saveFeed(feeds, website);

            //saveData(feeds,website);
            String notificationData[] = null;
            //MyLog.debugLog("Total Feed:"+feeds.size());
            if (feeds.size() == 1)
                notificationData = new String[]{feeds.get(0).title};
            else if (feeds.size() > 1)
                notificationData = new String[]{feeds.get(0).title, feeds.get(1).title};
            if (notificationData != null)
                sendNotification(notificationData, getApplicationContext(), website);


        } else {
            MyLog.debugLog("blank response");

        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MyLog.debugLog("onHandleIntent");
        if (Misc.isConnected(getApplicationContext())) {
            callServer(Constants.STACK_OVERFLOW_URL, Constants.STACK_OVERFLOW);
            callServer(Constants.ANDROID_DEVELOPER_URL, Constants.ANDROID_DEVELOPER);
            callServer(Constants.ECONOMIC_TIMES_URL, Constants.ECONOMIC_TIMES);

            callAirNews(Constants.AIR_NEWS_URL);
        } else {
            Misc.cancelAlarm(getApplicationContext());
            Misc.toggleComponent(getApplicationContext(), true);
        }
    }


   /* BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (enqueue.contains(downloadId)) {
                    enqueue.remove((Long) downloadId);
                    if (enqueue.size() == 0) {
                        unregisterReceiver(receiver);
                    }
                    DAOAirNews daoAirNews = new DAOAirNews(getApplicationContext());
                    daoAirNews.updateDownloadStatus(downloadId);
                }

            }


        }
    };*/

    private void callAirNews(String url) {

MyLog.debugLog("called Airnews now");
       /* registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));*/

       // StringBuffer buffer = new StringBuffer();
        try {
            DAOAirNews daoAirNews = new DAOAirNews(getApplicationContext());

           // Log.d("JSwa", "Connecting to [" + url + "]");
            Document doc = Jsoup.connect(url).get();
            MyLog.debugLog("Connected to [" + url + "]");
            // Get document (HTML page) title
            String title = doc.title();
            MyLog.debugLog( "Title [" + title + "]");
          //  buffer.append("Title: " + title + "\r\n");

            Elements links = doc.select("#english a");
            MyLog.debugLog("Total a:"+links.size());

            for (Element link : links) {
                String href = link.attr("href");
                // Or if you want to have absolute URL instead, so that you can leech them.
                String absUrl = link.absUrl("href");
                absUrl = absUrl.replace(" ", "%20");
                absUrl = absUrl.replace(" ", "");
                Log.d("news:", "absUrl:" + absUrl);

                if (!daoAirNews.checkNewsStatus(absUrl))
                    startVideoDownload(absUrl, getNewsName(absUrl));



            }

        } catch (Throwable t) {
            t.printStackTrace();
        }


    }


    String getDayName(int day) {
        String dayName="";
        switch (day) {
            case Calendar.MONDAY:
                dayName= "Monday";
            break;
            case Calendar.TUESDAY:
                dayName= "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayName= "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayName= "Thursday";
                break;
            case Calendar.FRIDAY:
                dayName= "Friday";
                break;
            case Calendar.SATURDAY:
                dayName= "Saturday";
                break;
            case Calendar.SUNDAY:
                dayName= "Sunday";
                break;

        }
        return dayName;
    }

    String getNewsName(String url) {
String newsName="";
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int day = mCalendar.get(Calendar.DAY_OF_WEEK);
        String dayName = "";

        if (url.contains(Constants.ENGLISH_MORNING)) {
            if (hour > 0 && hour < 9) {//Means news was not downloaded yesterday
                dayName = getDayName(day - 1);
            } else {//Todays news
                dayName = getDayName(day);
            }
            if(dayName=="")
                return "";
            newsName= Constants.MORNING_NEWS + dayName + Constants.FileExtension;
        } else if (url.contains(Constants.ENGLISH_MIDDAY)) {
            if (hour > 0 && hour < 14) {//Means news was not downloaded yesterday
                dayName = getDayName(day - 1);
            } else {
                dayName = getDayName(day);//Todays news
            }
            if(dayName=="")
                return "";

            newsName= Constants.MIDDAY_NEWS + dayName + Constants.FileExtension;
        } else if (url.contains(Constants.NINE_BULLITEINS)) {
            if (hour > 0 && hour < 20) {//Means news was not downloaded yesterday
                dayName = getDayName(day - 1);
            } else {
                dayName = getDayName(day);//Todays news
            }
            if(dayName=="")
                return "";

            newsName= Constants.NIGHT_NEWS + dayName + Constants.FileExtension;
        }
        return newsName;
    }

    void startVideoDownload(String url, String fileName) {
        if (fileName == "")
            return;

        DAOAirNews daoAirNews = new DAOAirNews(getApplicationContext());
        AirNews airNews = new AirNews();
        airNews.setIsReady(0);
        airNews.setCreated(getTimeStamp());
        airNews.setName(fileName);
        airNews.setUrl(url);


        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        setExternalPath(airNews.getName());
        request.setDestinationUri(external_path);
        //TODO need to enable this line
        long newid = dm.enqueue(request);
        //long newid=1234;
        if (enqueue == null)
            enqueue = new ArrayList<Long>();
        enqueue.add(newid);

        airNews.setDownloadId(newid);
        daoAirNews.saveAirNews(airNews);

    }

    private String getTimeStamp() {
        SimpleDateFormat df = new SimpleDateFormat("cc,dd-MM HH:mm");

        return df.format(mCalendar.getTime());
    }


    void setExternalPath(String fileName) {
        String local_file_url1 = "";
        try {
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path, "AirNews");
            if (file.exists() && file.isDirectory()) {
                // VU.deleteRecursive(file);
                file.mkdirs();
                local_file_url1 = file.getAbsolutePath();

            } else {
                if (file.mkdirs()) {
                    local_file_url1 = file.getAbsolutePath();
                }
            }
        } catch (Exception e) {

        }

        File root = new File(local_file_url1, fileName);

        if(root.exists()){
            MyLog.debugLog("Deleting existing file");
            root.delete();
        }

        external_path = Uri.fromFile(root);
        MyLog.debugLog("File will be saved on :" + external_path.toString());


    }


   /* void saveData(ArrayList<Feed> data,int website){
        ArrayList<ContentValues> cvList=new ArrayList<ContentValues>();
        if(data.size()==0)
            return;
        for(int i=data.size()-1;i>=0;i--){


            ContentValues values=new ContentValues();

            values.put(FeedDataProvider.COLUMN2, data.get(i).title);
            values.put(FeedDataProvider.COLUMN3, data.get(i).link);
            values.put(FeedDataProvider.COLUMN4, website);
            cvList.add(values);


        }

        ContentValues[] cvArray = new ContentValues[cvList.size()];
        cvArray = cvList.toArray(cvArray);
        MyLog.debugLog("Array Length:"+cvArray.length);

        int count= getApplicationContext().getContentResolver().bulkInsert(FeedDataProvider.CONTENT_URI, cvArray);
        MyLog.debugLog("inserted records:"+count);
    }*/

    void sendNotification(String[] feed, Context context, int website) {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // define sound URI, the sound to be played when there's a notification

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("My Robot reporting");

        builder.setSound(soundUri);

        builder.setContentText(feed[0]);
        if (website == Constants.STACK_OVERFLOW) {
            builder.setSmallIcon(R.drawable.stackoverflow);
            builder.setContentTitle("StackOverflow discussion");
        } else if (website == Constants.ANDROID_DEVELOPER) {
            builder.setSmallIcon(R.drawable.android_developer);
            builder.setContentTitle("Android Developer's Blog Post");
        }
        //builder.setContentTitle("Android Feed");
        builder.setAutoCancel(true);

        String feedString = Arrays.toString(feed);

        feedString = feedString.substring(1, feedString.length() - 1);

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(feedString));
        builder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(website, builder.build());

        //builder.setSmallIcon(R.id.play);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.debugLog("Service Destroyed");
    }
}
