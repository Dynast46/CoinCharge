package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.namember.NAMember;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.InitActivity;
import com.sundaymorning.coincharge.MainActivity;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.activity.ChangePWActivity;
import com.sundaymorning.coincharge.activity.PolicyActivity;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.utils.Utils;

/**
 * Created by sweet on 2017-08-05.
 */

@SuppressLint("ValidFragment")
public class SettingFragment extends Fragment {

    private Context mContext;

    public SettingFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        getActivity().setTitle(R.string.setting);

        LinearLayout mPolicy1_layout = (LinearLayout) v.findViewById(R.id.policy1_layout);
        LinearLayout mPolicy2_layout = (LinearLayout) v.findViewById(R.id.policy2_layout);

        LinearLayout mChange_PW_layout = (LinearLayout) v.findViewById(R.id.change_pw_layout);
        LinearLayout mLoginOut_Layout = (LinearLayout) v.findViewById(R.id.logout_layout);

        mPolicy1_layout.setOnClickListener(Policy1ClickListener);
        mPolicy2_layout.setOnClickListener(Policy2ClickListener);

        mChange_PW_layout.setOnClickListener(PWChangeClickListener);
        mLoginOut_Layout.setOnClickListener(LogoutClickListener);
        return v;
    }

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
