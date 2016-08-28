package com.gjj.shop.person;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/14.
 */
public class SettingFragment extends BaseFragment {
    @Bind(R.id.my_news_item)
    RelativeLayout myNewsItem;
    @Bind(R.id.version_item)
    RelativeLayout versionItem;
    @Bind(R.id.clean_data_item)
    RelativeLayout cleanDataItem;
    @Bind(R.id.advice_item)
    RelativeLayout adviceItem;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {

    }


    @OnClick({R.id.my_news_item, R.id.version_item, R.id.clean_data_item, R.id.advice_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_news_item:
                PageSwitcher.switchToTopNavPage(getActivity(),MynewFragment.class,null,getString(R.string.my_news),null);

                break;
            case R.id.version_item:
                ToastUtil.shortToast(getActivity(),"已是最新版本");
                break;
            case R.id.clean_data_item:
                ToastUtil.shortToast(getActivity(),"已经清除");
                break;
            case R.id.advice_item:
                PageSwitcher.switchToTopNavPage(getActivity(),AdviceCommitFragment.class,null,getString(R.string.advice),null);

                break;
        }
    }
}
