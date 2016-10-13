package com.zzm.zzmlibrary.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;

/**
 * Created by itzhong on 2016/10/13.
 */
public class DialogUtils {

    private static AlertDialog.Builder builder;
    private static ProgressDialog progressDialog;

    public static AlertDialog.Builder makeDefault(Activity activity){
        if(builder==null){
            builder = new AlertDialog.Builder(activity);
        }
        builder.setTitle("");
        builder.setMessage("");
        builder.setPositiveButton("",null);
        builder.setNegativeButton("",null);
        builder.setCancelable(false);
        return builder;
    }

    public static void dismissDefault(){
        if(builder!=null){
            builder.create().dismiss();
        }
    }

    public static ProgressDialog makeLoading(Activity activity){
        if(progressDialog==null){
            progressDialog = new ProgressDialog(activity);
        }
        progressDialog.setTitle("");
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(true);
        return progressDialog;
    }


    public static void disDialog(){
        if(builder!=null) {
            builder = null;
        }
        if(progressDialog!=null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
