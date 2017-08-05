package com.sundaymorning.coincharge.holder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sundaymorning.coincharge.R;

/**
 * Created by Dynast on 2017. 5. 7..
 */

public class NasViewHolder {

    private View mView;
    private ImageView mImage;
    private TextView mTitle;
    private TextView mSubTitle;
    private Button mCashBtn;

    public NasViewHolder(View v) {
        mView = v;

        if (mTitle == null)
            mTitle = (TextView) mView.findViewById(R.id.nas_title);

        if (mImage == null)
            mImage = (ImageView) mView.findViewById(R.id.icon_image);

        if (mSubTitle == null)
            mSubTitle = (TextView) mView.findViewById(R.id.nas_subtitle);

        if (mCashBtn == null)
            mCashBtn = (Button) mView.findViewById(R.id.nas_btn);
    }

    private ImageView getIconImage() {
        if (mImage == null)
            mImage = (ImageView) mView.findViewById(R.id.icon_image);

        return mImage;
    }

    public void setIconImage(Context context, String imageUrl) {
        Picasso.with(context).load(imageUrl).into(getIconImage());
    }

    public TextView getTitle() {
        if (mTitle == null)
            mTitle = (TextView) mView.findViewById(R.id.nas_title);

        return mTitle;
    }

    public void setTitle(String text) {
        mTitle.setText(text);
    }

    public TextView getSubTitle() {
        if (mSubTitle == null)
            mSubTitle = (TextView) mView.findViewById(R.id.nas_subtitle);

        return mSubTitle;
    }

    public void setSubTitle(String text) {
        mSubTitle.setText(text);
    }

    public Button getCashBtn() {
        if (mCashBtn == null)
            mCashBtn = (Button) mView.findViewById(R.id.nas_btn);

        return mCashBtn;
    }

    public void setCashBtn(String text) {
        mCashBtn.setText(text);
    }
}