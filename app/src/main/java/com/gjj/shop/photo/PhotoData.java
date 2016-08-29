package com.gjj.shop.photo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @Title:过家家-用户版
 * @Description: 图片信息
 * @Copyright: Copyright (c) 2015
 * @Company: 深圳市过家家
 * @version: 1.0.0.0
 * @author: jack
 * @createDate 2015-5-6
 * 
 */
public class PhotoData implements Parcelable {

    /**
     * 图片url
     */
    public String photoUrl;

    /**
     * 缩略图url
     */
    public String thumbUrl;


    public boolean showLoadErrorToast;

    public PhotoData() {
    }

    public PhotoData(Parcel source) {

        photoUrl = source.readString();
        thumbUrl = source.readString();
        showLoadErrorToast = source.readByte() != 0;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(photoUrl);
        dest.writeString(thumbUrl);
        dest.writeByte(showLoadErrorToast ? (byte) 1 : (byte) 0);
    }

    public static final Creator<PhotoData> CREATOR = new Creator<PhotoData>() {
        public PhotoData createFromParcel(Parcel source) {
            return new PhotoData(source);
        }

        public PhotoData[] newArray(int size) {
            return new PhotoData[size];
        }
    };

    @Override
    public String toString() {
        return "PhotoData{" +
                "photoUrl='" + photoUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", showLoadErrorToast=" + showLoadErrorToast +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof PhotoData) {
            PhotoData code = (PhotoData) o;
            return this.photoUrl.equals(code.photoUrl);
        }
        return super.equals(o);
    }
}
