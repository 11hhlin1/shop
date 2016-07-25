package com.gjj.shop.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.gjj.applibrary.task.MainTaskExecutor;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.R;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.widget.UnScrollableGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * Created by Chuck on 2016/7/21.
 */
public class IndexFragment extends BaseFragment {

    @Bind(R.id.store_house_ptr_frame)
    PtrClassicFrameLayout mPtrClassicFrameLayout;

    @Bind(R.id.convenientBanner)
    ConvenientBanner mBanner;

//    @Bind(R.id.advice_list)
//    RecyclerViewFinal mAdviceList;
    @Bind(R.id.product_grid)
    UnScrollableGridView mUnScrollableGridView;

    @Bind(R.id.scrollView)
    ScrollView mScrollView;


    private AdviceProductAdapter mProductAdapter;
    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };
    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_index;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPtrClassicFrameLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        List<String> stringList = new ArrayList<>();
        for (String image: images) {
            stringList.add(image);
        }
        mBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, stringList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
        mBanner.startTurning(5000);
        mBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });


//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
//                LinearLayoutManager.VERTICAL);
//        // 设置布局管理器
//        mAdviceList.setLayoutManager(staggeredGridLayoutManager);
        ArrayList<ProductInfo> list = new ArrayList<ProductInfo>();
        for (int i = 0; i< 10; i++) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.mName = "的撒旦";
            productInfo.mUrl = images[0];
            list.add(productInfo);
        }
//        AdviceProductAdapter productAdapter = new AdviceProductAdapter(getActivity(), list);
//        mProductAdapter = productAdapter;
//        mAdviceList.setItemAnimator(null);
//        mAdviceList.setAdapter(productAdapter);
        GridAdapter gridAdapter = new GridAdapter(getActivity(), list);
        mUnScrollableGridView.setAdapter(gridAdapter);
        MainTaskExecutor.scheduleTaskOnUiThread(500, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        });
    }


}