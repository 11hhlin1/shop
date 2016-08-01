package com.gjj.shop.index.cheap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gjj.shop.index.ProductListFragment;

/**
 * Created by Chuck on 2015/10/12.
 */
public class CheapFragmentAdapter extends FragmentPagerAdapter {
    private Fragment[] mCache;

    public CheapFragmentAdapter(FragmentManager fm, Fragment[] cache) {
        super(fm);
        mCache = cache;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mCache[position];
        if (fragment != null) {
            return fragment;
        }
        if (position == 0) {
            fragment = new ProductListFragment();
        } else if (position == 1) {
            fragment = new ProductListFragment();
        } else if (position == 2) {
            fragment = new ProductListFragment();
        }
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
