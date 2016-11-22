package com.zzm.zzmlibrary.ui.widget;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zzm.zzmlibrary.R;
import com.zzm.zzmlibrary.utils.DialogUtils;
import com.zzm.zzmlibrary.utils.UIUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private static ShareDialog shareDialog;

    private String title;

    private String text;

    private String targetUrl;

    private String imgUrl;

    private Bitmap image;

    private PlatformActionListener mListener;

    private String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    private ShareDialog(Context context) {
        super(context, R.style.dialog_normal);
        this.context = context;
        initDialog();
    }

    public static synchronized ShareDialog getInstance(Context context){
        if(shareDialog==null){
            synchronized (ShareDialog.class) {
                if (shareDialog == null) {
                    shareDialog = new ShareDialog(context);
                }
            }
        }
        return shareDialog;
    }

    public static void destroyDialog(){
        if(shareDialog != null){
            if(shareDialog.isShowing()){
                shareDialog.dismiss();
            }
            shareDialog.cancel();
            shareDialog = null;
        }
    }

    private void initDialog() {
        View view = UIUtils.createView(R.layout.layout_share);
        view.findViewById(R.id.share_wechat).setOnClickListener(this);
        view.findViewById(R.id.share_wechatm).setOnClickListener(this);
        view.findViewById(R.id.share_qq).setOnClickListener(this);
        view.findViewById(R.id.share_sina).setOnClickListener(this);
        view.findViewById(R.id.tv_share_cancel).setOnClickListener(this);
        this.setContentView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.dialog_animstyle);
        WindowManager.LayoutParams lp  = window.getAttributes();
        lp.width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        window.setAttributes(lp);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        ShareSDK.initSDK(context);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.tv_share_cancel){
            UIUtils.runOnUiThread(()->this.dismiss());
        }else if(id == R.id.share_wechat){
            setShareContent(Wechat.NAME);
        }else if(id == R.id.share_wechatm){
            setShareContent(WechatMoments.NAME);
        }else if(id == R.id.share_qq){
            setShareContent(QQ.NAME);
        }else if(id == R.id.share_sina){
            setShareContent(SinaWeibo.NAME);
        }
    }

    private void setShareContent(String name) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle("测试分享的标题");
        sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
        sp.setText("测试分享的文本");
        sp.setImageUrl("http://i.zeze.com/attachment/forum/201606/04/150438y6azrzlis6244gba.jpg");
        sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");
//        sp.setImageData();
        Platform platform = ShareSDK.getPlatform (name);
        platform.setPlatformActionListener(getmListener()); // 设置分享事件回调
        platform.share(sp);
        UIUtils.runOnUiThread(()->this.dismiss());
    }

    private PlatformActionListener listener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            UIUtils.showToast("分享成功了");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            UIUtils.showToast("分享失败了");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            UIUtils.showToast("分享取消了");
        }
    };


    public PlatformActionListener getmListener() {
        if(mListener == null){
            return listener;
        }
        return mListener;
    }

    public ShareDialog setmListener(PlatformActionListener mListener) {
        this.mListener = mListener;
        return shareDialog;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ShareDialog setImgUrl(String imgUrl) {
        if(!TextUtils.isEmpty(imgUrl)){
            Glide.with(context).load(imgUrl).asBitmap().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    image = resource;
                    return false;
                }
            });
        }
        this.imgUrl = imgUrl;
        return shareDialog;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public ShareDialog setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
        return shareDialog;
    }

    public String getText() {
        return text;
    }

    public ShareDialog setText(String text) {
        this.text = text;
        return shareDialog;
    }

    public String getTitle() {
        return title;
    }

    public ShareDialog setTitle(String title) {
        this.title = title;
        return shareDialog;
    }

    public Bitmap getImage() {
        if(TextUtils.isEmpty(imgUrl)||image==null){
            image = BitmapFactory.decodeResource(UIUtils.getResource(), R.drawable.ic_launcher);
        }
        return image;
    }

    public ShareDialog setImage(Bitmap image) {
        this.image = image;
        return shareDialog;
    }
}
