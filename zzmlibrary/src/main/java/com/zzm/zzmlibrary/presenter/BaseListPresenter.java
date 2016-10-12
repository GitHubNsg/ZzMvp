package com.zzm.zzmlibrary.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zzm.zzmlibrary.manager.net.StringCallBack;
import com.zzm.zzmlibrary.model.BaseListView;
import com.zzm.zzmlibrary.model.bean.BaseResponse;
import com.zzm.zzmlibrary.utils.LogUtils;
import com.zzm.zzmlibrary.utils.TDevice;

import java.util.List;

import okhttp3.Call;

/**
 * Created by itzhong on 2016/7/15.
 */
public class BaseListPresenter extends BasePresenter{

    public static final int PAGESIZE = 10;
    public int mPage = 1;
    public Context context;
    public int totalPage = 0;

    public BaseListPresenter(Context context, BaseListView baseListView) {
        super(context, baseListView);
        this.context=context;
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
        OkHttpUtils.get().url(url).tag(url).build().execute(new StringCallBack(context) {
            @Override
            public void onResponse(BaseResponse response, int id) {
                if(response==null||!response.isOk()||TextUtils.isEmpty(response.getData())){
                    listView.noData();
                    return;
                }
                List<?> list = listView.getList(response.getData());
                totalPage = listView.getTotalPage(response.getData());
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
        OkHttpUtils.get().url(url).tag(url).build().execute(new StringCallBack(context) {
            @Override
            public void onResponse(BaseResponse response, int id) {
                if(!response.isOk()||TextUtils.isEmpty(response.getData())){
                    return;
                }
                List<?> list = listView.getList(response.getData());
                totalPage = listView.getTotalPage(response.getData());
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
            listView.noNetWork();
            return false;
        }
        return true;
    }

    /**
     * 执行get请求
     */
    public void getRequest(String url,StringCallBack callBack){
        if(!checkUrl(url)) return;
        OkHttpUtils.get().url(url).tag(url).build().execute(callBack);
    }

}
