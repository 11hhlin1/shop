package com.gjj.thirdaccess;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;

/**
 * Title:过家家-项目经理
 * Description:
 * Copyright: Copyright (c) 2016
 * Company: 深圳市过家家
 * version: 1.0.0.0
 * author: jack
 * createDate 2016/2/19
 *
 * 微博认证授权回调类。
 * 1. SSO 授权时，需要在 {@link 'onActivityResult'} 中调用 {@link SsoHandler#authorizeCallBack} 后，
 *    该回调才会被执行。
 * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
 * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
 */
public class SinaAuthListener  implements WeiboAuthListener {
    private Context mContext;

    public SinaAuthListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token

        Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        //从这里获取用户输入的 电话号码信息
        String  phoneNum =  mAccessToken.getPhoneNum();
        if (mAccessToken.isSessionValid()) {
            // 显示 Token
            //updateTokenView(false);
            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                    new java.util.Date(mAccessToken.getExpiresTime()));
            String format = mContext.getString(R.string.weibosdk_demo_token_to_string_format_1);
            String message = String.format(format, mAccessToken.getToken(), date);
            Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();
            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);
            Toast.makeText(mContext, "sina登录成功！", Toast.LENGTH_SHORT).show();
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            String message = "sina登录失败";
            if (!TextUtils.isEmpty(code)) {
                message = message + "\nObtained the code: " + code;
            }
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
        doComplete(values);
    }

    @Override
    public void onCancel() {
        Toast.makeText(mContext, "取消", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Toast.makeText(mContext,
                "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    protected void doComplete(Bundle values) {

    }
}
