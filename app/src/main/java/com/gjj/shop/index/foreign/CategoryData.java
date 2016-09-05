package com.gjj.shop.index.foreign;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Chuck on 2016/9/5.
 */
public class CategoryData  implements Parcelable {
    public int mCateId;
    public String mCateName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCateId);
        dest.writeString(this.mCateName);
    }

    public CategoryData() {
    }

    private CategoryData(Parcel in) {
        this.mCateId = in.readInt();
        this.mCateName = in.readString();
    }

    public static final Parcelable.Creator<CategoryData> CREATOR = new Parcelable.Creator<CategoryData>() {
        public CategoryData createFromParcel(Parcel source) {
            return new CategoryData(source);
        }

        public CategoryData[] newArray(int size) {
            return new CategoryData[size];
        }
    };
}
