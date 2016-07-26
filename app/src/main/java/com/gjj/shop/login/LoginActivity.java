package com.gjj.shop.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.gjj.shop.R;
import com.gjj.shop.main.MainActivity;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;


/**
 * Created by chuck on 16/7/17.
 */
public class LoginActivity extends Activity {
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
    Button login_btn;


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
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }
   // @OnClick(R.id.login_register_tv)
    void onClickGoReg() {
        //PageSwitcher.switchToTopNavPage(thiss, RegisterSubmitFragment.class, null, "", "注册账号", null);
    }

    /**
     * 短信验证有效时间ms
     */
    private static final int SMS_VALIDITY = 60000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    /**
     * 获取短信验证码
     */
    private void getAppSmsToken() {
        //String phone = login_phone_number_edit.getText().toString().trim().replaceAll(" ", "");
        //if (TextUtils.isEmpty(phone)) {
            //YylApp.showToast("请输入正确的手机号");
           // return;
        //}
        //login_security_code_edit.setText("");
        //Request request = YylRequestFactory.getMobileRegisterToken(phone);
        //YylRequestManager.getInstance().execute(request, this);
    }



    /**
     * 登录成功
     */
    private void loginSucceed() {
       /* Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setAttributes(params);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/
        Intent i = new Intent(this, MainActivity.class);
        // if (GjjApp.getInstance().getLockPatternUtils().savedPatternExists()) {
        // i.setClass(this, UnlockGesturePasswordActivity.class);
        // } else {
        // i.setClass(this, GuideGesturePasswordActivity.class);
        // }
        //延迟关闭登录页面，因为MainActivity背景透明，可能在做动画过程中看到桌面
//        MainTaskExecutor.scheduleTaskOnUiThread(500, new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        });
        startActivity(i);
        finish();
    }


    class SmsRequestParam {
        String mobile;
    }



    /**
     * 设置按钮可用
     *
     * @param ms
     */


    private String sendSmsCountDown(long ms) {
        return String.format("%s" + "再次发送", ms);
    }

    /**
     * 登陆
     */
    private void doLogin() {
        String phone = login_phone_number.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(phone)) {
            //YylApp.showToast("请输入正确的手机号码");
            return;
        }
        String smscode = login_psw.getText().toString().trim()
                .replaceAll(" ", "");
        if (TextUtils.isEmpty(smscode)) {
           // YylApp.showToast("请出入验证码");
            return;
        }

        OkHttpUtils.post(ApiConstants.LOGIN);
    }

    class LoginRequestParam {
        String sms;
        String mobile;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
