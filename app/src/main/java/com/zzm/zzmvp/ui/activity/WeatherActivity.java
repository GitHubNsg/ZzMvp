package com.zzm.zzmvp.ui.activity;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.zzm.zzmlibrary.presenter.BasePresenter;
import com.zzm.zzmlibrary.presenter.BaseStringPresenter;
import com.zzm.zzmlibrary.ui.baseui.BaseActivity;
import com.zzm.zzmlibrary.utils.GsonTools;
import com.zzm.zzmvp.R;
import com.zzm.zzmvp.model.WeatherBean;

/**
 * Created by itzhong on 2016/10/13.
 */
public class WeatherActivity extends BaseActivity{

    private TextView tvCity;
    private TextView tvAir;
    private TextView tvOther;
    private String key = "17db5425c5c90";
    private String url = "http://apicloud.mob.com/v1/weather/query";
    private String city = "";
    private String pro = "";

    @Override
    protected void initView() {
        mTvTitle.setText("天气预报示例");
        tvCity = getView(R.id.tvCity);
        tvAir = getView(R.id.tvAir);
        tvOther = getView(R.id.tvOther);
//        tvOther.setMovementMethod(ScrollingMovementMethod.getInstance());
        city = "南京";
        pro = "江苏";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public String getDefaultUrl() {
        return url + "?key="+key+"&city="+city+"&province="+pro;
    }

    @Override
    public void setViewData(String data) {
        WeatherBean weatherBean = GsonTools.changeGsonToBean(data, WeatherBean.class);
        tvCity.setText("城市："+weatherBean.getResult().get(0).getCity());
        tvAir.setText("空气质量："+weatherBean.getResult().get(0).getAirCondition());
        tvOther.setText("请求结果："+data);
    }

    @Override
    public BasePresenter getPresenter() {
        return new BaseStringPresenter(this,this);
    }
}
