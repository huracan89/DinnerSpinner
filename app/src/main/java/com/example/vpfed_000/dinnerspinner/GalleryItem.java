package com.example.vpfed_000.dinnerspinner;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

public class GalleryItem implements Parcelable{
    private String mCaption;
    private String mId;
    private String mUrl;
    private String mRating;
    private String mPrice;
    private String mAddress;

    public GalleryItem() {

    }

    protected GalleryItem(Parcel in) {
        mCaption = in.readString();
        mId = in.readString();
        mUrl = in.readString();
        mRating = in.readString();
        mPrice = in.readString();
        mAddress = in.readString();
    }

    public static final Creator<GalleryItem> CREATOR = new Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel in) {
            return new GalleryItem(in);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return mCaption;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCaption);
        dest.writeString(mId);
        dest.writeString(mUrl);
        dest.writeString(mRating);
        dest.writeString(mPrice);
        dest.writeString(mAddress);
    }

}
