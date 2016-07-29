package com.gjj.shop.shopping;

import android.widget.TextView;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by Chuck on 2016/7/21.
 */
public class ShoppingFragment extends BaseFragment {
    @Bind(R.id.tv_title)
    TextView mTitleTV;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_shopping;
    }

    @Override
    public void initView() {
        mTitleTV.setText(R.string.shopping);
    }
}
