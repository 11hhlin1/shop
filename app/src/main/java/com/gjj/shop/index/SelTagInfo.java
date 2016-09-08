package com.gjj.shop.index;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by user on 16/9/8.
 */
public class SelTagInfo  implements Parcelable {


    public String mTitle;
    public String mSelTag;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mSelTag);
    }

    public SelTagInfo() {
    }

    private SelTagInfo(Parcel in) {
        this.mTitle = in.readString();
        this.mSelTag = in.readString();
    }

    public static final Creator<SelTagInfo> CREATOR = new Creator<SelTagInfo>() {
        public SelTagInfo createFromParcel(Parcel source) {
            return new SelTagInfo(source);
        }

        public SelTagInfo[] newArray(int size) {
            return new SelTagInfo[size];
        }
    };
}