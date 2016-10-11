package com.zzm.zzmlibrary.utils;

import android.content.SharedPreferences;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

/**
 * Created by itzhong on 2016/7/14.
 * 存用户信息
 * SPUtils.saveBoolean(SPUtils.IS_LOGIN,true);
 * SPUtils.getBoolean(SPUtils.IS_LOGIN,false);
 * 存应用配置信息
 * SPUtils.saveDefaultBoolean(SPUtils.IS_FIRST_IN,true);
 * SPUtils.getDefaultBoolean(SPUtils.IS_FIRST_IN,false);
 */
public class SPUtils {
    //应用默认配置SP，应用安装后保存的配置信息，一般不清除
    public static final String DEFAULT_SP_NAME = "config";
    //用户相关SP文件名，用户登录后会清空以前保存的用户相关数据
    public static final String USER_SP_NAME = "userconfig";
    private static SharedPreferences sp;
    private static SharedPreferences usersp;

    /**
     * 存String
     *
     * @param key
     * @param value
     */
    public static void saveString(String key, String value) {
        if (usersp == null)
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
        usersp.edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        if (usersp == null)
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
        return usersp.getString(key, defValue);
    }

    public static void saveDefaultString(String key, String value) {
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
        sp.edit().putString(key, value).commit();
    }

    public static String getDefaultString(String key, String defValue) {
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
        return sp.getString(key, defValue);
    }

    /**
     * 存Boolean
     *
     * @param key
     * @param value
     */
    public static void saveBoolean(String key, boolean value) {
        if (usersp == null)
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
        usersp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        if (usersp == null)
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
        return usersp.getBoolean(key, defValue);
    }

    public static void saveDefaultBoolean(String key, boolean value) {
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getDefaultBoolean(String key, boolean defValue) {
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 存Int
     *
     * @param key
     * @param value
     */
    public static void saveDefaultInt(String key, int value) {
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
        sp.edit().putInt(key, value).commit();
    }

    public static int getDefaultInt(String key, int defValue) {
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
        return sp.getInt(key, defValue);
    }

    public static void saveInt(String key, int value) {
        if (usersp == null)
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
        usersp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        if (usersp == null)
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
        return usersp.getInt(key, defValue);
    }

    /**
     * 存long
     *
     * @param key
     * @param value
     */
    public static void saveLong(String key, long value) {
        if (usersp == null)
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
        usersp.edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        if (usersp == null)
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
        return usersp.getLong(key, defValue);
    }

    public static void saveDefaultLong(String key, long value) {
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
        sp.edit().putLong(key, value).commit();
    }

    public static long getDefaultLong(String key, long defValue) {
        if (sp == null)
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
        return sp.getLong(key, defValue);
    }

    /**
     * 清空用户相关SP数据
     */
    public static void clearSpUser() {
        if (OkHttpUtils.getInstance() != null
                && OkHttpUtils.getInstance().getOkHttpClient().cookieJar() != null
                && OkHttpUtils.getInstance().getOkHttpClient().cookieJar() instanceof CookieJarImpl) {
            CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
            cookieJar.getCookieStore().removeAll();
        }
        if (usersp != null) {
            usersp.edit().clear().commit();
        } else {
            usersp = UIUtils.getContext().getSharedPreferences(USER_SP_NAME, 0);
            usersp.edit().clear().commit();
        }
    }

    /**
     * 清空默认SP数据
     */
    public static void clearSpDefault() {
        if (sp != null) {
            sp.edit().clear().commit();
        } else {
            sp = UIUtils.getContext().getSharedPreferences(DEFAULT_SP_NAME, 0);
            sp.edit().clear().commit();
        }
    }


}
