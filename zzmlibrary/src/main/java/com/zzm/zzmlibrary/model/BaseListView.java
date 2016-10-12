package com.zzm.zzmlibrary.model;


import com.zzm.zzmlibrary.ui.adapter.BaseQuickAdapter;

import java.util.List;

/**
 * Created by itzhong on 2016/7/15.
 */
public interface BaseListView  {

    /**
     * 显示loading
     */
    void showLoading();
    /**
     * 隐藏loading
     */
    void hideLoading();
    /**
     * 获取更新ui的接口
     * @return
     */
    String getDefaultUrl();
    /**
     * 获取list
     * @return
     */
    List<?> getList(String data);

    /**
     * 刷新数据
     */
    void onRefreshView(List<?> list);

    /**
     * 加载更多
     */
    void loadMore(List<?> list);

    /**
     * 得到adapter
     * @return
     */
    BaseQuickAdapter getAdapter();

    /**
     * 停止刷新
     */
    void stopRefreshView();

    /**
     * 没有更多数据
     */
    void noMoreData();

    /**
     * 没有数据
     */
    void noData();

    /**
     * 没有网络
     */
    void noNetWork();
    /**
     * 获取总页数
     */
    int getTotalPage(String data);
}
