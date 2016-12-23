package com.zzm.zzmvp.ui.activity;

import android.widget.EditText;

import com.zhy.autolayout.AutoLinearLayout;
import com.zzm.zzmlibrary.ui.baseui.BaseActivity;
import com.zzm.zzmlibrary.ui.widget.ToggleButton;
import com.zzm.zzmvp.R;

/**
 * Created by itzhong on 2016/11/29.
 */

public class InterfaceTestActivity extends BaseActivity {


    private AutoLinearLayout layoutHeaders,layoutValues;
    private EditText etUrl;
    private ToggleButton mTb1, mTb3;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_interface_test;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvTitle.setText("本地接口测试,还没写完");
        etUrl = getView(R.id.etUrl);
        mTb1 = getView(R.id.mTb1);
        mTb3 = getView(R.id.mTb3);
        layoutHeaders = getView(R.id.layoutHeaders);
        layoutValues = getView(R.id.layoutValues);


    }

    @Override
    public String getDefaultUrl() {
        return null;
    }

    @Override
    public void setViewData(String data) {

    }
}
