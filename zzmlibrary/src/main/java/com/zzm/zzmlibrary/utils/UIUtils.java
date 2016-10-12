package com.zzm.zzmlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import com.zzm.zzmlibrary.common.BaseApplication;


/**
 * Created by itzhong on 2016/7/14.
 * UI工具类
 */
public class UIUtils {

    private static Toast mToast;

    /**
     * 吐司
     * @param stringId
     */
    public static void showToast(final int stringId) {
        if ("main".equals(Thread.currentThread().getName())) {
            if (mToast != null)
                mToast.setText(stringId);
            else {
                mToast = Toast.makeText(getContext(), stringId, Toast.LENGTH_SHORT);
            }
            mToast.show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast != null)
                        mToast.setText(stringId);
                    else {
                        mToast = Toast.makeText(getContext(), stringId, Toast.LENGTH_SHORT);
                    }
                    mToast.show();
                }
            });
        }
    }

    /**
     * 吐司
     * @param str
     */
    public static void showToast(final String str) {
        if ("main".equals(Thread.currentThread().getName())) {
            if (mToast != null)
                mToast.setText(str);
            else {
                mToast = Toast.makeText(getContext(), str, Toast.LENGTH_SHORT);
            }
            mToast.show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast != null)
                        mToast.setText(str);
                    else {
                        mToast = Toast.makeText(getContext(), str, Toast.LENGTH_SHORT);
                    }
                    mToast.show();
                }
            });
        }
    }

    /**
     * 在主程序执行一段逻辑
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (android.os.Process.myTid() == BaseApplication.MainThreadId) {
            runnable.run();
        } else {
            Handler handler = BaseApplication.getHandler();
            handler.post(runnable);
        }
    }

    /**
     * 获得资源
     * @return
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 获得上下文
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getInstance();
    }

    /**
     * 创建一个布局
     * @param resource
     * @return
     */
    public static View createView(int resource) {
        return View.inflate(getContext(), resource, null);
    }

    /**
     * 获得string
     * @param id
     * @return
     */
    public static String getString(int id) {
        return getResource().getString(id);
    }

    /**
     * 获得string数组
     * @param id
     * @return
     */
    public static String[] getStringArray(int id) {
        return getResource().getStringArray(id);
    }


    /**
     * 检测是否拥有该权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermissions(Context context,String permission){
        if(ContextCompat.checkSelfPermission(context,permission) !=
                PackageManager.PERMISSION_GRANTED){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 父亲移除孩子
     * @param view
     */
    public static void removeChild(View view){
        ViewParent parent = view.getParent();
        if(parent!=null){
            ViewGroup group = (ViewGroup) parent;
            group.removeView(view);
        }
    }

    /**
     * 获取view的孩子
     * @param layoutView
     * @param resId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(View layoutView, @IdRes int resId) {
        return (T) layoutView.findViewById(resId);
    }

    /**
     * 获取actionBar的高度
     * @return
     */
    public static int getTitleBarHeight(){
        TypedArray actionbarSizeTypedArray = getContext().obtainStyledAttributes(new int[] {
                android.R.attr.actionBarSize});
        return (int)actionbarSizeTypedArray.getDimension(0, 0);
    }

    /**
     * 得到文字的高度
     * @param textView
     * @return
     */
    public static int getTextHeight(TextView textView){
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textView.getTextSize());
        textPaint.setColor(Color.WHITE);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fTop = fontMetrics.top;
        float fBottom = fontMetrics.bottom;
        return (int) (fBottom - fTop);
    }

    /**
     * 当要使用权限却被拒绝时提示跳到设置页面允许权限
     * @param context
     */
    /*public static void openPermissionSetting(Context context){
        DialogUtils.makeDefault(context)
                .setTitle("提示")
                .setMessage("当前应用缺少必要权限\n打开应用进行权限设置")
                .setLeftStr("确定")
                .setRightStr("取消")
                .setLeftBtListener(v->{
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package",getContext().getPackageName(), null));
                    context.startActivity(intent);
                    DialogUtils.makeDefault(context).dismiss();
                })
                .setRightBtListener(null)
                .show();
    }*/

}
