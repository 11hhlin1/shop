package com.gjj.shop.main;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseMainActivity;
import com.gjj.shop.index.IndexFragment;
import com.gjj.shop.widget.NestRadioGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseMainActivity {

    private static final String SAVE_INSTANCE_STATE_KEY_TAB_ID = "tabId";
    @Bind(R.id.top_layout)
    RelativeLayout mTitleBar;

    @Bind(R.id.group_tab)
    NestRadioGroup mRadioGroup;

    @Bind(R.id.tv_title)
    TextView mTitleTV;
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
            case R.id.index_tab:
                mRadioGroup.check(R.id.index_tab);
                break;
            case R.id.community_tab:
                mRadioGroup.check(R.id.community_tab);
                break;
            case R.id.shopping_tab:
                mRadioGroup.check(R.id.shopping_tab);
                break;
            default:
                mRadioGroup.check(R.id.person_tab);
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

    }

    private void showShoppingTab() {

    }

    private void showCommunityTab() {

    }

    private void showIndexTab() {
        replaceFragment(IndexFragment.class);
    }

}
