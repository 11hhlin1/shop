package com.gjj.shop.index.foreign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gjj.shop.index.ProductListFragment;

import java.util.ArrayList;

/**
 * Created by Chuck on 2016/9/5.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private Fragment[] mCache;
    private ArrayList<CategoryData> dataArrayList;
    private int firstCate;
    public ViewPagerFragmentAdapter(FragmentManager fm, Fragment[] cache, ArrayList<CategoryData> datas, int cate) {
        super(fm);
        mCache = cache;
        dataArrayList = datas;
        firstCate = cate;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mCache[position];
        if (fragment != null) {
            return fragment;
        }
        fragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("sortId", dataArrayList.get(position).id);
        bundle.putInt("cate", firstCate);
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        mCache[position] = fragment;
        return fragment;
    }

    @Override
    public int getCount() {
        return mCache.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}

