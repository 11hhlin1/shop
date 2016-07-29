package com.gjj.shop.person;

import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by Chuck on 2016/7/21.
 */
public class PersonalFragment extends BaseFragment{
    @Bind(R.id.tv_title)
    TextView mTitleTV;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_person;
    }

    @Override
    public void initView() {
        mTitleTV.setText(R.string.person);
    }
}
