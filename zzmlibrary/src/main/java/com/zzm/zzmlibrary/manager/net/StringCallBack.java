package com.zzm.zzmlibrary.manager.net;

import android.content.Context;

import com.zhy.http.okhttp.callback.Callback;
import com.zzm.zzmlibrary.model.bean.BaseResponse;
import com.zzm.zzmlibrary.ui.baseui.BaseActivity;
import com.zzm.zzmlibrary.utils.LogUtils;
import com.zzm.zzmlibrary.utils.UIUtils;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by itzhong on 2016/7/14.
 */
public abstract class StringCallBack extends Callback<BaseResponse> {

    private Context context;

    public StringCallBack(Context context) {
        this.context = context;
    }

    @Override
    public BaseResponse parseNetworkResponse(Response response, int id) throws IOException {
        String str = response.body().string();
        LogUtils.d("OKHTTP_TAG", str);
        BaseResponse baseResponse = null;
        try {
            baseResponse = new BaseResponse(str);
            if (baseResponse == null) {
                LogUtils.e("解析失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResponse;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        e.printStackTrace();
        if (e instanceof java.net.SocketTimeoutException) {
            UIUtils.showToast("请求超时");
        }
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        if (context != null && context instanceof BaseActivity) {
            ((BaseActivity)context).hideLoading();
        }
    }

}
