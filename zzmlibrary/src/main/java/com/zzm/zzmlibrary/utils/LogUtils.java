package com.zzm.zzmlibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.zzm.zzmlibrary.common.Constants;


/**
 * Created by itzhong on 2016/7/14.
 * 统一的log日志
 */
public class LogUtils {

    private static final String TAG = "ZZM";

    public static void v( String message) {
        if(Constants.APP_DBG) {
            Log.v(TAG, message);
        }
    }

    public static void d( String message) {
        if(Constants.APP_DBG) {
            Log.d(TAG, message);
        }
    }

    public static void i( String message) {
        if(Constants.APP_DBG) {
            Log.i(TAG, message);
        }
    }

    public static void w( String message) {
        if(Constants.APP_DBG) {
            Log.w(TAG, message);
        }
    }

    public static void e(String message) {
        if(Constants.APP_DBG) {
            Log.e(TAG, message);
        }
    }

    public static void e(String message, Exception e) {
        if(Constants.APP_DBG) {
            Log.e(TAG, message, e);
        }
    }

    public static void v(String tag, String message) {
        if(Constants.APP_DBG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if(Constants.APP_DBG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if(Constants.APP_DBG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if(Constants.APP_DBG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(Constants.APP_DBG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if(Constants.APP_DBG) {
            Log.e(tag, message, e);
        }
    }
}
