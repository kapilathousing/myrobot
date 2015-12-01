package com.kapil.robot.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kapil.robot.util.Misc;
import com.kapil.robot.util.MyLog;


public class NetworkStatusReceiver extends BroadcastReceiver {
    public NetworkStatusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        MyLog.debugLog("Network receiver got");
        if(Misc.isConnected(context)){
            Misc.setAlarm(context);
            //disable network status receiver
            Misc.toggleComponent(context,false);
        }else{
            Misc.cancelAlarm(context);
        }

    }
}
