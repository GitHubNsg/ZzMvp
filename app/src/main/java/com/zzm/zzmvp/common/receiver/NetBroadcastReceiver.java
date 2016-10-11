package com.zzm.zzmvp.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zzm.zzmvp.common.BaseApplication;
import com.zzm.zzmvp.ui.baseui.BaseActivity;
import com.zzm.zzmvp.util.TDevice;


/**
 * Created by itzhong on 2016/7/26.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NET_CHANGE_ACTION)) {
            BaseApplication.mNetWorkState = TDevice.getNetworkType();
            if(BaseActivity.netListener!=null){
                BaseActivity.netListener.onNetChange(BaseApplication.mNetWorkState);
            }
        }
    }

}
