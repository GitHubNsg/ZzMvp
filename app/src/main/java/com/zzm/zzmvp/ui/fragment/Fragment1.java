package com.zzm.zzmvp.ui.fragment;

import android.content.Intent;

import com.zzm.zzmlibrary.ui.baseui.BaseFragment;
import com.zzm.zzmlibrary.utils.DialogUtils;
import com.zzm.zzmvp.R;
import com.zzm.zzmvp.ui.activity.WeChatActivity;
import com.zzm.zzmvp.ui.activity.WeatherActivity;


/**
 * Created by itzhong on 2016/10/12.
 */
public class Fragment1 extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_1;
    }

    @Override
    public void initView() {
        super.initView();
        getView(R.id.tvAct).setOnClickListener(v->{
            startActivity(new Intent(getActivity(), WeatherActivity.class));
        });

        getView(R.id.tvActList).setOnClickListener(v->{
            startActivity(new Intent(getActivity(), WeChatActivity.class));
        });

        getView(R.id.tvDialog).setOnClickListener(v->{
            DialogUtils.makeShare(getActivity()).show();
        });

    }

    @Override
    public String getDefaultUrl() {
        return null;
    }

    @Override
    public void setViewData(String data) {

    }
}
