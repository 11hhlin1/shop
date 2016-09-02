package com.gjj.shop.index;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.UrlUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 16/8/27.
 */
public class ProductPicFragment extends BaseFragment {
    @Bind(R.id.product_title)
    TextView productTitle;
    @Bind(R.id.product_desc)
    TextView productDesc;
    @Bind(R.id.image_01)
    ImageView image01;
    @Bind(R.id.image_02)
    ImageView image02;
    @Bind(R.id.image_03)
    ImageView image03;
    @Bind(R.id.image_04)
    ImageView image04;
    private ProductInfo mProductInfo;


    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_product_pic_detail;
    }


    @Override
    public void initView() {
        Bundle bundle = getArguments();
        mProductInfo = (ProductInfo) bundle.getSerializable("product");
        productTitle.setText(mProductInfo.title);
        productDesc.setText(mProductInfo.details);
        Activity activity = getActivity();
        List<String> imageList = mProductInfo.imageList;
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(imageList.get(0)))
                .centerCrop()
                .placeholder(R.mipmap.cpxq_01)
                .error(R.mipmap.cpxq_01)
                .into(image01);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(imageList.get(1)))
                .centerCrop()
                .placeholder(R.mipmap.cpxq_01)
                .error(R.mipmap.cpxq_01)
                .into(image02);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(imageList.get(2)))
                .centerCrop()
                .placeholder(R.mipmap.cpxq_01)
                .error(R.mipmap.cpxq_01)
                .into(image03);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(imageList.get(2)))
                .centerCrop()
                .placeholder(R.mipmap.cpxq_04)
                .error(R.mipmap.cpxq_04)
                .into(image04);
    }

}
