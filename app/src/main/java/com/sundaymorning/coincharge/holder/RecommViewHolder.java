package com.sundaymorning.coincharge.holder;

import android.view.View;
import android.widget.TextView;

import com.sundaymorning.coincharge.R;

/**
 * Created by Dynast on 2017. 7. 6..
 */

public class RecommViewHolder {

    private View mView;
    private TextView mDate;
    private TextView mDescription;
    private TextView mMission;
    private TextView mFriend;

    public RecommViewHolder(View v) {
        mView = v;

        if (mDate == null)
            mDate = (TextView) mView.findViewById(R.id.date);

        if (mDescription == null)
            mDescription = (TextView) mView.findViewById(R.id.description);

        if (mMission == null)
            mMission = (TextView) mView.findViewById(R.id.mission);

        if (mFriend == null)
            mFriend = (TextView) mView.findViewById(R.id.friend);
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

    public TextView getMission() {
        if (mMission == null)
            mMission = (TextView) mView.findViewById(R.id.mission);

        return mMission;
    }

    public void setMission(String mission) {
        this.mMission.setText(mission);
    }


    public TextView getFriend() {
        if (mFriend == null)
            mFriend = (TextView) mView.findViewById(R.id.friend);

        return mFriend;
    }

    public void setFriend(String mFriend) {
        this.mFriend.setText(mFriend);
    }
}
