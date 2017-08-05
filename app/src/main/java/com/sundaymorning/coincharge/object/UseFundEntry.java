package com.sundaymorning.coincharge.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dynast on 2017. 7. 6..
 */

public class UseFundEntry implements Parcelable {

    private int _id;
    private String mDate;
    private String mDescription;
    private String mType;
    private String mCash;
    private String mState;

    @Override
    public int describeContents() {
        return 0;
    }

    private UseFundEntry(Parcel in) {
        readFromParcel(in);
    }

    public UseFundEntry(int id, String date, String description, String type, String cash, String state) {
        _id = id;
        mDate = date;
        mDescription = description;
        mType = type;
        mCash = cash;
        mState = state;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(mDate);
        dest.writeString(mDescription);
        dest.writeString(mType);
        dest.writeString(mCash);
        dest.writeString(mState);
    }

    private void readFromParcel(Parcel in) {
        mDate = in.readString();
        mDescription = in.readString();
        mType = in.readString();
        mCash = in.readString();
        mState = in.readString();
    }

    public static final Creator CREATOR = new Creator() {
        public UseFundEntry createFromParcel(Parcel in) {
            return new UseFundEntry(in);
        }

        public UseFundEntry[] newArray(int size) {
            return new UseFundEntry[size];
        }
    };

    public String getDate() {
        return mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getType() {
        return mType;
    }

    public String getCash() {
        return mCash;
    }

    public String getState() {
        return mState;
    }
}
