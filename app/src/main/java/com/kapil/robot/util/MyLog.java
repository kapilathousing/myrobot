package com.kapil.robot.util;

import android.util.Log;

/**
 * Created by kapilboss on 06/05/15.
 */
public class MyLog {
    static boolean debug=true;
    static final String TAG="MyAPP";
    public static void debugLog(String msg){
        if(debug)
        Log.i(TAG,msg);
    }
}
