package com.zzm.zzmlibrary.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * 全局常量类
 */
public class Constants {

    /**
     * 是否为debug，打印log等
     */
    public static boolean APP_DBG = true;

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

    /**
     * H5页面的类型
     */
    public static final String H5_TYPE = "web_type";
    public static final String H5_URL= "web_url";
    public static final String H5_TITLE = "web_title";
    public static final String H5_CONTENT = "web_content";
    /**
     * 设备IMEI
     */
    public static final String IMEI = "imei";
}
