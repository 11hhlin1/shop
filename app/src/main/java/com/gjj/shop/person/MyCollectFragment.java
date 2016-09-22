package com.gjj.shop.person;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.SpaceItemDecoration;
import com.gjj.shop.community.CommunityFragment;
import com.gjj.shop.community.CommunityInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrDefaultHandler;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by user on 16/8/27.
 */
public class MyCollectFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener,
        SlideView.OnSlideListener {

    @Bind(R.id.list)
    ListViewCompat mListView;
    @Bind(R.id.collect_layout)
    PtrClassicFrameLayout mCollectLayout;
    private SlideView mLastSlideViewWithStatusOn;
    CollectListAdapter mAdapter;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_my_collect;
    }

    @Override
    public void initView() {
        List<CollectInfo> list = new ArrayList<>();
        for (int i=0;i<10;i++) {
            list.add(new CollectInfo());
        }
        mAdapter = new CollectListAdapter(getActivity(), list, this);
        mListView.setAdapter(mAdapter);
    }
    private void setSwipeRefreshInfo() {
        mListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                requestData(mAdapter.getCount());
            }
        });
        mCollectLayout.setLastUpdateTimeRelateObject(this);
        mCollectLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData(0);
            }
        });
        mCollectLayout.autoRefresh();
    }

    private void requestData(final int start) {
        HashMap<String, String> params = new HashMap<>();
        params.put("index", String.valueOf(start));
        params.put("size", String.valueOf(CommunityFragment.SIZE));
        OkHttpUtils.get(ApiConstants.COLLECT_LIST)
                .tag(this)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(new ListCallback<CollectInfo>(CollectInfo.class) {
                    @Override
                    public void onSuccess(BaseList<CollectInfo> communityInfoBaseList, Call call, Response response) {
                        handleData(start,communityInfoBaseList);
                    }

                    @Override
                    public void onCacheSuccess(BaseList<CollectInfo> communityInfoBaseList, Call call) {
                        super.onCacheSuccess(communityInfoBaseList, call);
                        handleData(start,communityInfoBaseList);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (start == 0) {
                                    mCollectLayout.refreshComplete();
                                } else {
                                    mListView.onLoadMoreComplete();
                                }
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });

                    }

                });
    }


    void handleData(final int start, final BaseList<CollectInfo> baseList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (start == 0) {
                    mCollectLayout.refreshComplete();
                } else {
                    mListView.onLoadMoreComplete();
                }
                List<CollectInfo> infoList = new ArrayList<>();
                if(baseList != null) {
                    infoList = baseList.list;
                }
                if(start == 0) {
                    mAdapter.setData(infoList);
                } else {
                    mAdapter.addData(infoList);
                }
                if(infoList.size() < CommunityFragment.SIZE) {
                    mListView.setHasLoadMore(false);
                } else {
                    mListView.setHasLoadMore(true);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete) {
            L.e("onClick v= " + v);
            ToastUtil.shortToast(R.string.delete);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }
}
