package com.sundaymorning.coincharge.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sweet on 2017-09-23.
 */

public class StoreCardEntry implements Parcelable {

    public int _CARD_TYPE_ID;
    public int _MARKET_MONEY;
    public String _NAME;
    public int _USE_POSSIBLE_CARD_COUNT;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StoreCardEntry createFromParcel(Parcel in) {
            return new StoreCardEntry(in);
        }

        public StoreCardEntry[] newArray(int size) {
            return new StoreCardEntry[size];
        }
    };
    private StoreCardEntry(Parcel in) {
        readFromParcel(in);
    }

    public StoreCardEntry(int CARD_TYPE_ID, int MARKET_MONEY, String NAME, int USE_POSSIBLE_CARD_COUNT) {
        _CARD_TYPE_ID = CARD_TYPE_ID;
        _MARKET_MONEY = MARKET_MONEY;
        _NAME = NAME;
        _USE_POSSIBLE_CARD_COUNT = USE_POSSIBLE_CARD_COUNT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_CARD_TYPE_ID);
        dest.writeInt(_MARKET_MONEY);
        dest.writeString(_NAME);
        dest.writeInt(_USE_POSSIBLE_CARD_COUNT);
    }

    private void readFromParcel(Parcel in) {
        _CARD_TYPE_ID = in.readInt();
        _MARKET_MONEY = in.readInt();
        _NAME = in.readString();
        _USE_POSSIBLE_CARD_COUNT = in.readInt();
    }

//    public int getCardTypeID() {
//        return this._CARD_TYPE_ID;
//    }
//
//    public String getTitle() {
//        return this._productTitle;
//    }
//
//    public int getCash() {
//        return this._productCash;
//    }
}
