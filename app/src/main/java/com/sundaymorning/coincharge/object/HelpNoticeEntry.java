package com.sundaymorning.coincharge.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sweet on 2017-07-08.
 */

public class HelpNoticeEntry implements Parcelable {

    private int _id;
    private String mTitle = "";
    private String mContents = "";
    private String mInsertDate = "";

    @Override
    public int describeContents() {
        return 0;
    }

    private HelpNoticeEntry(Parcel in) {
        readFromParcel(in);
    }

    public HelpNoticeEntry(int id, String title, String contents, String insertDate) {
        _id = id;
        mTitle = title;
        mContents = contents;
        mInsertDate = insertDate;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(mTitle);
        dest.writeString(mContents);
        dest.writeString(mInsertDate);
    }

    private void readFromParcel(Parcel in) {
        mTitle = in.readString();
        mContents = in.readString();
        mInsertDate = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public HelpNoticeEntry createFromParcel(Parcel in) {
            return new HelpNoticeEntry(in);
        }

        public HelpNoticeEntry[] newArray(int size) {
            return new HelpNoticeEntry[size];
        }
    };

    public int getNoticeID() {
        return this._id;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getContents() {
        return this.mContents;
    }

    public String getInsertDate() {
        return this.mInsertDate;
    }
}
