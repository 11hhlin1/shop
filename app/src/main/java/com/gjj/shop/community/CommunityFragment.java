package com.gjj.shop.community;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/7/21.
 */
public class CommunityFragment extends BaseFragment{

//    @Bind(R.id.fl_empty_view)
//    FrameLayout mFlEmptyView;
    @Bind(R.id.recyclerView)
    RecyclerViewFinal mRecyclerView;
    @Bind(R.id.community_layout)
    PtrClassicFrameLayout mPtrLayout;
    @Bind(R.id.add_feed_btn)
    ImageView mAddFeed;
    @Bind(R.id.tv_title)
    TextView mTitleTV;

    @OnClick(R.id.add_feed_btn)
    void addFeed() {
        PageSwitcher.switchToTopNavPage(getActivity(),AddFeedFragment.class,null,getString(R.string.share),getString(R.string.commit));

    }
    private List<CommunityInfo> mCommunityInfoList;
    private CommunityAdapter mAdapter;
    private int mPage = 1;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_community;
    }

    @Override
    public void initView() {
        mTitleTV.setText(R.string.community);
        mCommunityInfoList = new ArrayList<>();
        mAdapter = new CommunityAdapter(getContext(), mCommunityInfoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.setEmptyView(mFlEmptyView);
        mRecyclerView.setAdapter(mAdapter);

        setSwipeRefreshInfo();
    }

    private void setSwipeRefreshInfo() {
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                requestData(mAdapter.getItemCount() - 1);
            }
        });

        mPtrLayout.setLastUpdateTimeRelateObject(this);
        mPtrLayout.disableWhenHorizontalMove(true);
        mPtrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData(0);
            }
        });
        mPtrLayout.autoRefresh();
    }

    private void requestData(final int start) {
      //TODO
        HashMap<String, String> params = new HashMap<>();
        params.put("index", String.valueOf(start));
        params.put("size", String.valueOf(20));
        OkHttpUtils.post(ApiConstants.COMMUNITY_LIST)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
//                .postJson(jsonObject.toString())
                .execute(new JsonCallback<CommunityInfoList>(CommunityInfoList.class) {
                    @Override
                    public void onResponse(boolean isFromCache, CommunityInfoList rspInfo, Request request, @Nullable Response response) {

                        if(start == 0) {
                            mAdapter.setData(rspInfo.list);
                        } else {
                        }
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {

                    }
                });

    }
}
