package com.gjj.shop.index;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by user on 16/8/27.
 */
public class ProductDetailFragmentAdapter extends FragmentPagerAdapter {
    private Fragment[] mCache;
    private Bundle mBundle;

    public ProductDetailFragmentAdapter(FragmentManager fm, Fragment[] cache, Bundle bundle) {
        super(fm);
        mCache = cache;
        mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mCache[position];
        if (fragment != null) {
            return fragment;
        }
        if (position == 0) {
            fragment = new ProductPicFragment();
            fragment.setArguments(mBundle);
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
