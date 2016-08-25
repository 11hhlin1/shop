package com.gjj.shop.app;

import android.app.Application;

import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.http.model.BundleKey;
import com.gjj.applibrary.task.ForegroundTaskExecutor;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.shop.user.UserMgr;
import com.lzy.okhttputils.OkHttpUtils;

/**
 * Created by Administrator on 2016/7/10.
 */
public class BaseApplication extends Application {
    private static BaseApplication mApp;
    private AppLib mAppLib;
    private UserMgr mUserMgr;
    public static BaseApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mAppLib = AppLib.onCreate(mApp);
//        PreferencesManager.getInstance().put(BundleKey.TOKEN, "834320403214");
        OkHttpUtils.init(this);

        ForegroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                mUserMgr = new UserMgr();
                AppLib.setInitialized(true);
            }
        });
    }
    public static UserMgr getUserMgr() {
        return mApp.mUserMgr;
    }
}

