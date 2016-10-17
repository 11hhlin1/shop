package com.gjj.shop.index;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.gjj.applibrary.http.callback.ListCallback;
import com.gjj.applibrary.http.model.BaseList;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.SpaceItemDecoration;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.ApiConstants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import okhttp3.Call;
import okhttp3.Response;

import static com.gjj.shop.community.CommunityFragment.SIZE;

/**
 * Created by user on 16/8/27.
 */
public class UserAdviceFragment extends BaseFragment{

    @Bind(R.id.recyclerView)
    RecyclerViewFinal mRecyclerView;
    private UserAdviceAdapter mAdapter;
    private ProductInfo mProductInfo;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_user_advice;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        mProductInfo = (ProductInfo) bundle.getSerializable("product");
        mAdapter = new UserAdviceAdapter(getContext(), new ArrayList<CommentInfo>());
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
                requestData(mAdapter.getItemCount());
            }
        });
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_20p);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mRecyclerView.setItemAnimator(null);
        requestData(0);
//        mPtrLayout.setLastUpdateTimeRelateObject(this);
////        mPtrLayout.disableWhenHorizontalMove(true);
////        mPtrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
////            @Override
////            public void onRefreshBegin(PtrFrameLayout frame) {
////                requestData(0);
////            }
////        });
//        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                requestData(0);
//            }
//        });
//        mPtrLayout.autoRefresh();
    }

    private void requestData(final int start) {
        HashMap<String, String> params = new HashMap<>();
        params.put("goodsId", mProductInfo.goodsId);
        params.put("index", String.valueOf(start));
        params.put("size", String.valueOf(SIZE));
        OkHttpUtils.get(ApiConstants.COMMENT_INFOLIST)
                .tag(this)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(new ListCallback<CommentInfo>(CommentInfo.class) {
                    @Override
                    public void onSuccess(BaseList<CommentInfo> communityInfoBaseList, Call call, Response response) {
                        handleData(start,communityInfoBaseList);
                    }

                    @Override
                    public void onCacheSuccess(BaseList<CommentInfo> communityInfoBaseList, Call call) {
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
//                                    mPtrLayout.refreshComplete();
                                } else {
                                    mRecyclerView.onLoadMoreComplete();
                                }
                                ToastUtil.shortToast(R.string.fail);
                            }
                        });

                    }

                });
    }


    void handleData(final int start, final BaseList<CommentInfo> baseList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (start == 0) {
//                    mPtrLayout.refreshComplete();
                } else {
                    mRecyclerView.onLoadMoreComplete();
                }
                List<CommentInfo> infoList = new ArrayList<CommentInfo>();
                if(baseList != null) {
                    infoList = baseList.list;
                }
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
        });
    }
}
