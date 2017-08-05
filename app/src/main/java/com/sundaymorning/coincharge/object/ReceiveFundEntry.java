package com.sundaymorning.coincharge.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dynast on 2017. 7. 6..
 */

public class ReceiveFundEntry implements Parcelable {

    private int _id;
    private String mDate;
    private String mDescription;
    private String mType;
    private String mCash;

    @Override
    public int describeContents() {
        return 0;
    }

    private ReceiveFundEntry(Parcel in) {
        readFromParcel(in);
    }

    public ReceiveFundEntry(int id, String date, String description, String type, String cash) {
        _id = id;
        mDate = date;
        mDescription = description;
        mType = type;
        mCash = cash;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(mDate);
        dest.writeString(mDescription);
        dest.writeString(mType);
        dest.writeString(mCash);
    }

    private void readFromParcel(Parcel in) {
        mDate = in.readString();
        mDescription = in.readString();
        mType = in.readString();
        mCash = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ReceiveFundEntry createFromParcel(Parcel in) {
            return new ReceiveFundEntry(in);
        }

        public ReceiveFundEntry[] newArray(int size) {
            return new ReceiveFundEntry[size];
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
}
