package com.gjj.shop.index;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.order.EditOrderFragment;
import com.gjj.shop.util.CallUtil;
import com.gjj.shop.widget.NavLineView;
import com.gjj.shop.widget.UnScrollableGridView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 16/8/27.
 */
public class ProductDetailFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @Bind(R.id.buy_now_btn)
    Button buyNowBtn;
    @Bind(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @Bind(R.id.logo)
    ImageView logo;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.new_price)
    TextView newPrice;
    @Bind(R.id.old_price)
    TextView oldPrice;
    @Bind(R.id.choose_detail_item)
    RelativeLayout chooseDetailItem;
    @Bind(R.id.product_avatar_iv)
    ImageView productAvatarIv;
    @Bind(R.id.detail_item)
    RelativeLayout detailItem;
    @Bind(R.id.phone_item)
    RelativeLayout phoneItem;
    @Bind(R.id.contact_tv)
    TextView contactTv;
    @Bind(R.id.pic_detail_tv)
    TextView picDetailTv;
    @Bind(R.id.user_advice_tv)
    TextView userAdviceTv;
    @Bind(R.id.project_radio_group)
    LinearLayout projectRadioGroup;
    @Bind(R.id.bottom_line)
    View bottomLine;
    @Bind(R.id.tab_line_iv)
    NavLineView mNavLineView;
    @Bind(R.id.top_rl)
    RelativeLayout topRl;
    @Bind(R.id.page_vp)
    ViewPager mPageVp;
    private ProductDetailFragmentAdapter mFragmentAdapter;

    private Fragment[] mFragmentCache;
    private int mRedColor;
    private int mSecondaryGrayColor;
    private int mNavItemWidth;
    private PopupWindow mPickUpPopWindow;
    private ProductInfo mProductInfo;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_product_detail;
    }

    @Override
    public void initView() {
        Resources res = getResources();
        mRedColor = res.getColor(R.color.color_EE394A);
        mSecondaryGrayColor = res.getColor(R.color.secondary_gray);

        mFragmentCache = new Fragment[2];
        initTabLineWidth();
        ViewPager viewPager = mPageVp;
        viewPager.setCurrentItem(0);
        picDetailTv.setTextColor(mRedColor);
        //mDoneRadioBtn.setTextColor(mSecondaryGrayColor);
        viewPager.setOffscreenPageLimit(mFragmentCache.length);
        viewPager.setOnPageChangeListener(this);
        Bundle bundle = getArguments();
        mProductInfo = (ProductInfo) bundle.getSerializable("product");
        mFragmentAdapter = new ProductDetailFragmentAdapter(getChildFragmentManager(), mFragmentCache, bundle);
        mPageVp.setAdapter(mFragmentAdapter);
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        title.setText(mProductInfo.name);
        newPrice.setText(getString(R.string.money_has_mark, Util.getFormatData(mProductInfo.curPrice)));
        oldPrice.setText(getString(R.string.money_has_mark, Util.getFormatData(mProductInfo.prePrice)));
        contactTv.setText(mProductInfo.contactPhone);
        Activity activity = getActivity();
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(mProductInfo.logo))
                .centerCrop()
                .placeholder(R.mipmap.cp)
                .error(R.mipmap.cp)
                .into(logo);
        shopName.setText(mProductInfo.shopName);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(mProductInfo.shopThumb))
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(activity))
                .placeholder(R.mipmap.sjlogo)
                .error(R.mipmap.sjlogo)
                .into(productAvatarIv);
//        String mGoodsId = bundle.getString("goodsId");
//        showLoadingDialog(R.string.loading_view_loading,true);
//        HashMap<String, String> params = new HashMap<>();
//        params.put("goodsId", mGoodsId);
//        OkHttpUtils.get(ApiConstants.PRODUCT_INFO)
//                .tag(this)
//                .cacheMode(CacheMode.NO_CACHE)
//                .params(params)
//                .execute(new JsonCallback<ProductInfo>(ProductInfo.class) {
//                    @Override
//                    public void onResponse(boolean isFromCache, ProductInfo productInfo, Request request, @Nullable Response response) {
//
//                        dismissLoadingDialog();
//                        ToastUtil.shortToast(R.string.success);
//
//                    }
//
//                    @Override
//                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
//                        super.onError(isFromCache, call, response, e);
//                        dismissLoadingDialog();
//                        ToastUtil.shortToast(R.string.fail);
//                    }
//                });

    }


    @OnClick({R.id.buy_now_btn, R.id.choose_detail_item, R.id.phone_item, R.id.user_advice_tv, R.id.pic_detail_tv, R.id.icon_back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buy_now_btn:
                PageSwitcher.switchToTopNavPage(getActivity(), EditOrderFragment.class, null, getString(R.string.check_order), "");
                break;
            case R.id.choose_detail_item:
                showPickupWindow();
                break;
            case R.id.phone_item:
                CallUtil.askForMakeCall(getActivity(), "", "400-82838838888");
                break;
            case R.id.user_advice_tv:
                mPageVp.setCurrentItem(1);
                break;
            case R.id.pic_detail_tv:
                mPageVp.setCurrentItem(0);
                break;
            case R.id.icon_back_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float left = mNavItemWidth * (position + positionOffset);
        NavLineView navLineView = mNavLineView;
        navLineView.setNavLeft(left);
        navLineView.setNavRight(left + mNavItemWidth);
        navLineView.invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                userAdviceTv.setTextColor(mSecondaryGrayColor);
                picDetailTv.setTextColor(mRedColor);
                break;
            case 1:
                picDetailTv.setTextColor(mSecondaryGrayColor);
                userAdviceTv.setTextColor(mRedColor);
                break;

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置滑动条的宽度为屏幕的1/mFragmentList.size();(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mNavItemWidth = dm.widthPixels / mFragmentCache.length;
    }

    /**
     * 显示选择框
     */
    @SuppressWarnings("unused")
    private void showPickupWindow() {
        // dismissConstructNoticeWindow();
        View contentView;
        PopupWindow popupWindow = mPickUpPopWindow;
        if (popupWindow == null) {
            contentView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.choose_detail_pop, null);
            ViewHolder viewHolder = new ViewHolder(contentView);
            viewHolder.popTitle.setText(mProductInfo.name);
            viewHolder.popNewPrice.setText(getString(R.string.money_has_mark, Util.getFormatData(mProductInfo.curPrice)));
            viewHolder.popOldPrice.setText(getString(R.string.money_has_mark, Util.getFormatData(mProductInfo.prePrice)));
            Glide.with(getActivity())
                    .load(UrlUtil.getHttpUrl(mProductInfo.logo))
                    .centerCrop()
                    .placeholder(R.mipmap.cp_gg)
                    .error(R.mipmap.cp_gg)
                    .into(viewHolder.popLogoIv);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissConstructNoticeWindow();
                }
            });
            Rect r = new Rect();
            mRootView.getWindowVisibleDisplayFrame(r);
            final int[] location = new int[2];
            mRootView.getLocationOnScreen(location);
            int height = Util.dip2px(60);
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, r.bottom
                    - location[1] - height, false);
            mPickUpPopWindow = popupWindow;
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
            // mPickUpPopWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
        } else {
            contentView = popupWindow.getContentView();
        }
        //判读window是否显示，消失了就执行动画
        if (!popupWindow.isShowing()) {
            Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.effect_bg_show);
            contentView.startAnimation(animation2);
        }
        popupWindow.showAtLocation(contentView, Gravity.TOP, 0, 0);

    }

    /**
     * 取消工程消息弹出框
     *
     * @return
     */
    private void dismissConstructNoticeWindow() {
        PopupWindow pickUpPopWindow = mPickUpPopWindow;
        if (null != pickUpPopWindow && pickUpPopWindow.isShowing()) {
            pickUpPopWindow.dismiss();
        }
    }

    class ViewHolder {
        @Bind(R.id.pop_title)
        TextView popTitle;
        @Bind(R.id.pop_new_price)
        TextView popNewPrice;
        @Bind(R.id.pop_old_price)
        TextView popOldPrice;
        @Bind(R.id.pop_logo_iv)
        ImageView popLogoIv;
        @Bind(R.id.close_pop)
        ImageView closePop;
        @Bind(R.id.plus_btn)
        ImageView plusBtn;
        @Bind(R.id.product_amount)
        TextView productAmount;
        @Bind(R.id.sub_btn)
        ImageView subBtn;
        @Bind(R.id.amount_ll)
        LinearLayout amountLl;
        @Bind(R.id.product_grid)
        UnScrollableGridView productGrid;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            popOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            closePop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissConstructNoticeWindow();
                }
            });
        }
    }


}
