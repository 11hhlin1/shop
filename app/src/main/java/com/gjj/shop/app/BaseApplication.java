package com.gjj.shop.app;

import android.app.Application;

import com.gjj.applibrary.app.AppLib;
import com.lzy.okhttputils.OkHttpUtils;

/**
 * Created by Administrator on 2016/7/10.
 */
public class BaseApplication extends Application {
    private static BaseApplication mApp;
    private AppLib mAppLib;

    public static BaseApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mAppLib = AppLib.onCreate(mApp);

        OkHttpUtils.init(this);
    }
}

