package com.gjj.shop.community;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjj.applibrary.http.callback.CommonCallback;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;



import java.util.ArrayList;
import java.util.Collections;
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
                        JSONObject jsonObject = JSON.parseObject(responseData);
                        final String msg = jsonObject.getString("msg");
                        final int code = jsonObject.getIntValue("code");
                        String data = jsonObject.getString("data");
                        switch (code) {
                            case 0:

//                                JSONArray jsonArray = JSONArray.parseArray(data);
//                                int len = jsonArray.size();
//                                ArrayList<CommunityInfo> list = new ArrayList<CommunityInfo>();
//                                for (int i = 0 ; i< len; i++){
//                                    JSONObject object = jsonArray.getJSONObject(i);
//                                    CommunityInfo info = new CommunityInfo();
//                                    info.thumbAvatar = object.getString("thumbAvatar");
//                                    info.nickname = object.getString("nickname");
//                                    info.content = object.getString("content");
//                                    info.time = object.getIntValue("time");
//                                    String [] images = object.getString("imageList").split(",");
////                                    info.imageList = JSON.parseArray(object.getString("imageList"), String.class);
////                                    info.thumbList = JSON.parseArray(object.getString("thumbList"), String.class);
//                                    List<String> imageList = new ArrayList<String>();
//                                    Collections.addAll(imageList, images);
//
//                                    info.imageList = imageList;
//                                    String [] thumbs = object.getString("thumbList").split(",");
//                                    List<String> thumbList = new ArrayList<String>();
//                                    Collections.addAll(thumbList, thumbs);
//                                    info.thumbList = thumbList;
//                                    list.add(info);
//                                }
                                CommunityInfoList communityInfoList = new CommunityInfoList();
                                communityInfoList.list = JSON.parseArray(data, CommunityInfo.class);
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
