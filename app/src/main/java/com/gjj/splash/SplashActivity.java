package com.gjj.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gjj.applibrary.task.MainTaskExecutor;
import com.gjj.shop.MainActivity;
import com.gjj.shop.R;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MainTaskExecutor.scheduleTaskOnUiThread(500, new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
