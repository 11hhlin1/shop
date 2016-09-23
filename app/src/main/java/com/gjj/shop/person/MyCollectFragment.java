package com.gjj.shop.person;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.base.SpaceItemDecoration;
import com.gjj.shop.community.CommunityFragment;
import com.gjj.shop.community.CommunityInfo;
import com.gjj.shop.event.EventOfAddCartSuccess;
import com.gjj.shop.index.ProductDetailFragment;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;

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
public class MyCollectFragment extends BaseFragment implements CollectListAdapter.ClickCallBack,
        SlideView.OnSlideListener {

    @Bind(R.id.list)
    ListViewCompat mListView;
    @Bind(R.id.collect_layout)
    PtrClassicFrameLayout mCollectLayout;
    private SlideView mLastSlideViewWithStatusOn;
    private CollectListAdapter mAdapter;
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_my_collect;
    }

    @Override
    public void initView() {
        List<CollectInfo> list = new ArrayList<>();
        mAdapter = new CollectListAdapter(getActivity(), list, this);
        mListView.setAdapter(mAdapter);
        mAdapter.setmClickCallBack(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CollectInfo collectInfo = mAdapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("product",collectInfo.product);
            PageSwitcher.switchToTopNavPageNoTitle(getActivity(),ProductDetailFragment.class,bundle,"","");
            }
        });
        setSwipeRefreshInfo();
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
                .execute(new ListCallback<ProductInfo>(ProductInfo.class) {
                    @Override
                    public void onSuccess(BaseList<ProductInfo> communityInfoBaseList, Call call, Response response) {
                        handleData(start,communityInfoBaseList);
                    }

                    @Override
                    public void onCacheSuccess(BaseList<ProductInfo> communityInfoBaseList, Call call) {
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


    void handleData(final int start, final BaseList<ProductInfo> baseList) {
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
                    for (ProductInfo productInfo : baseList.list) {
                        CollectInfo collectInfo = new CollectInfo();
                        collectInfo.product = productInfo;
                        infoList.add(collectInfo);
                    }
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

    private void unCollectGood(String goodsId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("goodsId", goodsId);
        OkHttpUtils.post(ApiConstants.COLLECT_GOOD)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new JsonCallback<String>(String.class) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(R.string.delete_success);
                                requestData(0);
                            }
                        });
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });
                    }
                });
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

    @Override
    public void delete(int pos) {
        CollectInfo collectInfo = mAdapter.getItem(pos);
        unCollectGood(collectInfo.product.goodsId);
    }
}
