package com.gjj.shop.community;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.gjj.applibrary.http.callback.CommonCallback;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.json.JSONObject;

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
    private static final int SIZE = 20;

    @OnClick(R.id.add_feed_btn)
    void addFeed() {
        PageSwitcher.switchToTopNavPage(getActivity(),AddFeedFragment.class,null,getString(R.string.share),getString(R.string.commit));

    }
    private List<CommunityInfo> mCommunityInfoList;
    private CommunityAdapter mAdapter;

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
        HashMap<String, String> params = new HashMap<>();
        params.put("index", String.valueOf(start));
        params.put("size", String.valueOf(SIZE));
        OkHttpUtils.get(ApiConstants.COMMUNITY_LIST)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .execute(new CommonCallback<CommunityInfoList>() {
                    @Override
                    public CommunityInfoList parseNetworkResponse(Response response) throws Exception {
                        String responseData = response.body().string();
                        if (TextUtils.isEmpty(responseData)) return null;
                        JSONObject jsonObject = new JSONObject(responseData);
                        final String msg = jsonObject.optString("msg", "");
                        final int code = jsonObject.optInt("code", 0);
                        String data = jsonObject.optString("data", "");
                        switch (code) {
                            case 0:
                                ArrayList<CommunityInfo> list = JSON.parseObject(data, new TypeReference<ArrayList<CommunityInfo>>() {
                                });
                                CommunityInfoList communityInfoList = new CommunityInfoList();
                                communityInfoList.list = list;
                                return communityInfoList;
                            default:
                                throw new IllegalStateException("错误代码：" + code + "，错误信息：" + msg);
                        }
                    }

                    @Override
                    public void onResponse(boolean isFromCache, CommunityInfoList communityInfoList, Request request, @Nullable Response response) {
                        if (start == 0) {
                            mPtrLayout.onRefreshComplete();
                        } else {
                            mRecyclerView.onLoadMoreComplete();
                        }
                        List<CommunityInfo> infoList;
                        infoList = communityInfoList.list;
                        if(start == 0) {
                            mAdapter.setData(infoList);
                        } else {
                            mAdapter.addData(infoList);
                        }
                        if(infoList.size() < SIZE) {
                            mRecyclerView.setHasLoadMore(false);
                        } else {
                            mRecyclerView.setHasLoadMore(true);
                        }
                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        if (start == 0) {
                            mPtrLayout.onRefreshComplete();
                        } else {
                            mRecyclerView.onLoadMoreComplete();
                        }
                        if(!isFromCache)
                        ToastUtil.shortToast(R.string.fail);
                    }
                });
//                .postJson(jsonObject.toString())
//                .execute(new CommonCallback<Object>(CommunityInfoList.class) {
//                    @Override
//                    public void onResponse(boolean isFromCache, CommunityInfoList rspInfo, Request request, @Nullable Response response) {
//                        if (start == 0) {
//                            mPtrLayout.onRefreshComplete();
//                        } else {
//                            mRecyclerView.onLoadMoreComplete();
//                        }
//                        List<CommunityInfo> infoList;
//                        infoList = rspInfo.list;
//                        if(start == 0) {
//                            mAdapter.setData(infoList);
//                        } else {
//                            mAdapter.addData(infoList);
//                        }
//                        if(infoList.size() < SIZE) {
//                            mRecyclerView.setHasLoadMore(false);
//                        } else {
//                            mRecyclerView.setHasLoadMore(true);
//                        }
//                    }
//                    @Override
//                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
//                        if (start == 0) {
//                            mPtrLayout.onRefreshComplete();
//                        } else {
//                            mRecyclerView.onLoadMoreComplete();
//                        }
//                        if(!isFromCache)
//                        ToastUtil.shortToast(R.string.fail);
//                    }
//                });

    }

}
