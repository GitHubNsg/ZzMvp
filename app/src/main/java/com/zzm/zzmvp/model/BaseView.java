package com.zzm.zzmvp.model;


import com.zzm.zzmvp.model.bean.BaseResponse;

/**
 * Created by itzhong on 2016/7/14.
 */
public interface BaseView {

    /**
     * 显示loading
     */
    void showLoading();
    /**
     * 隐藏loading
     */
    void hideLoading();
    /**
     * 获取更新ui的接口url
     * @return
     */
    String getDefaultUrl();
    /**
     * 更新ui数据
     */
    void setViewData(String data);

    /**
     * 没有网络
     */
    void noNetWork();

    /**
     * 服务器异常，不是200时候的处理
     */
    void isNotTwohundred(BaseResponse response);

}
