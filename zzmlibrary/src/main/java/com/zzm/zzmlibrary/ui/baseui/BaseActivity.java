package com.zzm.zzmlibrary.ui.baseui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zzm.zzmlibrary.R;
import com.zzm.zzmlibrary.common.BaseApplication;
import com.zzm.zzmlibrary.common.Constants;
import com.zzm.zzmlibrary.common.interf.NetEventHandler;
import com.zzm.zzmlibrary.model.BaseView;
import com.zzm.zzmlibrary.model.bean.BaseResponse;
import com.zzm.zzmlibrary.presenter.BasePresenter;
import com.zzm.zzmlibrary.utils.DialogUtils;
import com.zzm.zzmlibrary.utils.LogUtils;
import com.zzm.zzmlibrary.utils.UIUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by itzhong on 2016/6/15.
 * 用于初始化Activity提供方法，退出APP结束所有Activity的活动
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView, NetEventHandler {

    protected final String TAG = this.getClass().getSimpleName();
    public static List<BaseActivity> mActivities = new LinkedList<BaseActivity>();

    public static BaseActivity activity;
    //    public ViewDataBinding binding;
    public BasePresenter presenter;
    public TextView mTvTitle;
    public ImageButton mLeftButton;
    public ImageButton mRightButton;
    public TextView mLeftText;
    public TextView mRightText;
    public static NetEventHandler netListener;
    public AutoLinearLayout titleBarLayout;
    public SegmentTabLayout segmentTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        synchronized (mActivities) {
            mActivities.add(this);
        }
        onBeforeSetContentLayout();
        setContentView(getLayoutId());
        /*if (isDataBinding()) {
            binding = DataBindingUtil.setContentView(this, getLayoutId());
        } else {
            setContentView(getLayoutId());
        }*/
        presenter = getPresenter();
        initActionBar();
        init(savedInstanceState);
        initView();
        if (isLoadDefault()) {
            presenter.loadDefaultData();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        netListener = this;
        if (Constants.APP_DBG) {
            LogUtils.d("测试统计，当前Activity个数：" + mActivities.size());
            for (BaseActivity activity : mActivities) {
                LogUtils.d(activity.getClass().getName());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activity = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
        netListener = null;
        DialogUtils.disDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (isDataBinding()) {
            binding.unbind();
        }*/
        if (presenter != null) {
            presenter.cancelLoad();
        }
        removeActivity();

    }

    @Override
    public void finish() {
        super.finish();
        removeActivity();
    }

    private void removeActivity() {
        if (mActivities.contains(this)) {
            synchronized (mActivities) {
                mActivities.remove(this);
            }
        }
    }

    /**
     * 退出APP
     */
    public static void exitApp() {
        ArrayList<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity baseActivity : copy) {
            baseActivity.finish();
        }
    }

    /**
     * 获取presenter 默认使用BasePresenter
     * @return
     */
    public BasePresenter getPresenter() {
        return new BasePresenter(this, this);
    }

    @Override
    public void showLoading() {
        DialogUtils.makeLoading(this).show();
    }

    public void showLoading(String str) {
        DialogUtils.makeLoading(this).setMessage(str);
        DialogUtils.makeLoading(this).show();
    }

    public void showLoading(int id) {
        DialogUtils.makeLoading(this).setMessage(UIUtils.getString(id));
        DialogUtils.makeLoading(this).show();
    }


    @Override
    public void hideLoading() {
        DialogUtils.makeLoading(this).dismiss();
    }

    @Override
    public void noNetWork() {
        UIUtils.showToast("网络连接异常");
        hideLoading();
    }

    /**
     * 优化抽取findViewById
     *
     * @param resId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(@IdRes int resId) {
        return (T) super.findViewById(resId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(View layoutView, @IdRes int resId) {
        return (T) layoutView.findViewById(resId);
    }


    public void initActionBar() {
        try{
            segmentTabLayout = getView(R.id.segmentTabLayout);
            titleBarLayout = getView(R.id.titleBarLayout);
            mTvTitle = getView(R.id.title);
            mLeftButton = getView(R.id.left_button);
            mRightButton = getView(R.id.right_button);
            mLeftText = getView(R.id.left_text);
            mRightText = getView(R.id.right_text);
            mLeftButton.setOnClickListener(v -> comeBack());
            mLeftText.setOnClickListener(v -> comeBack());
            titleBarLayout.setBackgroundColor(BaseApplication.titleBarBackgroundColor);
            mTvTitle.setTextColor(BaseApplication.titleBarTextColor);
            mLeftText.setTextColor(BaseApplication.titleBarTextColor);
            mRightText.setTextColor(BaseApplication.titleBarTextColor);
            segmentTabLayout.setTextUnselectColor(BaseApplication.titleBarTextColor);
            segmentTabLayout.setTextSelectColor(BaseApplication.titleBarBackgroundColor);
            segmentTabLayout.setIndicatorColor(BaseApplication.titleBarTextColor);
            segmentTabLayout.setBackgroundColor(BaseApplication.titleBarBackgroundColor);
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.e(TAG,"没有include标题栏");
        }
    }

    private void comeBack() {
        onBackPressed();
        finish();
    }

    /**
     * 返回指定activity
     *
     * @param className
     */
    public void comeBack(String className) {
        if (!TextUtils.isEmpty(className)) {
            try {
                Class<?> aClass = Class.forName(className);
                if (aClass != this.getClass()) {
                    Intent intent = new Intent(this, aClass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回指定activity
     *
     * @param className
     */
    public void comeBack(String className, Bundle bundle) {
        if (!TextUtils.isEmpty(className)) {
            try {
                Class<?> aClass = Class.forName(className);
                if (aClass != this.getClass()) {
                    Intent intent = new Intent(this, aClass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    startActivity(intent);
                    finish();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 监听网络改变
     * @param status
     */
    @Override
    public void onNetChange(int status) {
        if (status > 0) {
            LogUtils.d("网络连接成功");
        } else {
            UIUtils.showToast("网络连接异常，请检查您的网络");
        }
    }

    /**
     * 是否使用DataBinding，默认不使用
     *
     * @return
     */
    protected boolean isDataBinding() {
        return false;
    }

    /**
     * 加载布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化界面布局
     */
    protected void initView() {
    }

    /**
     * 初始化
     */
    protected void init(Bundle savedInstanceState) {
    }

    /**
     * 在SetContent之前执行一些操作
     */
    protected void onBeforeSetContentLayout() {
    }

    /**
     * 子类实现刷新数据更新ui操作
     * 默认 presenter.loadDefaultData();
     */
    protected void onRefreshData() {
        presenter.loadDefaultData();
    }

    /**
     * 子类实现停止刷新数据更新ui操作
     * 默认 presenter.cancelLoad();
     */
    protected void stopRefreshData() {
        presenter.cancelLoad();
    }

    /**
     * 是否默认自动加载数据
     *
     * @return
     */
    public boolean isLoadDefault() {
        return true;
    }

    /**
     * 服务器返回非200
     * @param response
     */
    @Override
    public void isNotTwohundred(BaseResponse response) {
    }

}
