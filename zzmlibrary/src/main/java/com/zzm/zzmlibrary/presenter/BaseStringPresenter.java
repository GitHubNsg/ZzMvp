package com.zzm.zzmlibrary.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzm.zzmlibrary.manager.net.DefaultStringCallBack;
import com.zzm.zzmlibrary.manager.net.StringCallBack;
import com.zzm.zzmlibrary.model.BaseListView;
import com.zzm.zzmlibrary.model.BaseView;
import com.zzm.zzmlibrary.model.bean.BaseResponse;

import okhttp3.Call;

/**
 * Created by itzhong on 2016/10/13.
 */
public class BaseStringPresenter extends BasePresenter {
    public BaseStringPresenter(Context context, BaseListView baseListView) {
        super(context, baseListView);
    }

    public BaseStringPresenter(Context context, BaseView view) {
        super(context, view);
    }

    @Override
    public void loadDefaultData() {
        String url = view.getDefaultUrl();
        if(!checkUrl(url)) return;
        view.showLoading();
        OkHttpUtils.get().url(url).tag(url).build().execute(new DefaultStringCallBack(context) {
            @Override
            public void onResponse(String response, int id) {
                if(!TextUtils.isEmpty(response)){
                    view.setViewData(response);
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                view.noNetWork();
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                if(view!=null){
                    view.hideLoading();
                }
            }
        });
    }
}
