package com.gjj.shop.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.model.BundleKey;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.task.MainTaskExecutor;
import com.gjj.applibrary.util.PreferencesManager;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseMainActivity;
import com.gjj.shop.community.CommunityFragment;
import com.gjj.shop.index.IndexFragment;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.person.PersonalFragment;
import com.gjj.shop.shopping.ShoppingFragment;
import com.gjj.shop.widget.NestRadioGroup;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chuck on 16/7/17.
 */
public class MainActivity extends BaseMainActivity {

    private static final String SAVE_INSTANCE_STATE_KEY_TAB_ID = "tabId";

    @Bind(R.id.group_tab)
    NestRadioGroup mRadioGroup;


    private boolean mIsBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRadioGroup.setOnCheckedChangeListener(new NestRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestRadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.index_tab:
                        showIndexTab();
                        break;
                    case R.id.community_tab:
                        showCommunityTab();
                        break;
                    case R.id.shopping_tab:
                        showShoppingTab();
                        break;
                    case R.id.person_tab:
                        showPersonTab();
                        break;
                }
            }
        });

        int tabId = 0;
        if (null != savedInstanceState) {
            tabId = savedInstanceState.getInt(SAVE_INSTANCE_STATE_KEY_TAB_ID);
        }
        switch (tabId) {
            case R.id.person_tab:
                mRadioGroup.check(R.id.person_tab);
                break;
            case R.id.community_tab:
                mRadioGroup.check(R.id.community_tab);
                break;
            case R.id.shopping_tab:
                mRadioGroup.check(R.id.shopping_tab);
                break;
            default:
                mRadioGroup.check(R.id.index_tab);
                break;
        }
//        Glide.with(this).load("http://jcodecraeer.com/uploads/20150327/1427445294447874.jpg")
//                .into(imageView);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_INSTANCE_STATE_KEY_TAB_ID, mRadioGroup.getCheckedRadioButtonId());
    }

    private void showPersonTab() {
        OkHttpUtils.post(ApiConstants.GET_USER_INFO)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onResponse(boolean isFromCache, UserInfo rspInfo, Request request, @Nullable Response response) {

//                        dismissProgressDialog();
                        if(rspInfo != null) {
                            L.d("@@@@@>>", rspInfo.phone);
//                            PreferencesManager.getInstance().put(BundleKey.TOKEN, rspInfo.token);
//                            Intent intent = new Intent();
//                            intent.setClass(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
//                        dismissProgressDialog();
                        if(response != null)
                            L.d("@@@@@>>", response.code());
                    }
                });
        replaceFragment(PersonalFragment.class);
    }

    private void showShoppingTab() {

        replaceFragment(ShoppingFragment.class);

    }

    private void showCommunityTab() {

        replaceFragment(CommunityFragment.class);
    }

    private void showIndexTab() {

        replaceFragment(IndexFragment.class);
    }

    @Override
    public void onBackPressed() {
        if (mIsBackPressed) {
            mIsBackPressed = false;
            finish();
            killApp();
        } else {
            ToastUtil.shortToast(this, R.string.quit_toast);
            mIsBackPressed = true;
            MainTaskExecutor.scheduleTaskOnUiThread(2000, new Runnable() {

                @Override
                public void run() {
                    mIsBackPressed = false;
                }
            });
        }
    }
}
