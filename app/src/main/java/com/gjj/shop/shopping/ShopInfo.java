package com.gjj.shop.shopping;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ShopInfo implements Parcelable {

    public String shopId;
    public String shopName;
    public String shopImage;
    public String shopThumb;
    public List<GoodsInfo> goodsList;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopImage);
        dest.writeString(this.shopThumb);
        dest.writeList(goodsList);
    }

    public ShopInfo() {
    }

    private ShopInfo(Parcel in) {
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.shopImage = in.readString();
        this.shopThumb = in.readString();
        this.goodsList = new ArrayList<>();
        in.readList(goodsList, getClass().getClassLoader());
    }

    public static final Creator<ShopInfo> CREATOR = new Creator<ShopInfo>() {
        public ShopInfo createFromParcel(Parcel source) {
            return new ShopInfo(source);
        }

        public ShopInfo[] newArray(int size) {
            return new ShopInfo[size];
        }
    };
}
