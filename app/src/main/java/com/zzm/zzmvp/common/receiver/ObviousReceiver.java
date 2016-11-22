package com.zzm.zzmvp.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.zzm.zzmvp.ui.fragment.Fragment2;

/**
 * Created by itzhong on 2016/11/22.
 */

public class ObviousReceiver extends BroadcastReceiver {

    private TextView text;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Fragment2.MESSAGE_ACTION1)){
            String msg = intent.getStringExtra("msg");
            if(!TextUtils.isEmpty(msg)&&text!=null){
                text.setText(msg);
            }
        }

    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }
}
