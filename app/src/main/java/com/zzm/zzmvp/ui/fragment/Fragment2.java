package com.zzm.zzmvp.ui.fragment;

import android.view.View;

import com.zzm.zzmlibrary.ui.baseui.BaseFragment;
import com.zzm.zzmvp.R;

/**
 * Created by itzhong on 2016/10/12.
 */
public class Fragment2 extends BaseFragment implements View.OnClickListener {



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_2;
    }

    @Override
    public void initView() {
        super.initView();
    }



    @Override
    public String getDefaultUrl() {
        return null;
    }

    @Override
    public void setViewData(String data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }


}
