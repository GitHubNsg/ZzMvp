package com.zzm.zzmlibrary.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.zzm.zzmlibrary.ui.baseui.BaseListPager;

import java.util.ArrayList;

/**
 * Created by itzhong on 2016/8/10.
 */
public class ListPagerAdapter extends PagerAdapter {

    private ArrayList<BaseListPager> mListPagers ;

    public ListPagerAdapter(ArrayList<BaseListPager> mListPagers) {
        this.mListPagers = mListPagers;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(mListPagers.get(position).getRootView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListPagers.get(position).getRootView(), 0);
        return mListPagers.get(position).getRootView();
    }

    @Override
    public int getCount() {
        return  mListPagers.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

}
