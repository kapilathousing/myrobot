package com.kapil.robot.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kapil.robot.util.Misc;


public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Misc.isConnected(context)){
         Misc.setAlarm(context);
        }else{
            //enable network status receiver
            Misc.toggleComponent(context,true);
        }


    }
}
