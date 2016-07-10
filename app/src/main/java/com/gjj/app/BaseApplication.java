package com.gjj.app;

import android.app.Application;

import com.lzy.okhttputils.OkHttpUtils;

/**
 * Created by Administrator on 2016/7/10.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpUtils.init(this);
    }
}

