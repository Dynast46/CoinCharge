package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namember.NAMember;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.BuildConfig;
import com.sundaymorning.coincharge.InitActivity;
import com.sundaymorning.coincharge.MainActivity;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.activity.ChangePWActivity;
import com.sundaymorning.coincharge.activity.ExpireActivity;
import com.sundaymorning.coincharge.activity.PolicyActivity;
import com.sundaymorning.coincharge.activity.ReceiveFundActivity;
import com.sundaymorning.coincharge.activity.RecommActivity;
import com.sundaymorning.coincharge.activity.UseFundActivity;
import com.sundaymorning.coincharge.data.MemberInfoData;
import com.sundaymorning.coincharge.data.MemberInitData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.utils.Utils;

/**
 * Created by Dynast on 2017. 5. 21..
 */

@SuppressLint("ValidFragment")
public class MyPageFragment extends android.support.v4.app.Fragment {

    private Context mContext;

    private LinearLayout mReceive_fund_Layout;
    private LinearLayout mUse_fund_layout;
    private LinearLayout mReceive_recomm_layout;

    //    private TextView mFriend_text;

    private LinearLayout mPolicy1_layout;
    private LinearLayout mPolicy2_layout;

    private LinearLayout mChange_PW_layout;
    private LinearLayout mLoginOut_Layout;
    private LinearLayout mExpire_Layout;

    public MyPageFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypage, container, false);

        getActivity().setTitle(R.string.my_page);

        MemberInitData initData = SharedPreferenceUtils.loadMemberInitData(mContext);

        mReceive_fund_Layout = (LinearLayout) v.findViewById(R.id.receive_fund_layout);
        mReceive_recomm_layout = (LinearLayout) v.findViewById(R.id.receive_recomm_layout);
        mUse_fund_layout = (LinearLayout) v.findViewById(R.id.use_fund_layout);

        TextView mCurrentCoin = (TextView) v.findViewById(R.id.text_current_coin);
//        mFriend_text = (TextView) v.findViewById(R.id.text_friend);

        MemberInfoData memberInfoData = SharedPreferenceUtils.loadMemberInfoData(mContext);

        mCurrentCoin.setText(String.format(getResources().getString(R.string.current_coin), memberInfoData.getMoney()));
//        mFriend_text.setText(memberInfoData.getMyRecommendNickname());

        TextView mCurrentVersion = (TextView) v.findViewById(R.id.text_current_version);
        TextView mNewVersion = (TextView) v.findViewById(R.id.text_new_version);


        mCurrentVersion.setText(BuildConfig.VERSION_NAME);
        mNewVersion.setText(initData.getVersion());


        mPolicy1_layout = (LinearLayout) v.findViewById(R.id.policy1_layout);
        mPolicy2_layout = (LinearLayout) v.findViewById(R.id.policy2_layout);

        mChange_PW_layout = (LinearLayout) v.findViewById(R.id.change_pw_layout);
        mLoginOut_Layout = (LinearLayout) v.findViewById(R.id.logout_layout);
        mExpire_Layout = (LinearLayout) v.findViewById(R.id.expire_layout);

        initClickListener();
        return v;
    }

    private void initClickListener() {
        mReceive_fund_Layout.setOnClickListener(mReceive_fund_ClickListener);
        mReceive_recomm_layout.setOnClickListener(mReceive_recomm_ClickListener);
        mUse_fund_layout.setOnClickListener(mUse_fund_ClickListener);

        mPolicy1_layout.setOnClickListener(Policy1ClickListener);
        mPolicy2_layout.setOnClickListener(Policy2ClickListener);

        mChange_PW_layout.setOnClickListener(PWChangeClickListener);
        mLoginOut_Layout.setOnClickListener(LogoutClickListener);
        mExpire_Layout.setOnClickListener(ExpireClickListener);
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
            startActivity(new Intent(mContext, RecommActivity.class));
        }
    };

    private View.OnClickListener mUse_fund_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, UseFundActivity.class));
        }
    };

    private View.OnClickListener ExpireClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, ExpireActivity.class));
        }
    };


    private View.OnClickListener Policy1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, PolicyActivity.class);
            intent.putExtra("title", mContext.getString(R.string.policy));
            intent.putExtra("type", true);
            startActivity(intent);
        }
    };

    private View.OnClickListener Policy2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, PolicyActivity.class);
            intent.putExtra("title", mContext.getString(R.string.policy2));
            intent.putExtra("type", false);
            startActivity(intent);
        }
    };

    private View.OnClickListener LogoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NAMember.MemberLogout(mContext, memberLogoutListener);
        }
    };

    private View.OnClickListener PWChangeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ChangePWActivity.class);
            startActivity(intent);
        }
    };

    private Common.OnMemberLogoutListener memberLogoutListener = new Common.OnMemberLogoutListener() {
        @Override
        public void OnSuccess() {
            Utils.showProgressDialog(mContext);
            SharedPreferenceUtils.removeAllData(mContext);

            Utils.hideProgressDialog(mContext);
            Intent intent = new Intent(mContext, InitActivity.class);
            startActivity(intent);

            MainActivity activity = (MainActivity) mContext;
            activity.finish();
        }

        @Override
        public void OnError(int i) {
            Utils.showDialog(mContext,
                    getString(R.string.error),
                    getString(R.string.error_try_again) + "\nerrorCode : [ " + i + "]",
                    getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, null);
        }

        @Override
        public void OnJson(String s) {

        }
    };
}
