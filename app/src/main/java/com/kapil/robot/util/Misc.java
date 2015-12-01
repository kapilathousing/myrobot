package com.kapil.robot.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kapil.robot.receivers.NetworkStatusReceiver;
import com.kapil.robot.services.FeedSyncService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kapilboss on 06/05/15.
 */
public class Misc {
static final String FEED_SYNC="feedsync";
    static final String PREF_NAME="SCJP_PREF";
    static  Date convertStringToDate(String input,int tag){
        if(tag==4){
           return convertIndianStringToDate(input);
        }
        //MyLog.debugLog("****converting date:"+input+" *********");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = formatter.parse(input);

        } catch (ParseException e) {
            e.printStackTrace();
        }

       // MyLog.debugLog("****After conversion date:"+formatter.format(date)+" *********");
        return  date;
    }

    static  Date convertIndianStringToDate(String indianDate){
        MyLog.debugLog("****indian date:"+indianDate+" *********");
        String input=indianDate.substring(5,25);
        MyLog.debugLog("****converting indian date:"+input+" *********");
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(input);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // MyLog.debugLog("****After conversion date:"+formatter.format(date)+" *********");
        return  date;
    }


    static boolean updateFeedSyncTime(Context context,String time,int tag){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FEED_SYNC+tag, time);
        MyLog.debugLog("Feed Sync Time Update:"+time);
        return  editor.commit();

    }

    public static Date getFeedSyncTime(Context context,int tag){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String defaultDate="2015-05-03T17:11:00Z";
        if(tag==4){
            defaultDate="Tue, 05 Nov 2015 09:05:52 +0530";
        }
       String feedTime= pref.getString(FEED_SYNC + tag, defaultDate);

        MyLog.debugLog("Feed sync time from pref:"+feedTime);

            return convertStringToDate(feedTime,tag);

    }



    public  static void  setAlarm(Context ctx){
        MyLog.debugLog("Alarm Scheduled");


        Intent messageIntent = new Intent(ctx, FeedSyncService.class);

        PendingIntent pendingIntent =
                PendingIntent.getService(ctx, 0, messageIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager msgAlarmMgr = (AlarmManager)
                ctx.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar=Calendar.getInstance();
        long triggerAtTime=calendar.getTimeInMillis() ;

        msgAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime,
                AlarmManager.INTERVAL_HOUR, pendingIntent);
    }

    public static void cancelAlarm(Context context){
        MyLog.debugLog("Alaram Cancel");
        Intent intent = new Intent(context, FeedSyncService.class);
        PendingIntent sender = PendingIntent.getService(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public static boolean isConnected(Context ctx){
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
           return  false;
        }

    }

    public static boolean toggleComponent(Context context,boolean status){
        MyLog.debugLog("Component Enabled status:"+status);
        PackageManager pm = context.getPackageManager();
        ComponentName compName =
                new ComponentName(context,
                        NetworkStatusReceiver.class);
        if(status){
            pm.setComponentEnabledSetting(
                    compName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }else{
            pm.setComponentEnabledSetting(
                    compName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }




        return  true;

    }

}
