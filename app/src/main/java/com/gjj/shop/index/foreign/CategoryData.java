package com.gjj.shop.index.foreign;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chuck on 2016/9/5.
 */
public class CategoryData  implements Parcelable {
    public int id;
    public int pid;
    public String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.pid);
        dest.writeString(this.name);
    }

    public CategoryData() {
    }

    private CategoryData(Parcel in) {
        this.id = in.readInt();
        this.pid = in.readInt();
        this.name = in.readString();
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
