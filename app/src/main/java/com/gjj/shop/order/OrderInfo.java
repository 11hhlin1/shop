package com.gjj.shop.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.Glide;
import com.gjj.shop.address.AddressInfo;
import com.gjj.shop.index.CommentInfo;
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
    public String shopId;
    public String shopLogoThumb;
    public AddressInfo address;
    public String orderId;
    public String shopLogo;
    public String payWay;
    public String contactPhone;
    public long receiveTime;
    public long deliverTime;
    public long payTime;
    public AfterSaleService afterSaleService;
    public List<GoodsInfo> goodsList;
    public List<CommentInfo> comment;

    public OrderInfo() {
    }

    protected OrderInfo(Parcel in) {
        createTime = in.readLong();
        receiveTime = in.readLong();
        deliverTime = in.readLong();
        payTime = in.readLong();
        status = in.readInt();
        shopName = in.readString();
        shopId = in.readString();
        shopLogoThumb = in.readString();
        orderId = in.readString();
        shopLogo = in.readString();
        payWay = in.readString();
        contactPhone = in.readString();
        address = (AddressInfo) in.readSerializable();
        afterSaleService = (AfterSaleService) in.readSerializable();
        goodsList = in.createTypedArrayList(GoodsInfo.CREATOR);
        comment = in.readArrayList(CommentInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createTime);
        dest.writeLong(receiveTime);
        dest.writeLong(deliverTime);
        dest.writeLong(payTime);
        dest.writeInt(status);
        dest.writeString(shopName);
        dest.writeString(shopId);
        dest.writeString(shopLogoThumb);
        dest.writeString(orderId);
        dest.writeString(shopLogo);
        dest.writeString(payWay);
        dest.writeString(contactPhone);
        dest.writeSerializable(address);
        dest.writeSerializable(afterSaleService);
        dest.writeTypedList(goodsList);
        dest.writeList(comment);
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
