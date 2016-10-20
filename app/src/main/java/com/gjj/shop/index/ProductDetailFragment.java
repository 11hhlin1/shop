package com.gjj.shop.index;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.event.EventOfAddCartSuccess;
import com.gjj.shop.event.EventOfChangeTab;
import com.gjj.shop.event.EventOfUpdateTags;
import com.gjj.shop.model.ProductInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.net.UrlUtil;
import com.gjj.shop.order.EditOrderFragment;
import com.gjj.shop.shopping.*;
import com.gjj.shop.util.CallUtil;
import com.gjj.shop.widget.NavLineView;
import com.gjj.shop.widget.UnScrollableListView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by user on 16/8/27.
 */
public class ProductDetailFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @Bind(R.id.buy_now_btn)
    Button buyNowBtn;
    @Bind(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @Bind(R.id.right_btn)
    ImageView mCollectBtn;
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

    @Bind(R.id.tags_tv)
    TextView tagsTv;
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
    private int amount = 1;
//    private Map<String,List<String>> mTags;
    private List<TagInfo> mTagsList;

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
        mTagsList = new ArrayList<>();
        Map<String,List<String>> mTags = JSON.parseObject(mProductInfo.tags, new TypeReference<Map<String, List<String>>>() {});
        if(mTags != null) {
            for (Map.Entry<String, List<String>> entry : mTags.entrySet()) {
                TagInfo tagInfo =new TagInfo();
                tagInfo.mTitle = entry.getKey();
                tagInfo.mList = entry.getValue();
                mTagsList.add(tagInfo);
            }
        }
        newPrice.setText(getString(R.string.money_has_mark, Util.getFormatData(mProductInfo.curPrice)));
        oldPrice.setText(getString(R.string.money_has_mark, Util.getFormatData(mProductInfo.prePrice)));
        contactTv.setText(mProductInfo.contactPhone);
        Activity activity = getActivity();
        Drawable drawable = new ColorDrawable(getResources().getColor(R.color.activity_bg_gray));
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(mProductInfo.logo))
                .centerCrop()
                .error(drawable)
                .into(logo);
        shopName.setText(mProductInfo.shopName);
        Glide.with(activity)
                .load(UrlUtil.getHttpUrl(mProductInfo.shopThumb))
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(activity))
                .error(drawable)
                .into(productAvatarIv);
        EventBus.getDefault().register(this);


    }


    @OnClick({R.id.buy_now_btn, R.id.add_cart_btn,R.id.choose_detail_item, R.id.phone_item, R.id.user_advice_tv, R.id.pic_detail_tv, R.id.icon_back_btn,R.id.right_btn,R.id.go_shopping,R.id.detail_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buy_now_btn:
                buyNow();
                break;
            case R.id.choose_detail_item:
                showPickupWindow();
                break;
            case R.id.phone_item:
                CallUtil.askForMakeCall(getActivity(), "", mProductInfo.contactPhone);
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
            case R.id.add_cart_btn:
                addCart();
                break;
            case R.id.right_btn:
                collectGood();
                break;
            case R.id.detail_item:
                Bundle bundle = new Bundle();
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.shopId = mProductInfo.shopId;
                bundle.putSerializable("mShopInfo",shopInfo);
                PageSwitcher.switchToTopNavPageNoTitle(getActivity(),ShopFragment.class, bundle,"","");
                break;
            case R.id.go_shopping:
                getActivity().finish();
                EventOfChangeTab eventOfChangeTab = new EventOfChangeTab();
                eventOfChangeTab.mIndex = 2;
                EventBus.getDefault().post(eventOfChangeTab);
                break;
        }
    }

    private void collectGood() {
        HashMap<String, String> params = new HashMap<>();
        params.put("goodsId", mProductInfo.goodsId);
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
                                ToastUtil.shortToast(R.string.collect_good_success);
                            }
                        });
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(R.string.collect_good_fail);
                            }
                        });
                    }
                });
    }
    private void buyNow() {
        Bundle bundle = new Bundle();
//        bundle.putInt("amount",amount);
//        ArrayList<SelTagInfo> selTagInfoList = new ArrayList<>();
        Map<String, String> map =new HashMap<>();

        for (TagInfo tagInfo : mTagsList) {
            if(TextUtils.isEmpty(tagInfo.mSelTag)) {
                ToastUtil.shortToast(getActivity(),"请选择规格");
                return;
            }
//            SelTagInfo selTagInfo = new SelTagInfo();
//            selTagInfo.mSelTag = tagInfo.mSelTag;
//            selTagInfo.mTitle = tagInfo.mTitle;
//            selTagInfoList.add(selTagInfo);
            map.put(tagInfo.mTitle,tagInfo.mSelTag);
        }
//        bundle.putSerializable("product",mProductInfo);
//        bundle.putParcelableArrayList("tagList", selTagInfoList);
        GoodsInfo goodsInfo = new GoodsInfo();
        List<GoodsInfo> goodsInfoList = new ArrayList<>();
        goodsInfo.amount = amount;
        goodsInfo.prePrice = mProductInfo.prePrice;
        goodsInfo.curPrice = mProductInfo.curPrice;
        goodsInfo.goodsName =mProductInfo.name;
        goodsInfo.goodsId = mProductInfo.goodsId;
        goodsInfo.goodsLogo = mProductInfo.logo;
        goodsInfo.goodsLogoThumb = mProductInfo.logoThumb;
        goodsInfo.tags = JSON.toJSONString(map);
        goodsInfoList.add(goodsInfo);
        ShoppingInfo shoppingInfo = new ShoppingInfo();
        shoppingInfo.shopId = mProductInfo.shopId;
        shoppingInfo.shopThumb = mProductInfo.shopThumb;
        shoppingInfo.shopImage = mProductInfo.shopLogo;
        shoppingInfo.shopName = mProductInfo.shopName;
        shoppingInfo.goodsList = goodsInfoList;
        ArrayList<ShoppingInfo> shoppingInfoArrayList = new ArrayList<>();
        shoppingInfoArrayList.add(shoppingInfo);
        bundle.putParcelableArrayList("shopInfo", shoppingInfoArrayList);
        bundle.putBoolean("isFromShopping", false);
        PageSwitcher.switchToTopNavPage(getActivity(), EditOrderFragment.class, bundle, getString(R.string.check_order), "");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateText(EventOfUpdateTags event) {
        StringBuilder stringBuilder = Util.getThreadSafeStringBuilder();
        stringBuilder.append("已选:");
        for (TagInfo tagInfo : mTagsList) {
            stringBuilder.append(tagInfo.mSelTag).append(" ");
        }
        tagsTv.setText(stringBuilder.toString());
    }
    private void addCart() {
        if(amount < 1 ) {
            ToastUtil.shortToast(getActivity(),"请选择数量");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("goodsId", mProductInfo.goodsId);
        params.put("amount", String.valueOf(amount));
        HashMap<String, String> tags = new HashMap<>();
        for (TagInfo tagInfo : mTagsList) {
            if(TextUtils.isEmpty(tagInfo.mSelTag)) {
                ToastUtil.shortToast(getActivity(),"请选择规格");
                return;
            }
            tags.put(tagInfo.mTitle, tagInfo.mSelTag);
        }
        String tagJsons = JSON.toJSONString(tags);
        L.d("@@@@@" + tagJsons);
        params.put("tags", tagJsons);
        OkHttpUtils.post(ApiConstants.ADD_CART)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new JsonCallback<String>(String.class) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(R.string.add_cart_success);
                                EventBus.getDefault().post(new EventOfAddCartSuccess());
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
                    .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                    .into(viewHolder.popLogoIv);
            viewHolder.addCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCart();
                    dismissConstructNoticeWindow();
                }
            });
            viewHolder.tagList.setAdapter(new UnScrollListAdapter(getActivity(),mTagsList));
//            contentView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismissConstructNoticeWindow();
//                }
//            });
            Rect r = new Rect();
            mRootView.getWindowVisibleDisplayFrame(r);
            final int[] location = new int[2];
            mRootView.getLocationOnScreen(location);
//            int height = Util.dip2px(60);
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, r.bottom
                    - location[1], false);
            mPickUpPopWindow = popupWindow;
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
//            mPickUpPopWindow.setOutsideTouchable(false);
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
        @Bind(R.id.pop_add_cart_btn)
        Button addCartBtn;
        @Bind(R.id.pop_buy_now_btn)
        Button buyNowBtn;
        @Bind(R.id.amount_ll)
        LinearLayout amountLl;
        @Bind(R.id.pop_list)
        UnScrollableListView tagList;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            popOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            closePop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissConstructNoticeWindow();
                }
            });
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount++;
                    productAmount.setText(String.valueOf(amount));
                }
            });
            subBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(amount > 1) {
                        amount--;
                        productAmount.setText(String.valueOf(amount));
                    }

                }
            });
            buyNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyNow();
                    dismissConstructNoticeWindow();
                }
            });
        }
    }


}
