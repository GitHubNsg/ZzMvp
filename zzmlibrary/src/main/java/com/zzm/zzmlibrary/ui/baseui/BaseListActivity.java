package com.zzm.zzmlibrary.ui.baseui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zzm.zzmlibrary.common.BaseApplication;
import com.zzm.zzmlibrary.model.BaseListView;
import com.zzm.zzmlibrary.presenter.BaseListPresenter;
import com.zzm.zzmlibrary.presenter.BasePresenter;
import com.zzm.zzmlibrary.ui.adapter.BaseQuickAdapter;
import com.zzm.zzmlibrary.ui.widget.AutoSwipeRefreshLayout;
import com.zzm.zzmlibrary.ui.widget.RecycleViewDivider;
import com.zzm.zzmlibrary.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itzhong on 2016/7/15.
 */
public abstract class BaseListActivity extends BaseActivity implements BaseListView, BaseQuickAdapter.OnRecyclerViewItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    public AutoSwipeRefreshLayout mSwipeLayout;
    public RecyclerView mRecyclerView;
    public BaseQuickAdapter adapter;
    public BaseListPresenter listPresenter;

    @Override
    protected int getLayoutId() {
        return com.zzm.zzmlibrary.R.layout.activity_base_list;
    }

    @Override
    protected void initView() {
        super.initView();
        adapter = getAdapter();
        if (adapter == null) {
            throw new NullPointerException("adapter不能为空");
        }
        listPresenter = (BaseListPresenter) presenter;
        mSwipeLayout = getView(com.zzm.zzmlibrary.R.id.mSwipeLayout);
        mRecyclerView = getView(com.zzm.zzmlibrary.R.id.mRecyclerView);
        setOrientation();
        mSwipeLayout.setOnRefreshListener(listener);
        mSwipeLayout.setColorSchemeColors(BaseApplication.titleBarBackgroundColor);
        View view = getLayoutInflater().inflate(com.zzm.zzmlibrary.R.layout.item_empty_layout,(ViewGroup) mRecyclerView.getParent(), false);
        TextView tvNoData = getView(view, com.zzm.zzmlibrary.R.id.tvNoData);
        tvNoData.setText(UIUtils.getString(com.zzm.zzmlibrary.R.string.click_loading));
        view.setOnClickListener(v->onRefreshData());
        adapter.setEmptyView(true,view);
        adapter.openLoadAnimation();
        adapter.setOnRecyclerViewItemClickListener(this);
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadMore(listPresenter.PAGESIZE,true);
        mRecyclerView.setAdapter(adapter);

    }

    /**
     * 子类可复写修改Orientation
     */
    public void setOrientation(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //默认分割线的颜色高度2px,灰色
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onRefreshView(List<?> list) {
        adapter.setNewData(list);
        adapter.removeAllFooterView();
        View view = getLayoutInflater().inflate(com.zzm.zzmlibrary.R.layout.item_footer_layout,(ViewGroup) mRecyclerView.getParent(), false);
        if(list.size()<listPresenter.PAGESIZE||listPresenter.totalPage<=1){
            TextView tv_load_more = getView(view, com.zzm.zzmlibrary.R.id.tv_load_more);
            getView(view,com.zzm.zzmlibrary.R.id.iv_load_more).setVisibility(View.GONE);
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
            View view = getLayoutInflater().inflate(com.zzm.zzmlibrary.R.layout.item_footer_layout,(ViewGroup) mRecyclerView.getParent(), false);
            TextView tv_load_more = getView(view, com.zzm.zzmlibrary.R.id.tv_load_more);
            getView(view,com.zzm.zzmlibrary.R.id.iv_load_more).setVisibility(View.GONE);
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
        if(adapter.getData()!=null&&adapter.getData().size()>0){
           adapter.setNewData(new ArrayList<>());
        }
        View emptyView = adapter.getEmptyView();
        TextView tvNoData = getView(emptyView, com.zzm.zzmlibrary.R.id.tvNoData);
        tvNoData.setText(UIUtils.getString(com.zzm.zzmlibrary.R.string.click_nodata));
    }

    @Override
    public void noMoreData() {
        adapter.notifyDataChangedAfterLoadMore(false);
        adapter.removeAllFooterView();
        View view = getLayoutInflater().inflate(com.zzm.zzmlibrary.R.layout.item_footer_layout,(ViewGroup) mRecyclerView.getParent(), false);
        TextView tv_load_more = getView(view, com.zzm.zzmlibrary.R.id.tv_load_more);
        getView(view,com.zzm.zzmlibrary.R.id.iv_load_more).setVisibility(View.GONE);
        tv_load_more.setText("已经加载全部");
        adapter.addFooterView(view);
    }

    @Override
    public void noNetWork() {
        View emptyView = adapter.getEmptyView();
        TextView tvNoData = getView(emptyView, com.zzm.zzmlibrary.R.id.tvNoData);
        tvNoData.setText(UIUtils.getString(com.zzm.zzmlibrary.R.string.click_load));
    }

    private AutoSwipeRefreshLayout.OnRefreshListener listener = new AutoSwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            listPresenter.loadDefaultData();
        }
    };

    @Override
    public BasePresenter getPresenter() {
        return new BaseListPresenter(this, this);
    }

    @Override
    protected void onRefreshData() {
        super.onRefreshData();
        if (mSwipeLayout == null) return;
        mSwipeLayout.post(() -> mSwipeLayout.setRefreshing(true));
        listener.onRefresh();
    }

    @Override
    protected void stopRefreshData() {
        super.stopRefreshData();
        presenter.cancelLoad();
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

    @Override
    public boolean isLoadDefault() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefreshData();
    }
}
