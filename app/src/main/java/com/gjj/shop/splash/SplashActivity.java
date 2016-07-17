package com.gjj.shop.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gjj.applibrary.task.MainTaskExecutor;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.shop.login.LoginActivity;
import com.gjj.shop.login.RegisterActivity;
import com.gjj.shop.main.MainActivity;
import com.gjj.shop.R;
import com.gjj.shop.net.BundleKey;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MainTaskExecutor.scheduleTaskOnUiThread(500, new Runnable() {
            @Override
            public void run() {
                String token = PreferencesManager.getInstance(SplashActivity.this).get(BundleKey.TOKEN);

                if(TextUtils.isEmpty(token)) {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

}
