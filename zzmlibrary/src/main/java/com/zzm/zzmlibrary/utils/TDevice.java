package com.zzm.zzmlibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.zzm.zzmlibrary.common.BaseApplication;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TDevice {

    // 手机网络类型
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static boolean GTE_HC;
    public static boolean GTE_ICS;
    public static boolean PRE_HC;
    private static Boolean _hasBigScreen = null;
    private static Boolean _hasCamera = null;
    private static Boolean _isTablet = null;
    private static Integer _loadFactor = null;

    private static int _pageSize = -1;
    public static float displayDensity = 0.0F;

    static {
        GTE_ICS = Build.VERSION.SDK_INT >= 14;
        GTE_HC = Build.VERSION.SDK_INT >= 11;
        PRE_HC = Build.VERSION.SDK_INT < 11;
    }

    public TDevice() {
    }

    public static float dpToPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160F);
    }

    public static float pixelsToDp(float f) {
        return f / (getDisplayMetrics().densityDpi / 160F);
    }

    public static int getDefaultLoadFactor() {
        if (_loadFactor == null) {
            Integer integer = Integer.valueOf(0xf & BaseApplication.getInstance()
                    .getResources().getConfiguration().screenLayout);
            _loadFactor = integer;
            _loadFactor = Integer.valueOf(Math.max(integer.intValue(), 1));
        }
        return _loadFactor.intValue();
    }

    public static float getDensity() {
        if (displayDensity == 0.0)
            displayDensity = getDisplayMetrics().density;
        return displayDensity;
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) BaseApplication.getInstance().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightInt() {
        return getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidthInt() {
        return getDisplayMetrics().widthPixels;
    }

    public static int[] getRealScreenSize(Activity activity) {
        int[] size = new int[2];
        int screenWidth = 0, screenHeight = 0;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                screenWidth = (Integer) Display.class.getMethod("getRawWidth")
                        .invoke(d);
                screenHeight = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d,
                        realSize);
                screenWidth = realSize.x;
                screenHeight = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
    }


    public static final boolean hasCamera() {
        if (_hasCamera == null) {
            PackageManager pckMgr = BaseApplication.getInstance()
                    .getPackageManager();
            boolean flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front");
            boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
            boolean flag2;
            flag2 = flag || flag1;
            _hasCamera = Boolean.valueOf(flag2);
        }
        return _hasCamera.booleanValue();
    }


    public static void gotoMarket(Context context, String pck) {
        if (!isHaveMarket(context)) {
            UIUtils.showToast("你手机中没有安装应用市场！");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }

    /**
     * 打开应用市场
     * @param context
     */
    public static void openAppInMarket(Context context) {
        if (context != null) {
            String pckName = context.getPackageName();
            try {
                gotoMarket(context, pckName);
            } catch (Exception ex) {
                try {
                    String otherMarketUri = "http://market.android.com/details?id="
                            + pckName;
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(otherMarketUri));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    UIUtils.showToast("你手机中没有安装应用市场！");
                }
            }
        }
    }

    public static void setFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void cancelFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(params);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static PackageInfo getPackageInfo(String pckName) {
        try {
            return BaseApplication.getInstance().getPackageManager()
                    .getPackageInfo(pckName, 0);
        } catch (NameNotFoundException e) {
            LogUtils.e(e.getMessage());
        }
        return null;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = BaseApplication
                    .getInstance()
                    .getPackageManager()
                    .getPackageInfo(BaseApplication.getInstance().getPackageName(),
                            0).versionCode;
        } catch (NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    public static String getVersionName() {
        String name = "";
        try {
            name = BaseApplication
                    .getInstance()
                    .getPackageManager()
                    .getPackageInfo(BaseApplication.getInstance().getPackageName(),
                            0).versionName;
        } catch (NameNotFoundException ex) {
            name = "";
        }
        return name;
    }

    /**
     * 安装apk文件
     * @param var1
     * @param var0
     */
    public static void installAPK(Context var1, File var0) {
        if (var0 == null || !var0.exists())
            return;
        Intent var2 = new Intent();
        var2.addFlags(268435456);
        var2.setAction("android.intent.action.VIEW");
        String var3 = getMIMEType(var0);
        var2.setDataAndType(Uri.fromFile(var0), var3);
        try {
            var1.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    public static void openCamera(Context context) {
        Intent intent = new Intent(); // 调用照相机
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        intent.setFlags(0x34c40000);
        context.startActivity(intent);
    }

    public static String getIMEI() {
        String imei = "";
        TelephonyManager tel = (TelephonyManager) BaseApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        imei = tel.getDeviceId();
        return imei;
    }

    /**
     * 生成伪IMEI
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getPhoneType() {
        return Build.MODEL;
    }

    /**
     * 是否打开了WiFi
     * @return
     */
    public static boolean isWifiOpen() {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager) BaseApplication
                .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        // check the networkInfos numbers
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    /**
     * 获取statusbar的高度
     * @return
     */
    public static int getStatuBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;// 默认为38，貌似大部分是这样的
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = BaseApplication.getInstance().getResources()
                    .getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        return (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication
                .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
}
