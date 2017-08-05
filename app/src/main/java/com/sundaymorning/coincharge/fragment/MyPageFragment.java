package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.activity.ReceiveFundActivity;
import com.sundaymorning.coincharge.activity.UseFundActivity;
import com.sundaymorning.coincharge.data.MemberInfoData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;

/**
 * Created by Dynast on 2017. 5. 21..
 */

@SuppressLint("ValidFragment")
public class MyPageFragment extends Fragment {

    private Context mContext;

    private LinearLayout mReceive_fund_Layout;
    private LinearLayout mUse_fund_layout;
    private LinearLayout mReceive_recomm_layout;

    private TextView mCurrentCoin;
    private TextView mFriend_text;

    public MyPageFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypage, container, false);

        getActivity().setTitle(R.string.my_page);
        mReceive_fund_Layout = (LinearLayout) v.findViewById(R.id.receive_fund_layout);
        mReceive_recomm_layout = (LinearLayout) v.findViewById(R.id.receive_recomm_layout);
        mUse_fund_layout = (LinearLayout) v.findViewById(R.id.use_fund_layout);

        mCurrentCoin = (TextView) v.findViewById(R.id.text_current_coin);
        mFriend_text = (TextView) v.findViewById(R.id.text_friend);

        MemberInfoData memberInfoData = SharedPreferenceUtils.loadMemberInfoData(mContext);

        mCurrentCoin.setText(String.format(getResources().getString(R.string.current_coin), memberInfoData.getMoney()));
        mFriend_text.setText(memberInfoData.getMyRecommendNickname());

        initClickListener();
        return v;
    }

    private void initClickListener() {
        mReceive_fund_Layout.setOnClickListener(mReceive_fund_ClickListener);
        mReceive_recomm_layout.setOnClickListener(mReceive_recomm_ClickListener);
        mUse_fund_layout.setOnClickListener(mUse_fund_ClickListener);
    }

    private View.OnClickListener mReceive_fund_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, ReceiveFundActivity.class));
        }
    };

    private View.OnClickListener mReceive_recomm_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener mUse_fund_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, UseFundActivity.class));
        }
    };
}
