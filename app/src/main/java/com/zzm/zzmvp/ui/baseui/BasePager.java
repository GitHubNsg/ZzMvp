package com.zzm.zzmvp.ui.baseui;

import android.content.Context;
import android.view.View;

import com.zzm.zzmvp.model.BaseView;
import com.zzm.zzmvp.model.bean.BaseResponse;
import com.zzm.zzmvp.presenter.BasePresenter;


/**
 * Created by itzhong on 2016/8/2.
 */
public abstract class BasePager implements BaseView {

    public Context context;

    private View view;

    public BasePresenter presenter;

    public BasePager(Context context) {
        this.context = context;
        presenter = getPresenter();
        view = initView();
        if(isLoadDefault()){
            presenter.loadDefaultData();
        }
    }

    public boolean isLoadDefault() {
        return true;
    }

    public abstract View initView();

    public View getRootView(){
        return view;
    }

    public BasePresenter getPresenter(){
     return new BasePresenter(context,this);
    }

    @Override
    public void showLoading() {}

    @Override
    public void hideLoading() {}

    @Override
    public void noNetWork() {}

    @Override
    public void isNotTwohundred(BaseResponse response) {
    }
}
