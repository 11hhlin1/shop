package com.gjj.shop.address;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by user on 16/8/27.
 */
public class AddressInfo implements Serializable {
    private static final long serialVersionUID = -4264220104283022976L;
    public String contact;
    public String phone;
    public String area;
    public String address;
    public long addressId;
//    public boolean isDefault;

}
