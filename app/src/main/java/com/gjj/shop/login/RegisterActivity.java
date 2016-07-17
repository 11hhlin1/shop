package com.gjj.shop.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.task.ForegroundTaskExecutor;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.shop.R;
import com.gjj.shop.main.MainActivity;
import com.gjj.shop.model.RegisterResponse;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.net.BundleKey;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 16/7/17.
 */
public class RegisterActivity extends Activity {

    /**
     * 输入手机号
     */
    @Bind(R.id.et_username)
    EditText mUsername;
    /**
     * 密码
     */
    @Bind(R.id.et_psw)
    EditText mPsw;

    /**
     * 密码
     */
    @Bind(R.id.et_sms_code)
    EditText mSmsCode;

    @Bind(R.id.btn_register)
    Button mRegister;

    @OnClick(R.id.btn_register)
    void register() {

        HashMap<String, String> params = new HashMap<>();
        params.put("username", mUsername.getText().toString());
        params.put("password", mPsw.getText().toString());
        params.put("phoneCode",mSmsCode.getText().toString());
        final JSONObject jsonObject = new JSONObject(params);

//        ForegroundTaskExecutor.executeTask(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    readContentFromChunkedPost(ApiConstants.REGISTER, jsonObject.toString());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        OkHttpUtils.post(ApiConstants.REGISTER)//
                .tag(this)//
                .postJson(jsonObject.toString())//
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onResponse(boolean isFromCache, UserInfo rspInfo, Request request, @Nullable Response response) {
                        // requestInfo 对象即为所需要的结果对象
//                      UserInfo baseApiResponse = rspInfo.rspInfogetData();
                        if(rspInfo != null) {
                            L.d("@@@@@>>", rspInfo.token);
                            PreferencesManager.getInstance(RegisterActivity.this).put(BundleKey.TOKEN, rspInfo.token);
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        // UI 线程，请求失败后回调
                        // isFromCache 表示当前回调是否来自于缓存
                        // call        本次网络的请求对象，可以根据该对象拿到 request
                        // response    本次网络访问的结果对象，包含了响应头，响应码等，如果网络异常 或者数据来自于缓存，该对象为null
                        // e           本次网络访问的异常信息，如果服务器内部发生了错误，响应码为 400~599之间，该异常为 null
                        L.d("@@@@@>>", response.code());
                    }
                });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

}
