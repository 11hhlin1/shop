package com.gjj.shop.index;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.net.UrlUtil;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<IndexData.BannerBean> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, IndexData.BannerBean data) {
        imageView.setImageResource(R.mipmap.ic_test_0);
        Glide.with(context).load(UrlUtil.getHttpUrl(data.getLogo()))
                .into(imageView);
    }

}
