package com.gjj.shop.shopping;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by user on 16/9/4.
 */
public class GoodsInfo implements Parcelable {
    public String goodsId;
    public String goodsName;
    public String goodsLogo;
    public String goodsLogoThumb;
    public String tags;
    public int amount;
    public double curPrice;
    public double prePrice;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goodsId);
        dest.writeString(this.goodsName);
        dest.writeString(this.goodsLogo);
        dest.writeString(this.goodsLogoThumb);
        dest.writeString(this.tags);
        dest.writeInt(amount);
        dest.writeDouble(curPrice);
        dest.writeDouble(prePrice);
    }

    public GoodsInfo() {
    }

    private GoodsInfo(Parcel in) {
        this.goodsId = in.readString();
        this.goodsName = in.readString();
        this.goodsLogo = in.readString();
        this.goodsLogoThumb = in.readString();
        this.tags = in.readString();
        this.amount =in.readInt();
        this.curPrice = in.readDouble();
        this.prePrice = in.readDouble();
    }

    public static final Creator<GoodsInfo> CREATOR = new Creator<GoodsInfo>() {
        public GoodsInfo createFromParcel(Parcel source) {
            return new GoodsInfo(source);
        }

        public GoodsInfo[] newArray(int size) {
            return new GoodsInfo[size];
        }
    };

}
