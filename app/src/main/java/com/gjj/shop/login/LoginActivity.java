package com.gjj.shop.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.http.callback.CommonCallback;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.model.BundleKey;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.task.BackgroundTaskExecutor;
import com.gjj.applibrary.util.MD5Util;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.app.BaseApplication;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.event.EventOfLoginWeixin;
import com.gjj.shop.main.MainActivity;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.widget.CustomProgressDialog;
import com.gjj.shop.wxapi.ShareConstant;
import com.gjj.thirdaccess.QQAccess;
import com.gjj.thirdaccess.SinaAccess;
import com.gjj.thirdaccess.WeiXinAccess;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.AbsCallback;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.common.Constants;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by chuck on 16/7/17.
 */
public class LoginActivity extends Activity {
    private CustomProgressDialog mLoginDialog;
    /**
     * 输入手机号
     */
    @Bind(R.id.username)
    EditText login_phone_number;
    /**
     * 验证码
     */
    @Bind(R.id.psw)
    EditText login_psw;


    /**
     * 登陆
     */
    @Bind(R.id.login_btn)
    Button mLoginBtn;

    /**
     * @param
     * @return void
     * @throws
     * @Description:登陆
     */
    @OnClick(R.id.login_btn)
    void onClickLogin() {
        doLogin();
    }

    @OnClick(R.id.go_register)
    void onClickRegister() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegisterFragment.FLAG, true);
        PageSwitcher.switchToTopNavPage(this,RegisterFragment.class,bundle,getString(R.string.register),"");
    }
    private SinaAccess sinaAccess;
    @OnClick(R.id.sina_login)
    void onSinaLogin() {
        sinaAccess = new  SinaAccess(this, ShareConstant.SINAAPPID);
        sinaAccess.loginSina(new WeiboAuthListener() {

            @Override
            public void onComplete(Bundle bundle) {
                showDialog();
//                HashMap<String, String> params = new HashMap<>();
                final String uid = bundle.getString("uid");
//                params.put("uid", uid);
//                params.put("access_token", bundle.getString("access_token"));
                OkHttpUtils.get("https://api.weibo.com/2/users/show.json?access_token=" + bundle.getString("access_token") + "&uid="+uid)
                        .tag(this)
                        .cacheMode(CacheMode.NO_CACHE)
//                        .params(params)
                        .execute(new AbsCallback<String>() {
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                if(!isFinishing()) {
                                    dismissProgressDialog();
                                }
                            }

                            @Override
                            public String parseNetworkResponse(Response response) throws Exception {
                                String responseData = response.body().string();
                                if (TextUtils.isEmpty(responseData)) return null;
                                return responseData;
                            }

                            @Override
                            public void onSuccess(String userInfo, Call call, Response response) {
                                if(!isFinishing()) {
                                    dismissProgressDialog();
                                    if(userInfo != null) {
                                        JSONObject jsonObject =  JSONObject.parseObject(userInfo);

                                        doThirdLogin(uid,jsonObject.getString("screen_name"),jsonObject.getString("profile_image_url"),"",4);
                                    }
                                }
                            }
                        });
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.shortToast(R.string.fail);
            }

            @Override
            public void onCancel() {

            }
        });
    }
    IUiListener loginListener;
    @OnClick(R.id.qq_login)
    void onqqLogin() {
        final QQAccess qqAccess = new QQAccess(this, ShareConstant.QQAPPID);
        loginListener = new IUiListener() {

            @Override
            public void onComplete(Object response) {
                org.json.JSONObject jsonResponse = (org.json.JSONObject) response;
                if (null != jsonResponse && jsonResponse.length() == 0) {
                    ToastUtil.shortToast(LoginActivity.this, "返回为空, 登录失败");
                    return;
                }
                assert jsonResponse != null;
                final String openId = jsonResponse.optString("openid");
                qqAccess.initOpenidAndToken(jsonResponse);
                qqAccess.getUserInfoQQ(new IUiListener() {

                    @Override
                    public void onComplete(Object qquser) {
                        org.json.JSONObject jsonResponse = (org.json.JSONObject) qquser;
                        doThirdLogin(openId,jsonResponse.optString("nickname"),jsonResponse.optString("figureurl_qq_2"),"",3);
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void onError(UiError uiError) {
                ToastUtil.shortToast(R.string.fail);

            }

            @Override
            public void onCancel() {
                ToastUtil.shortToast(R.string.cancel_order);
            }
        };
        qqAccess.loginQQ(loginListener);
    }

    @OnClick(R.id.wechat_login)
    void onWechatLogin() {
        WeiXinAccess weiXinAccess = new WeiXinAccess(AppLib.getContext(), ShareConstant.WEXINAPPID);
        weiXinAccess.loginWeiXin();
    }

    @OnClick(R.id.forget_psw)
    void onForgetPsw() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegisterFragment.FLAG, false);
        PageSwitcher.switchToTopNavPage(this, RegisterFragment.class,bundle,getString(R.string.find_psw),"");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginWeiXin(EventOfLoginWeixin event) {
        if(isFinishing())
            return;
        doThirdLogin("","","",event.code, 2);
    }
    /**
     * 登录成功
     */
    private void loginSucceed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void doThirdLogin(String uid, String nickname, String avatar, String code, int type) {
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("nickname", nickname);
        params.put("avatar", avatar);
        params.put("code", code);
        params.put("type", String.valueOf(type));
//        final JSONObject jsonObject = new JSONObject(params);
        OkHttpUtils.post(ApiConstants.THIRD_LOGIN)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
//                .postJson(jsonObject.toString())
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if(!isFinishing()) {
                            dismissProgressDialog();
                        }
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo, Call call, Response response) {
                        if(!isFinishing()) {
                            dismissProgressDialog();
                            if(userInfo != null) {
                                L.d("@@@@@>>" + userInfo.token);
                                ToastUtil.shortToast(R.string.login_success);
                                BaseApplication.getUserMgr().saveUserInfo(userInfo);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
    }
    /**
     * 登陆
     */
    private void doLogin() {

        String phone = login_phone_number.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.shortToast(R.string.hint_login_username);
            return;
        }
        if (!Util.isMobileNO(phone)) {
            ToastUtil.shortToast(R.string.enter_mobile_error);
            return;
        }
        String psw = login_psw.getText().toString().trim()
                .replaceAll(" ", "");
        if (TextUtils.isEmpty(psw)) {
            ToastUtil.shortToast(R.string.hint_login_pwd);
            return;
        }
        showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put("username", phone);
        params.put("password", MD5Util.md5Hex(psw));
//        final JSONObject jsonObject = new JSONObject(params);
        OkHttpUtils.post(ApiConstants.LOGIN)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
//                .postJson(jsonObject.toString())
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if(!isFinishing()) {
                            dismissProgressDialog();
                        }
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo, Call call, Response response) {
                        if(!isFinishing()) {
                            dismissProgressDialog();
                            if(userInfo != null) {
                                L.d("@@@@@>>" + userInfo.token);
                                ToastUtil.shortToast(R.string.login_success);
                                BaseApplication.getUserMgr().saveUserInfo(userInfo);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });

    }

    private void showDialog() {
        CustomProgressDialog loginDialog = mLoginDialog;
        if (null == loginDialog) {
            loginDialog = new CustomProgressDialog(this);
            mLoginDialog = loginDialog;
            loginDialog.setTipText(R.string.login_ing_tip);
            loginDialog.setCancelable(false);
            loginDialog.setCanceledOnTouchOutside(false);
            loginDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    onBackPressed();
                }
            });
        }
        loginDialog.show();
    }

    /**
     * 关闭登录提示框
     */
    private void dismissProgressDialog() {
        if (null != mLoginDialog) {
            mLoginDialog.dismiss();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }
        if(sinaAccess == null)
            return;
        if (sinaAccess.getmSsoHandler() != null) {
            sinaAccess.getmSsoHandler().authorizeCallBack(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
