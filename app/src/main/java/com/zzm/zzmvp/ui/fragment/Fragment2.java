package com.zzm.zzmvp.ui.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzm.zzmlibrary.ui.baseui.BaseFragment;
import com.zzm.zzmlibrary.ui.widget.SuperTextView;
import com.zzm.zzmlibrary.utils.UIUtils;
import com.zzm.zzmvp.R;
import com.zzm.zzmvp.common.receiver.ImplicitReceiver;
import com.zzm.zzmvp.common.receiver.ObviousReceiver;

/**
 * Created by itzhong on 2016/10/12.
 */
public class Fragment2 extends BaseFragment implements View.OnClickListener {

    private EditText etText;
    private TextView tvDes;
    private ObviousReceiver obviousReceiver;
    public static final String MESSAGE_ACTION1 = "com.zzm.a";
    public static final String MESSAGE_ACTION2 = "com.zzm.b";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_2;
    }

    @Override
    public void initView() {
        super.initView();
        etText = getView(R.id.etText);
        tvDes = getView(R.id.tvDes);
        getView(R.id.tvObviousBroadcast).setOnClickListener(this);
        getView(R.id.tvImplicitBroadcast).setOnClickListener(this);
        //显示广播需要注册
        IntentFilter msgfilter = new IntentFilter(MESSAGE_ACTION1);
        obviousReceiver = new ObviousReceiver();
        obviousReceiver.setText(tvDes);
        getActivity().registerReceiver(obviousReceiver, msgfilter);

        ImplicitReceiver.setTextView(tvDes);
    }



    @Override
    public String getDefaultUrl() {
        return null;
    }

    @Override
    public void setViewData(String data) {

    }

    @Override
    public void onClick(View v) {
        String msg = etText.getText().toString();
        if(TextUtils.isEmpty(msg)){
            UIUtils.showToast("请填写内容");
            return;
        }
        switch (v.getId()){
            case R.id.tvObviousBroadcast:
                Intent intent = new Intent();
                intent.setAction(MESSAGE_ACTION1);
                intent.putExtra("msg",msg);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.tvImplicitBroadcast:
                Intent intent2 = new Intent();
                intent2.setAction(MESSAGE_ACTION2);
                intent2.putExtra("msg",msg);
                getActivity().sendBroadcast(intent2);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(obviousReceiver!=null){
            getActivity().unregisterReceiver(obviousReceiver);
        }
    }
}
