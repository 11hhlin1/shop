package com.gjj.shop.user;

import android.text.TextUtils;

import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.http.model.BundleKey;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.event.EventOfLogout;
import com.gjj.shop.model.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Chuck on 2016/8/23.
 */
public class UserMgr {

    private UserInfo mUserInfo = null;
    public UserMgr() {
        mUserInfo = (UserInfo) PreferencesManager.getInstance().get(UserInfo.class);
//        List<UserInfo> userInfos = BaseApplication.getDaoSession(AppLib.getContext()).getUserInfoDao().loadAll();
//        if(!Util.isListEmpty(userInfos)) {
//            mUserInfo = userInfos.get(0);
//        }
    }

    public boolean isLogin() {
        UserInfo info = mUserInfo;
        if (null == info) {
            return false;
        }

        if (TextUtils.isEmpty(info.getToken())) {
            return false;
        }
        return true;
    }

    public void saveUserInfo(UserInfo userInfo) {
        PreferencesManager.getInstance().put(BundleKey.TOKEN, userInfo.getToken());
//        BaseApplication.getDaoSession(AppLib.getContext()).getUserInfoDao().deleteAll();
        PreferencesManager.getInstance().put(userInfo);
        mUserInfo = userInfo;
//        BaseApplication.getDaoSession(AppLib.getContext()).getUserInfoDao().insert(userInfo);
    }

    public UserInfo getUser() {
        return mUserInfo;
    }

    public void logOut() {
        mUserInfo = null;
        PreferencesManager.getInstance().put(BundleKey.TOKEN, "");
        EventBus.getDefault().post(new EventOfLogout());
//        BaseApplication.getDaoSession(AppLib.getContext()).getUserInfoDao().deleteAll();
        PreferencesManager.getInstance().clearAll();
    }
}
