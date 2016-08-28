package com.gjj.shop.index;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by user on 16/8/27.
 */
public class ProductDetailFragmentAdapter extends FragmentPagerAdapter {
    private Fragment[] mCache;

    public ProductDetailFragmentAdapter(FragmentManager fm, Fragment[] cache) {
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
            fragment = new ProductPicFragment();
        } else if (position == 1) {
            fragment = new UserAdviceFragment();
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
