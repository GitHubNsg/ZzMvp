package com.zzm.zzmvp.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.zzm.zzmlibrary.utils.UIUtils;

/**
 * Created by itzhong on 2016/11/22.
 */

public class ImplicitReceiver extends BroadcastReceiver {

    public static TextView textView;

    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = intent.getStringExtra("msg");
        if(!TextUtils.isEmpty(msg)&&textView!=null){
            textView.setText(msg);
        }

    }

    public static TextView getTextView() {
        return textView;
    }

    public static void setTextView(TextView textView) {
        ImplicitReceiver.textView = textView;
    }
}
