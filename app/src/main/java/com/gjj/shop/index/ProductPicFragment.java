package com.gjj.shop.index;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.util.Util;
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
        assert mProductInfo != null;
        productTitle.setText(mProductInfo.title);
        productDesc.setText(mProductInfo.details);
        Activity activity = getActivity();
        List<String> imageList = mProductInfo.imageList;
        if(Util.isListEmpty(imageList) || imageList.size() < 4)
            return;
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(imageList.get(0)))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(image01);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(imageList.get(1)))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(image02);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(imageList.get(2)))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(image03);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(imageList.get(3)))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .into(image04);
    }

}
