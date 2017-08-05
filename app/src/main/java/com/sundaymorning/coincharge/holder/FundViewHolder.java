package com.sundaymorning.coincharge.holder;

import android.view.View;
import android.widget.TextView;

import com.sundaymorning.coincharge.R;

/**
 * Created by Dynast on 2017. 7. 6..
 */

public class FundViewHolder {

    private View mView;
    private TextView mDate;
    private TextView mDescription;
    private TextView mCash;
    private TextView mState;

    public FundViewHolder(View v, int resource) {
        mView = v;

        if (mDate == null)
            mDate = (TextView) mView.findViewById(R.id.date);

        if (mDescription == null)
            mDescription = (TextView) mView.findViewById(R.id.description);

        if (mCash == null)
            mCash = (TextView) mView.findViewById(R.id.cash);

        if (resource == R.layout.holder_use_fund) {
            if (mState == null)
                mState = (TextView) mView.findViewById(R.id.state);
        }
    }

    public TextView getDate() {
        if (mDate == null)
            mDate = (TextView) mView.findViewById(R.id.date);

        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate.setText(mDate);
    }

    public TextView getDescription() {
        if (mDescription == null)
            mDescription = (TextView) mView.findViewById(R.id.description);

        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription.setText(mDescription);
    }

    public TextView getCash() {
        if (mCash == null)
            mCash = (TextView) mView.findViewById(R.id.cash);

        return mCash;
    }

    public void setCash(String mCash) {
        this.mCash.setText(mCash);
    }


    public TextView getState() {
        if (mState == null)
            mState = (TextView) mView.findViewById(R.id.state);

        return mState;
    }

    public void setState(String mState) {
        this.mState.setText(mState);
    }
}
