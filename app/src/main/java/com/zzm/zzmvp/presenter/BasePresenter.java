package com.zzm.zzmvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzm.zzmlibrary.utils.LogUtils;
import com.zzm.zzmvp.manager.net.RequestHeaders;
import com.zzm.zzmvp.manager.net.StringCallBack;
import com.zzm.zzmvp.model.BaseListView;
import com.zzm.zzmvp.model.BaseView;
import com.zzm.zzmvp.model.bean.BaseResponse;
import com.zzm.zzmvp.util.TDevice;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by itzhong on 2016/7/14.
 */
public class BasePresenter {

    public Context context;
    public BaseView view;
    public BaseListView listView;

    public BasePresenter(Context context, BaseListView baseListView) {
        this.context = context;
        this.listView = baseListView;
    }

    public BasePresenter(Context context, BaseView view) {
        this.context = context;
        this.view = view;
    }

    /**
     * 默认加载页面数据
     */
    public void loadDefaultData(){
        String url = view.getDefaultUrl();
        if(!checkUrl(url)) return;
        view.showLoading();
        OkHttpUtils.get().url(url).headers(RequestHeaders.headers()).tag(url).build().execute(new StringCallBack(context) {
            @Override
            public void onResponse(BaseResponse response, int id) {
                if(response!=null&&response.isOk()){
                    view.setViewData(response.getData());
                }else if(response!=null){
                    view.isNotTwohundred(response);
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

    /**
     * 执行get请求
     */
    public void getRequest(String url,StringCallBack callBack){
        if(!checkUrl(url)) return;
        OkHttpUtils.get().url(url).headers(RequestHeaders.headers()).tag(url).build().execute(callBack);
    }

    /**
     * 执行Post请求
     */
    public void postRequest(String url,String[][] body,StringCallBack callBack){
        if(!checkUrl(url)) return;
        HashMap<String, String> paramMap = new HashMap<>();
        for(int i=0;i<body.length;i++){
            paramMap.put(body[i][0],body[i][1]);
        }
        OkHttpUtils.postString().url(url).mediaType(MediaType.parse("application/json; charset=utf-8")).headers(RequestHeaders.headers()).tag(url).content(new Gson().toJson(paramMap)).build().execute(callBack);
    }

    /**
     * 执行Post请求
     */
    public void postRequest(String url,HashMap body,StringCallBack callBack){
        if(!checkUrl(url)) return;
        OkHttpUtils.postString().url(url).mediaType(MediaType.parse("application/json; charset=utf-8")).headers(RequestHeaders.headers()).tag(url).content(new Gson().toJson(body)).build().execute(callBack);
    }

    /**
     * 执行Post请求
     */
    public void postRequest(String url,String body,StringCallBack callBack){
        if(!checkUrl(url)) return;
        OkHttpUtils.postString().url(url).mediaType(MediaType.parse("application/json; charset=utf-8")).headers(RequestHeaders.headers()).tag(url).content(body).build().execute(callBack);
    }

    private boolean checkUrl(String url){
        if(TextUtils.isEmpty(url)){
            LogUtils.d("url为空，不加载数据");
            return false;
        }else if(!url.startsWith("http")){
            LogUtils.d("url类型错误");
            return false;
        }
        if(TDevice.getNetworkType()==0){
            LogUtils.e("网络连接异常");
            view.noNetWork();
            return false;
        }
        return true;
    }

    public void cancelLoad(){
        if(view!=null&&!TextUtils.isEmpty(view.getDefaultUrl())){
            OkHttpUtils.getInstance().cancelTag(view.getDefaultUrl());
        }
        if(listView!=null&&!TextUtils.isEmpty(listView.getDefaultUrl())){
            OkHttpUtils.getInstance().cancelTag(listView.getDefaultUrl());
        }
    }

}
