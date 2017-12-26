package com.jarvis.tbaseviewlib.utils;

import android.util.Log;

/**
 * Created by tansheng on 2017/7/5.
 */

public class LogUtils {
    private static boolean isDebug=true;
    private static String tag="TBaseView";

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static void e(String log){
        if (isDebug){
            Log.e(tag,log);
        }
    }
    public static void i(String log){
        if (isDebug){
            Log.i(tag,log);
        }
    }
    public static void d(String log){
        if (isDebug){
            Log.d(tag,log);
        }
    }
    public static void v(String log){
        if (isDebug){
            Log.v(tag,log);
        }
    }
    public static void w(String log){
        if (isDebug){
            Log.w(tag,log);
        }
    }

}
