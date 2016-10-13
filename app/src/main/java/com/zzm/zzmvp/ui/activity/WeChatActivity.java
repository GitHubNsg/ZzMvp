package com.zzm.zzmvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zzm.zzmlibrary.presenter.BaseListStringPresenter;
import com.zzm.zzmlibrary.presenter.BasePresenter;
import com.zzm.zzmlibrary.ui.adapter.BaseQuickAdapter;
import com.zzm.zzmlibrary.ui.adapter.BaseViewHolder;
import com.zzm.zzmlibrary.ui.baseui.BaseListActivity;
import com.zzm.zzmlibrary.utils.GsonTools;
import com.zzm.zzmvp.R;
import com.zzm.zzmvp.model.WechatBean;

import java.util.List;

/**
 * Created by itzhong on 2016/10/13.
 */
public class WeChatActivity extends BaseListActivity{

    private String key = "17db5425c5c90";
    private String url = "http://apicloud.mob.com/wx/article/search";

    @Override
    protected void initView() {
        super.initView();
        mTvTitle.setText("微信精选");

    }

    @Override
    public List<?> getList(String data) {
        try{
            return GsonTools.changeGsonToBean(data,WechatBean.class).getResult().getList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return new WechatAdapter();
    }

    @Override
    public int getTotalPage(String data) {
        try{
            int total = GsonTools.changeGsonToBean(data, WechatBean.class).getResult().getTotal();
            int tpage = total/listPresenter.PAGESIZE;
            int m = total%listPresenter.PAGESIZE;
            if(m==0){
                return tpage;
            }else{
                return tpage + 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String getDefaultUrl() {
        return url+"?key="+key+"&cid=24"+"&"+listPresenter.mPage+"&"+listPresenter.PAGESIZE;
    }

    @Override
    public void onItemClick(View var1, int var2) {
        WechatBean.ResultBean.ListBean b = (WechatBean.ResultBean.ListBean) adapter.getData().get(var2);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(b.getSourceUrl()));
        startActivity(intent);
    }

    @Override
    public BasePresenter getPresenter() {
        return new BaseListStringPresenter(this,this);
    }

    private class WechatAdapter extends BaseQuickAdapter<WechatBean.ResultBean.ListBean>{

        public WechatAdapter() {
            super(R.layout.item_wechat, null);
        }

        @Override
        protected void convert(BaseViewHolder var1, WechatBean.ResultBean.ListBean var2) {

            var1.setText(R.id.tvTitle,var2.getTitle())
                    .setText(R.id.tvDes,var2.getSubTitle());
            ImageView ivImg = var1.getView(R.id.ivImg);
            Glide.with(WeChatActivity.this).load(var2.getThumbnails()).into(ivImg);

        }
    }

}
