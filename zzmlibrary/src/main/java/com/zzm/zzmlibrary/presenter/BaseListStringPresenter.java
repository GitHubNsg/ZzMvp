package com.zzm.zzmlibrary.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzm.zzmlibrary.manager.net.DefaultStringCallBack;
import com.zzm.zzmlibrary.model.BaseListView;

import java.util.List;

import okhttp3.Call;

/**
 * Created by itzhong on 2016/7/15.
 */
public class BaseListStringPresenter extends BaseListPresenter{


    public BaseListStringPresenter(Context context, BaseListView baseListView) {
        super(context, baseListView);
    }

    /**
     * 重写父类默认加载方法改为刷新
     */
    @Override
    public void loadDefaultData() {
        mPage = 1;
        String url = listView.getDefaultUrl();
        if(!checkUrl(url)){
            listView.hideLoading();
            listView.stopRefreshView();
            return;
        }
        listView.showLoading();
        OkHttpUtils.get().url(url).tag(url).build().execute(new DefaultStringCallBack(context) {
            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    listView.noData();
                    return;
                }
                List<?> list = listView.getList(response);
                totalPage = listView.getTotalPage(response);
                if(list!=null&&list.size()>0){
                    mPage ++;
                    listView.onRefreshView(list);
                }else{
                    listView.noData();
                }
            }
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                listView.noNetWork();
            }
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                listView.hideLoading();
                listView.stopRefreshView();
            }
        });
    }

    /**
     * 加载更多
     */
    public void loadDataMore(){
        String url = listView.getDefaultUrl();
        if(!checkUrl(url))return;
        if(totalPage==0||mPage>totalPage)return;
        listView.showLoading();
        OkHttpUtils.get().url(url).tag(url).build().execute(new DefaultStringCallBack(context) {
            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                List<?> list = listView.getList(response);
                totalPage = listView.getTotalPage(response);
                if(list!=null&&list.size()>0){
                    mPage ++;
                    listView.loadMore(list);
                }else{
                    listView.noMoreData();
                }
            }
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                listView.noNetWork();
            }
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                listView.hideLoading();
                listView.stopRefreshView();
            }
        });
    }

}
