package com.gjj.shop.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gjj.shop.event.EventOfLogout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Chuck on 2016/7/28.
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(mEventReciever);
    }


    Object mEventReciever = new Object() {
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void logout(EventOfLogout event) {
           if (!isFinishing()) {
                finish();
           }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mEventReciever);
    }
}
