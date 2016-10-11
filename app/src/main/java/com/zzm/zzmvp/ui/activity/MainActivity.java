package com.zzm.zzmvp.ui.activity;

import android.view.View;

import com.zzm.zzmvp.R;
import com.zzm.zzmvp.ui.baseui.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected void initView() {
        super.initView();
        mLeftButton.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String getDefaultUrl() {
        return null;
    }

    @Override
    public void setViewData(String data) {

    }



}
