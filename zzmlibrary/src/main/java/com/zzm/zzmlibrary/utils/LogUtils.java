package com.zzm.zzmlibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;



/**
 * Created by itzhong on 2016/7/14.
 * 统一的log日志
 */
public class LogUtils {

    private static final String TAG = "ZZM";

    public static boolean APP_DBG = true; // 是否是debug模式  

    public static void init(Context context){
        APP_DBG = isApkDebugable(context);
    }

    /**
     * 但是当我们没在AndroidManifest.xml中设置其debug属性时: 
     * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false. 
     * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug. 
     * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值. 
     *
     * @param context
     * @return
     * @author SHANHY
     * @date   2015-8-7 
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void v( String message) {
        if(APP_DBG) {
            Log.v(TAG, message);
        }
    }

    public static void d( String message) {
        if(APP_DBG) {
            Log.d(TAG, message);
        }
    }

    public static void i( String message) {
        if(APP_DBG) {
            Log.i(TAG, message);
        }
    }

    public static void w( String message) {
        if(APP_DBG) {
            Log.w(TAG, message);
        }
    }

    public static void e(String message) {
        if(APP_DBG) {
            Log.e(TAG, message);
        }
    }

    public static void e(String message, Exception e) {
        if(APP_DBG) {
            Log.e(TAG, message, e);
        }
    }

    public static void v(String tag, String message) {
        if(APP_DBG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if(APP_DBG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if(APP_DBG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if(APP_DBG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(APP_DBG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if(APP_DBG) {
            Log.e(tag, message, e);
        }
    }
}
