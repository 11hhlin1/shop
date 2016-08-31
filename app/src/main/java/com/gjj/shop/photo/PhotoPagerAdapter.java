package com.gjj.shop.photo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.net.UrlUtil;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.ReusablePhotoView;

/**
 * @Title:过家家-项目经理
 * @Description:大图浏览适配器
 * @Copyright: Copyright (c) 2015
 * @Company: 深圳市过家家
 * @version: 1.0.0.0
 * @author: len、jack
 * @createDate 2015-6-25
 */
public class PhotoPagerAdapter extends PagerAdapter implements OnPhotoTapListener {

    private ArrayList<PhotoData> mPhotoDataList = null;
    //    private ReusablePhotoView[] mViews;
    private FrameLayout[] mFrameLayouts;
    private Activity mActivity;

    public PhotoPagerAdapter(Activity act, ArrayList<PhotoData> photoDataList) {
        mActivity = act;
        mPhotoDataList = photoDataList;
        // mViews = new ReusablePhotoView[4];
        mFrameLayouts = new FrameLayout[4];
    }

    @Override
    public int getCount() {
        return mPhotoDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int index = position % 4;
        // ReusablePhotoView photoView = mViews[index];
        FrameLayout frameLayout = mFrameLayouts[index];
        final ViewHolder holder;
        if (frameLayout == null) {
            Context context = mActivity;
            frameLayout = new FrameLayout(context);
            holder = new ViewHolder();
            frameLayout.setTag(holder);
            ReusablePhotoView photoView = new ReusablePhotoView(context);
            holder.photoView = photoView;
            // photoView.setBackgroundColor(Color.BLACK);
            photoView.setOnPhotoTapListener(this);
            photoView.setScaleType(ScaleType.CENTER);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            photoView.setLayoutParams(params);

            ProgressBar progressBar = new ProgressBar(context);
            holder.progressBar = progressBar;

//            Bitmap bmp = BitmapFactory.decodeResource(container.getContext().getResources(),R.drawable.progressbar_loading);
//            Drawable drawable =new BitmapDrawable(bmp);
//            progressBar.setIndeterminateDrawable(drawable);
            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params1.gravity = Gravity.CENTER;
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setLayoutParams(params1);
            frameLayout.addView(photoView);
            frameLayout.addView(progressBar);
            mFrameLayouts[index] = frameLayout;

        } else {
            holder = (ViewHolder) frameLayout.getTag();
        }
        final PhotoData data = mPhotoDataList.get(position);
//        final DrawableRequestBuilder<String> thumbnailRequest = Glide
//                .with(mActivity)
//                .load(data.thumbUrl);
        Glide.with(mActivity)//activty
                .load(data.photoUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        if(bitmap != null) {
                            int height = bitmap.getHeight();
                            int width = bitmap.getWidth();
                            int imageHeight = Util.getScreenHeight(mActivity) * width / height;
                            if(imageHeight > Util.getScreenHeight(mActivity)) {
                                imageHeight = Util.getScreenHeight(mActivity);
                            }
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                                    imageHeight);
                            params.gravity = Gravity.CENTER;
                            holder.photoView.setScaleType(ScaleType.CENTER);
                            holder.photoView.setLayoutParams(params);
                        }
                        Glide.with(mActivity)
                                .load(data.photoUrl)
                                .fitCenter()
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        holder.progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .placeholder(new ColorDrawable(mActivity.getResources().getColor(R.color.effect_tm_black)))
                                .error(new ColorDrawable(mActivity.getResources().getColor(R.color.effect_tm_black)))
//                                .thumbnail(thumbnailRequest)
                                .into(holder.photoView);
                    }

                });

        container.removeView(frameLayout);
        container.addView(frameLayout);
        return frameLayout;
    }

    private class ViewHolder {
        ReusablePhotoView photoView;
        ProgressBar progressBar;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        mActivity.finish();
        // mActivity.overridePendingTransition(0, R.anim.fade_out);
    }
}
