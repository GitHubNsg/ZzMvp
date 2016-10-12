package com.zzm.zzmlibrary.ui.baseui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.zzm.zzmlibrary.model.BaseView;
import com.zzm.zzmlibrary.model.bean.BaseResponse;
import com.zzm.zzmlibrary.presenter.BasePresenter;
import com.zzm.zzmlibrary.utils.UIUtils;


/**
 * Created by itzhong on 2016/7/14.
 * 用于初始化Fragment提供方法
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    private View view;
    public BasePresenter presenter;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected static final String TYPE = "FRAGMENT_TYPE";

    public BaseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(getLayoutId(), container, false);
            presenter = getPresenter();
            initView();
            if(presenter!=null&&isLoadDefault()){
                presenter.loadDefaultData();
            }
        }
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if(presenter!=null)
        presenter.cancelLoad();
    }

    private void removeChild(View view){
        ViewParent parent = view.getParent();
        ViewGroup group = (ViewGroup) parent;
        group.removeView(view);
    }

    /**
     * 抽取findViewById
     * @param resId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(@IdRes int resId) { return (T) view.findViewById(resId);}

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(View layoutView, @IdRes int resId) { return (T) layoutView.findViewById(resId);}

    /**
     * 获取presenter
     * @return
     */
    protected BasePresenter getPresenter(){
        return new BasePresenter(getActivity(),this);
    }

    /**
     * 加载布局
     * @return
     */
    protected abstract int getLayoutId() ;

    /**
     * 初始化view
     */
    public void initView() {}

    /**
     * 子类实现刷新数据更新ui操作
     * 默认 presenter.loadDefaultData();
     */
    public void onRefreshData(){
        presenter.loadDefaultData();
    };

    /**
     * 子类实现停止刷新数据更新ui操作
     * 默认 presenter.cancelLoad();
     */
    public void stopRefreshData(){
        presenter.cancelLoad();
    };

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    /**
     * 是否默认自动加载数据
     * @return
     */
    public boolean isLoadDefault(){
        return true;
    }

    @Override
    public void noNetWork() {
        UIUtils.showToast("网络连接异常");
        hideLoading();
    }

    /**
     * 服务器返回非200
     * @param response
     */
    @Override
    public void isNotTwohundred(BaseResponse response) {
    }
}
