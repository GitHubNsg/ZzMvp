package com.zzm.zzmvp.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zzm.zzmlibrary.ui.adapter.TabEntity;
import com.zzm.zzmlibrary.ui.baseui.BaseActivity;
import com.zzm.zzmlibrary.ui.baseui.BaseFragment;
import com.zzm.zzmlibrary.ui.widget.AutoNoScrollViewPager;
import com.zzm.zzmlibrary.utils.UIUtils;
import com.zzm.zzmvp.R;
import com.zzm.zzmvp.ui.fragment.Fragment1;
import com.zzm.zzmvp.ui.fragment.Fragment2;
import com.zzm.zzmvp.ui.fragment.Fragment3;
import com.zzm.zzmvp.ui.fragment.Fragment4;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements OnTabSelectListener, ViewPager.OnPageChangeListener {

    private AutoNoScrollViewPager mViewPager;
    public CommonTabLayout mTabLayout;
    private SparseArray<BaseFragment> mFragments = new SparseArray<BaseFragment>();
    private String[] mTitles = UIUtils.getStringArray(R.array.main_tabs);
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds = {R.drawable.tab_recommend_default,
            R.drawable.tab_financing_default, R.drawable.tab_me_default,
            R.drawable.tab_setting_default};
    private int[] mIconSelectIds = {R.drawable.tab_recommend_selected,
            R.drawable.tab_financing_selected, R.drawable.tab_me_selected,
            R.drawable.tab_setting_selected};

    @Override
    protected void initView() {
        super.initView();
        mTvTitle.setText("MVP框架展示");
        mLeftButton.setVisibility(View.GONE);
        mViewPager = getView(R.id.mViewPager);
        mTabLayout = getView(R.id.mTabLayout);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(this);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setCurrentTab(0);
    }

    @Override
    public void onTabSelect(int position) {
        mViewPager.setCurrentItem(position,false);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTabLayout.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return mTitles.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }
    }

    public BaseFragment getFragment(int postion) {
        BaseFragment fragment = mFragments.get(postion);
        if (fragment == null) {
            switch (postion) {
                case 0:
                    fragment = new Fragment1();
                    break;
                case 1:
                    fragment = new Fragment2();
                    break;
                case 2:
                    fragment = new Fragment3();
                    break;
                case 3:
                    fragment = new Fragment4();
                    break;
            }
            mFragments.put(postion, fragment);
        }
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String getDefaultUrl() {
        return null;
    }

    @Override
    public void setViewData(String data) {

    }



}
