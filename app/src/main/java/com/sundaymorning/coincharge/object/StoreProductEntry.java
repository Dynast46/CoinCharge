package com.sundaymorning.coincharge.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dynast on 2017. 8. 20..
 */

public class StoreProductEntry implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StoreProductEntry createFromParcel(Parcel in) {
            return new StoreProductEntry(in);
        }

        public StoreProductEntry[] newArray(int size) {
            return new StoreProductEntry[size];
        }
    };
    private int _id;
    private String _productTitle = "";
    private int _productCash;

    private StoreProductEntry(Parcel in) {
        readFromParcel(in);
    }

    public StoreProductEntry(int id, String title, int cash) {
        _id = id;
        _productTitle = title;
        _productCash = cash;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_productTitle);
        dest.writeInt(_productCash);
    }

    private void readFromParcel(Parcel in) {
        _id = in.readInt();
        _productTitle = in.readString();
        _productCash = in.readInt();
    }

    public int getProductID() {
        return this._id;
    }

    public String getTitle() {
        return this._productTitle;
    }

    public int getCash() {
        return this._productCash;
    }
}
