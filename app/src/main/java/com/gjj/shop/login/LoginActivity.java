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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.model.BundleKey;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.MD5Util;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.main.MainActivity;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.widget.CustomProgressDialog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.json.JSONObject;

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
        showDialog();
        doLogin();
    }

    @OnClick(R.id.go_register)
    void onClickRegister() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegisterFragment.FLAG, true);
        PageSwitcher.switchToTopNavPage(this,RegisterFragment.class,bundle,getString(R.string.register),"");
    }

    @OnClick(R.id.sina_login)
    void onSinaLogin() {
    }

    @OnClick(R.id.qq_login)
    void onqqLogin() {
    }

    @OnClick(R.id.wechat_login)
    void onWechatLogin() {
    }

    @OnClick(R.id.forget_psw)
    void onForgetPsw() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(RegisterFragment.FLAG, false);
        PageSwitcher.switchToTopNavPage(this, RegisterFragment.class,bundle,getString(R.string.register),"");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    /**
     * 登录成功
     */
    private void loginSucceed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
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
        String psw = login_psw.getText().toString().trim()
                .replaceAll(" ", "");
        if (TextUtils.isEmpty(psw)) {
            ToastUtil.shortToast(R.string.hint_login_pwd);
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("username", phone);
        params.put("password", MD5Util.md5Hex(psw));
        final JSONObject jsonObject = new JSONObject(params);
        OkHttpUtils.post(ApiConstants.LOGIN)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .postJson(jsonObject.toString())
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onResponse(boolean isFromCache, UserInfo rspInfo, Request request, @Nullable Response response) {

                        dismissProgressDialog();
                            if(rspInfo != null) {
                                L.d("@@@@@>>", rspInfo.token);
                                PreferencesManager.getInstance().put(BundleKey.TOKEN, rspInfo.token);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        dismissProgressDialog();
                        if(response != null)
                            L.d("@@@@@>>", response.code());
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
}
