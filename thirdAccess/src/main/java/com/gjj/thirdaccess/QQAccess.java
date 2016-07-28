package com.gjj.thirdaccess;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Title:过家家-项目经理
 * Description:
 * Copyright: Copyright (c) 2016
 * Company: 深圳市过家家
 * version: 1.0.0.0
 * author: jack
 * createDate 2016/2/16
 */
public class QQAccess {
    /**
     * 腾讯类
     */
    private Tencent mTencent;
    /**
     * QQ Appid
     */
    public static String APPID_QQ = "";
    /**
     * 授权域scope
     */
    public static final String SCOPE_QQ = "all";//"get_user_info";

    private  Context mContext;
    public QQAccess(Context context) {
        this.mContext = context;
        initAPPID();
    }

    public QQAccess(Context context, String appId) {
        this.mContext = context;
        APPID_QQ = appId;
    }
    /**
     * 初始化APPID
     */
    public void initAPPID() {
        AppIdMgr appIdMgr = new AppIdMgr(mContext);
        APPID_QQ = appIdMgr.getQQAPPID();
    }

    /**
     * 获取mTencent
     * @return
     */
    public Tencent getmTencent() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(APPID_QQ, mContext);
        }
        return mTencent;
    }

    /**
     * QQ登录
     * @param iUiListener
     */
    public void loginQQ(IUiListener iUiListener) {
        if (mTencent == null) {
            getmTencent();
        }
        if (!mTencent.isSessionValid()) {
            mTencent.login((Activity) mContext, SCOPE_QQ, iUiListener);
        }
    }

    /**
     * 获取用户信息
     * @param iUiListener
     */
    public void getUserInfoQQ(IUiListener iUiListener ) {
        if (mTencent == null) {
            getmTencent();
        }
        QQToken qqAuth = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(mContext, mTencent.getQQToken());
        userInfo.getUserInfo(iUiListener);
    }

    /**
     * 初始化token
     * @param jsonObject
     */
    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    /**
     * 登出
     */
    public void loginOutQQ() {
        if (mTencent == null) {
            getmTencent();
        }
        mTencent.logout(mContext);
    }


    public void ShareToQQ(IUiListener iUiListener, String webUrl, String title, String summary, String imageUrl) {
        getmTencent();
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  webUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ((Activity) mContext, params, iUiListener);
    }

    public void shareToQzone(IUiListener iUiListener, String webUrl, String title, String summary, String imageUrl) {
        getmTencent();
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webUrl);//必填
        ArrayList<String> list = new ArrayList<String>();
        if (!TextUtils.isEmpty(imageUrl)) {
            list.add(imageUrl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        mTencent.shareToQzone((Activity) mContext, params, iUiListener);
    }
}
