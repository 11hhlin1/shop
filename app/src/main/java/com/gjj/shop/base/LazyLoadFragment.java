package com.gjj.shop.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gjj.shop.R;


/**
 * Created by Kop on 2015/11/2.
 */
public abstract class LazyLoadFragment extends BaseFragment {
    private FrameLayout mContentView;
    private Bundle mSavedInstanceState;

    private boolean mIsVisibleToUser;
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContentView = (FrameLayout)inflater.inflate(R.layout.fragment_base_lazy_load_layout, null);
        mSavedInstanceState = savedInstanceState;
        if (mIsVisibleToUser) {
            replaceLazyView();
        }
        return mContentView;
    }

    public abstract View onLazyCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState);

    public boolean isLazyLoaded() {
        return mContentView != null && mContentView.findViewById(R.id.lazy_loading_tv) == null;
    }

    public final void replaceLazyView() {
        FrameLayout contentView = mContentView;
        if (contentView != null && contentView.findViewById(R.id.lazy_loading_tv) != null) {
            View lazyCreateView = onLazyCreateView(getActivity().getLayoutInflater(), contentView, mSavedInstanceState);
            contentView.removeAllViews();
            contentView.addView(lazyCreateView);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            replaceLazyView();
        }
    }
}
