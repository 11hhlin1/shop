package com.gjj.shop.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okhttputils.OkHttpUtils;


/**
 * chuck
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayout(), container, false);
        return view;
    }

    public abstract int getContentViewLayout();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //根据 Tag 取消请求
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
