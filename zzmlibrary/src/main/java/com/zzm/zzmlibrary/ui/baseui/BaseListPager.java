package com.zzm.zzmlibrary.ui.baseui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zzm.zzmlibrary.R;
import com.zzm.zzmlibrary.common.BaseApplication;
import com.zzm.zzmlibrary.model.BaseListView;
import com.zzm.zzmlibrary.presenter.BaseListPresenter;
import com.zzm.zzmlibrary.ui.adapter.BaseQuickAdapter;
import com.zzm.zzmlibrary.ui.widget.AutoSwipeRefreshLayout;
import com.zzm.zzmlibrary.ui.widget.RecycleViewDivider;
import com.zzm.zzmlibrary.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itzhong on 2016/8/8.
 */
public abstract class BaseListPager extends BasePager implements BaseListView, BaseQuickAdapter.OnRecyclerViewItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    protected AutoSwipeRefreshLayout mSwipeLayout;
    protected RecyclerView mRecyclerView;
    public BaseQuickAdapter adapter;
    protected BaseListPresenter listPresenter;
    private PagerListener mListener;
    protected View fTitleBar;

    public BaseListPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = ((BaseActivity)context).getLayoutInflater().inflate(R.layout.pager_base_list,null);
        adapter = getAdapter();
        if (adapter == null) {
            throw new NullPointerException("adapter不能为空");
        }
        listPresenter = new BaseListPresenter(context, this);
        mSwipeLayout = UIUtils.getView(view,R.id.mPagerSwipeLayout);
        mRecyclerView = UIUtils.getView(view,R.id.mPagerRecyclerView);
        fTitleBar = UIUtils.getView(view,R.id.fTitleBar);
        fTitleBar.setVisibility(View.GONE);
        setOrientation();
        mListener = new PagerListener();
        mSwipeLayout.setOnRefreshListener(mListener);
        mSwipeLayout.setColorSchemeResources(BaseApplication.titleBarBackgroundColor);
        View emptyView = ((BaseActivity)context).getLayoutInflater().inflate(R.layout.item_empty_layout,(ViewGroup) mRecyclerView.getParent(), false);
        TextView tvNoData = UIUtils.getView(emptyView, R.id.tvNoData);
        tvNoData.setText(UIUtils.getString(R.string.click_loading));
        emptyView.setOnClickListener(v->onRefreshData());
        adapter.setEmptyView(true,emptyView);
        adapter.openLoadAnimation();
        adapter.setOnRecyclerViewItemClickListener(this);
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadMore(listPresenter.PAGESIZE,true);
        mRecyclerView.setAdapter(adapter);
        initOtherView();
        if(isLoadDefault()){
            listPresenter.loadDefaultData();
        }
        return view;
    }

    /**
     * 设置adapter等
     */
    protected void initOtherView(){}

    /**
     * 子类可复写修改Orientation
     */
    public void setOrientation(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //默认分割线的颜色高度2px,灰色
        mRecyclerView.addItemDecoration(new RecycleViewDivider(context,LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onRefreshView(List<?> list) {
        adapter.setNewData(list);
        adapter.removeAllFooterView();
        View view = ((BaseActivity)context).getLayoutInflater().inflate(R.layout.item_footer_layout,(ViewGroup) mRecyclerView.getParent(), false);
        if(list.size()<listPresenter.PAGESIZE||listPresenter.totalPage<=1){
            TextView tv_load_more = UIUtils.getView(view, R.id.tv_load_more);
            UIUtils.getView(view,R.id.iv_load_more).setVisibility(View.GONE);
            tv_load_more.setText("已经加载全部");
            adapter.addFooterView(view);
        }else{
            adapter.setLoadingView(view);
            adapter.openLoadMore(listPresenter.PAGESIZE,true);
        }
    }

    @Override
    public void stopRefreshView() {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(List<?> list) {
        adapter.notifyDataChangedAfterLoadMore(list, true);
        if(list.size()<listPresenter.PAGESIZE||(listPresenter.mPage-1)==listPresenter.totalPage){
            adapter.removeAllFooterView();
            View view = ((BaseActivity)context).getLayoutInflater().inflate(R.layout.item_footer_layout,(ViewGroup) mRecyclerView.getParent(), false);
            TextView tv_load_more = UIUtils.getView(view, R.id.tv_load_more);
            UIUtils.getView(view,R.id.iv_load_more).setVisibility(View.GONE);
            tv_load_more.setText("已经加载全部");
            adapter.addFooterView(view);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        listPresenter.loadDataMore();
    }

    @Override
    public void noData() {
        if(context==null) return;
        if(adapter.getData()!=null&&adapter.getData().size()>0){
            adapter.setNewData(new ArrayList<>());
        }
        View emptyView = adapter.getEmptyView();
        TextView tvNoData = UIUtils.getView(emptyView, R.id.tvNoData);
        tvNoData.setText(UIUtils.getString(R.string.click_nodata));
    }

    @Override
    public void noMoreData() {
        if(context==null) return;
        adapter.notifyDataChangedAfterLoadMore(false);
        adapter.removeAllFooterView();
        View view = ((BaseActivity)context).getLayoutInflater().inflate(R.layout.item_footer_layout,(ViewGroup) mRecyclerView.getParent(), false);
        TextView tv_load_more = UIUtils.getView(view, R.id.tv_load_more);
        UIUtils.getView(view,R.id.iv_load_more).setVisibility(View.GONE);
        tv_load_more.setText("已经加载全部");
        adapter.addFooterView(view);
    }

    @Override
    public void noNetWork() {
        if(context==null) return;
        View emptyView = adapter.getEmptyView();
        TextView tvNoData = UIUtils.getView(emptyView, R.id.tvNoData);
        tvNoData.setText(UIUtils.getString(R.string.click_load));
    }

    private class PagerListener implements AutoSwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            listPresenter.loadDefaultData();
        }
    };

    public void onRefreshData() {
        if (mSwipeLayout == null) return;
        mSwipeLayout.post(() -> mSwipeLayout.setRefreshing(true));
        mListener.onRefresh();
    }

    public void stopRefreshData() {
        listPresenter.cancelLoad();
    }

    /**
     * 是否默认自动加载数据
     * @return
     */
    public boolean isLoadDefault(){
        return true;
    }

    @Override
    public void setViewData(String data) {

    }
    @Override
    public void showLoading() {
        if(listPresenter.mPage>1){
            super.showLoading();
        }
    }
}
