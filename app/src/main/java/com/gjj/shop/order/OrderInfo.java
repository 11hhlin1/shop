package com.gjj.shop.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.Glide;
import com.gjj.shop.address.AddressInfo;
import com.gjj.shop.shopping.GoodsInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/14.
 */
public class OrderInfo implements Parcelable {

    public long createTime;
    public int status;
    public String shopName;
    public long shopId;
    public String shopLogoThumb;
    public AddressInfo address;
    public long orderId;
    public String shopLogo;
    public List<GoodsInfo> goodsList;

    public OrderInfo() {
    }

    protected OrderInfo(Parcel in) {
        createTime = in.readLong();
        status = in.readInt();
        shopName = in.readString();
        shopId = in.readLong();
        shopLogoThumb = in.readString();
        orderId = in.readLong();
        shopLogo = in.readString();
        address = (AddressInfo) in.readSerializable();
        goodsList = in.createTypedArrayList(GoodsInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createTime);
        dest.writeInt(status);
        dest.writeString(shopName);
        dest.writeLong(shopId);
        dest.writeString(shopLogoThumb);
        dest.writeLong(orderId);
        dest.writeString(shopLogo);
        dest.writeSerializable(address);
        dest.writeTypedList(goodsList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel in) {
            return new OrderInfo(in);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };
}
