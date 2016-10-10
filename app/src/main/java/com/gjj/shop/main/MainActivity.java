package com.gjj.shop.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.task.MainTaskExecutor;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.app.BaseApplication;
import com.gjj.shop.base.BaseMainActivity;
import com.gjj.shop.community.CommunityFragment;
import com.gjj.shop.event.EventOfChangeTab;
import com.gjj.shop.event.EventOfLogout;
import com.gjj.shop.index.IndexFragment;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.person.PersonalFragment;
import com.gjj.shop.shopping.ShoppingFragment;
import com.gjj.shop.widget.NestRadioGroup;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
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
        EventBus.getDefault().register(this);
        getUserInfo();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeTab(EventOfChangeTab event) {
        if(isFinishing())
            return;
        switch (event.mIndex) {
            case 2:
                mRadioGroup.check(R.id.shopping_tab);
                break;
            case 0:
                mRadioGroup.check(R.id.index_tab);
                break;
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_INSTANCE_STATE_KEY_TAB_ID, mRadioGroup.getCheckedRadioButtonId());
    }

    private void showPersonTab() {

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

    private void getUserInfo() {
        OkHttpUtils.post(ApiConstants.GET_USER_INFO)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if(response != null)
                            L.d("@@@@@>>" + response.code());
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo, Call call, Response response) {
                        if(userInfo != null) {
                            L.d("@@@@@>>" + userInfo.phone);
                            BaseApplication.getUserMgr().saveUserInfo(userInfo);
                        }
                    }
                });
    }
}
