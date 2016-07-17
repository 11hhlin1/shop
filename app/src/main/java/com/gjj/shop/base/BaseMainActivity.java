package com.gjj.shop.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.gjj.applibrary.log.L;
import com.gjj.shop.R;

import java.util.ArrayList;

/**
 *
 * 类/接口注释
 *
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class BaseMainActivity extends FragmentActivity {

    private ArrayList<Fragment> mHistoryList = new ArrayList<Fragment>();
    private FragmentFactory mFragmentFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public <T extends BaseFragment> Fragment replaceFragment(Class<T> clazz) {
        return replaceFragment(clazz, new Bundle());
    }
    public FragmentFactory getFragmentFactory() {
        if (mFragmentFactory == null) {
            mFragmentFactory = new FragmentFactory();
        }
        return mFragmentFactory;
    }

    public boolean isFragmentInCache(BaseFragment f) {
        if (mFragmentFactory != null) {
            return mFragmentFactory.getFragmentFromCache(f.getClass()) != null;
        }
        return false;
    }


    /**
     * @param clazz
     * @param args
     */
    public <T extends BaseFragment> Fragment replaceFragment(Class<T> clazz, Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fT = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(clazz.getName());
        if (fragment == null) {
            fragment = getFragmentFactory().getFragment(clazz, false);
            fragment.setArguments(args);
            fT.add(R.id.content, fragment, clazz.getName());
        } else {
            fT.show(fragment);
        }
        ArrayList<Fragment> historyList = mHistoryList;
        if (!historyList.contains(fragment)) {
            historyList.add(fragment);
        }

        for (Fragment other : historyList) {
            if (other != fragment) {
                fT.hide(other);
            }
        }
        fT.commitAllowingStateLoss();

        return fragment;
    }

    protected <T extends BaseFragment> BaseFragment findFragment(Class<T> clazz) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (BaseFragment) fragmentManager.findFragmentByTag(clazz.getName());
    }

    public BaseFragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (BaseFragment) fragmentManager.findFragmentById(R.id.content);
    }

    protected void init() {
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = getCurrentFragment();
//        if (fragment == null || !fragment.goBack(true)) {
//            super.onBackPressed();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public <T extends BaseFragment> void removeCacheFragment(Class<T> clazz) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(clazz.getName());
        if (fragment != null) {
            L.d("BaseMainActivity removeCacheFragment: %s", clazz.getName());
            mHistoryList.remove(fragment);
            FragmentTransaction fT = fragmentManager.beginTransaction();
            fT.remove(fragment);
            fT.commitAllowingStateLoss();
        }
    }
}
