package com.zzm.zzmvp.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;

import com.zzm.zzmlibrary.ui.baseui.BaseFragment;
import com.zzm.zzmlibrary.utils.DialogUtils;
import com.zzm.zzmlibrary.utils.UIUtils;
import com.zzm.zzmvp.R;
import com.zzm.zzmvp.ui.activity.WeChatActivity;
import com.zzm.zzmvp.ui.activity.WeatherActivity;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;

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
        ShareSDK.initSDK(getActivity());
        getView(R.id.tvAct).setOnClickListener(v->{
            startActivity(new Intent(getActivity(), WeatherActivity.class));
        });

        getView(R.id.tvActList).setOnClickListener(v->{
            startActivity(new Intent(getActivity(), WeChatActivity.class));
        });

        getView(R.id.tvDialog).setOnClickListener(v->{
            DialogUtils.makeDefault(getActivity())
                    .setTitle("提示")
                    .setMessage("提示消息")
                    .setPositiveButton("确定",(d,i)->{
                        DialogUtils.dismissDefault();
                        shareQzone();
            }).setNegativeButton("取消", (d,i)->{

            }).show();
        });

    }

    private void shareQzone(){
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle("测试分享的标题");
        sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
        sp.setText("测试分享的文本");
        sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
        sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");

        Platform qzone = ShareSDK.getPlatform (QZone.NAME);
//        qzone. setPlatformActionListener (paListener); // 设置分享事件回调
// 执行图文分享
        qzone.share(sp);
    }

    @Override
    public String getDefaultUrl() {
        return null;
    }

    @Override
    public void setViewData(String data) {

    }
}
