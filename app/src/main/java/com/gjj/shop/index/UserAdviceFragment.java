package com.gjj.shop.index;

import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.BaseSubActivity;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * Created by user on 16/8/27.
 */
public class UserAdviceFragment extends BaseFragment{
    @Bind(R.id.recyclerView)
    RecyclerViewFinal mRecyclerView;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_user_advice;
    }

    @Override
    public void initView() {

    }
}
