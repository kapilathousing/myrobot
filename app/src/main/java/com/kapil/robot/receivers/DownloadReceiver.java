package com.kapil.robot.receivers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kapil.robot.db.dao.DAOAirNews;
import com.kapil.robot.util.MyLog;

public class DownloadReceiver extends BroadcastReceiver {
    public DownloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            /*if(enqueue.contains(downloadId)){
                enqueue.remove((Long)downloadId);
                if(enqueue.size()==0)unregisterReceiver(receiver);
            }*/
            MyLog.debugLog("Download finished:"+downloadId);
            DAOAirNews daoAirNews = new DAOAirNews(context);
            daoAirNews.updateDownloadStatus(downloadId);

        }
    }
}
