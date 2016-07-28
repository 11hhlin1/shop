package com.gjj.thirdaccess;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Title:过家家-项目经理
 * Description:
 * Copyright: Copyright (c) 2016
 * Company: 深圳市过家家
 * version: 1.0.0.0
 * author: jack
 * createDate 2016/2/18
 */
public class SinaAccess {
    private static final String TAG = "SinaAccess";

    private AuthInfo mAuthInfo;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    /** 微博微博分享接口实例 */
    private IWeiboShareAPI mWeiboShareAPI = null;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    /** 应用的 APP_KEY*/
    public static  String APP_KEY = "";
    /**
     * 当前应用的回调页。
     *
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";


    private  Context mContext;
    public SinaAccess(Context context) {
        this.mContext = context;
        initAPPID();
    }

    public SinaAccess(Context context, String appId) {
        this.mContext = context;
        APP_KEY = appId;
    }

    /**
     * 初始化APPID
     */
    public void initAPPID() {
        AppIdMgr appIdMgr = new AppIdMgr(mContext);
        APP_KEY = appIdMgr.getSINAAPPID();
    }

    /**
     * 获取SsoHandler
     * @return
     */
    public SsoHandler getmSsoHandler() {
        if (mSsoHandler == null) {
            mAuthInfo = new AuthInfo(mContext, APP_KEY, REDIRECT_URL, SCOPE);
            mSsoHandler = new SsoHandler((Activity) mContext, mAuthInfo);
        }
        return mSsoHandler;
    }

    /**
     * 新浪客户端登录
     * @param weiboAuthListener
     */
    public void loginSina(WeiboAuthListener weiboAuthListener) {
        if (mSsoHandler == null) {
            getmSsoHandler();
        }
        mSsoHandler.authorize(weiboAuthListener);
    }

    /**
     * 登出
     */
    public void loginOutSina() {
        AccessTokenKeeper.clear(mContext.getApplicationContext());
        mAccessToken = new Oauth2AccessToken();
    }

    public void setmAccessToken(Oauth2AccessToken mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public Oauth2AccessToken getmAccessToken() {
        return mAccessToken;
    }
    public IWeiboShareAPI getmWeiboShareAPI() {
        return mWeiboShareAPI;
    }

    public void initAPI() {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, APP_KEY);
        mWeiboShareAPI.registerApp();
    }

    public void initAPI(WeiboAuthListener weiboAuthListener) {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, APP_KEY);
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        if (mAccessToken.isSessionValid()) {
            mWeiboShareAPI.registerApp();
        } else {
            mWeiboShareAPI.registerApp();
            loginSina(weiboAuthListener);
        }
    }

    public void shareToWeibo(String url, String title, String des, Bitmap bitmap) {

        if(mWeiboShareAPI.isWeiboAppInstalled()) {
//            sendSingleMessage(true, true, false);
            sendSingleMessage(url, title, des, bitmap);
         } else {
            Toast.makeText(mContext, mContext.getString(R.string.sina_not_install), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     *
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     */
    private void sendSingleMessage(boolean hasText, boolean hasImage, boolean hasWebpage) {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            weiboMessage.mediaObject = getTextObj();
        }
//        if (hasImage) {
//            weiboMessage.mediaObject = getImageObj(mContext);
//        }
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj(mContext);
        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/

        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest((Activity) mContext, request);
    }

    private void sendSingleMessage(String url, String title, String des, Bitmap bitmap) {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.imageObject = getImageObj(bitmap);
        weiboMessage.mediaObject = getWebpageObj(url, title, des, bitmap);
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest((Activity) mContext, request);
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "分享文本";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable();
        //        设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }
    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(String url, String title, String des, Bitmap bitmap) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = des;

//        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
//        mediaObject.defaultText = "过家家";
        return mediaObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(Context context) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "";
        mediaObject.description = "";

        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "";
        mediaObject.defaultText = "过家家";
        return mediaObject;
    }
}
