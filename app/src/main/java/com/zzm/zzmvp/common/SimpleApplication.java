package com.zzm.zzmvp.common;

import android.content.Intent;
import android.graphics.Color;

import com.zzm.zzmlibrary.common.BaseApplication;
import com.zzm.zzmlibrary.common.Constants;
import com.zzm.zzmlibrary.ui.baseui.BaseActivity;
import com.zzm.zzmvp.ui.activity.MainActivity;


/**
 * 全局上下文,初始化程序和第三方SDK的配置，抓取崩溃信息
 */
public class SimpleApplication extends BaseApplication {

    @Override
    protected void initResource() {
        titleBarTextColor = Color.parseColor("#ffffff");
        titleBarBackgroundColor = Color.parseColor("#ee5e5e");
    }

    @Override
    protected void dealWrongText(Throwable ex) {
        if (!Constants.APP_DBG && BaseActivity.activity != null
                && !(BaseActivity.activity instanceof MainActivity)) {
            Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.getInstance().startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

}
