package com.sundaymorning.coincharge.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dynast on 2017. 5. 7..
 */

public class NasEntry implements Parcelable {

    private int _id;
    private String mTitle;
    private String mSubtitle;
    private String mImageURL;
    private String mBtnText;

    @Override
    public int describeContents() {
        return 0;
    }

    public NasEntry() {
    }

    private NasEntry(Parcel in) {
        readFromParcel(in);
    }

    public NasEntry(int id, String title, String subtitle, String imageurl, String btnText) {
        _id = id;
        mTitle = title;
        mSubtitle = subtitle;
        mImageURL = imageurl;
        mBtnText = btnText;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(mTitle);
        dest.writeString(mSubtitle);
        dest.writeString(mImageURL);
        dest.writeString(mBtnText);
    }

    private void readFromParcel(Parcel in) {
        mTitle = in.readString();
        mSubtitle = in.readString();
        mImageURL = in.readString();
        mBtnText = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NasEntry createFromParcel(Parcel in) {
            return new NasEntry(in);
        }

        public NasEntry[] newArray(int size) {
            return new NasEntry[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public String getBtnText() {
        return mBtnText;
    }
}
