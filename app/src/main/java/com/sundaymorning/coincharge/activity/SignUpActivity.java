package com.sundaymorning.coincharge.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.namember.NAMember;
import com.namember.data.MemberJoinData;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.InitActivity;
import com.sundaymorning.coincharge.NAGcm;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.utils.Utils;

/**
 * Created by sweet on 2017-07-09.
 */

public class SignUpActivity extends AppCompatActivity {

    private Context mContext = this;

    private EditText mEmailEditText;
    private EditText mPWEditText;
    private EditText mPWConfirmEditText;
    private EditText mNickNameEditText;
    private EditText mRecommedEditText;

    private CheckBox mPolicy1Box;
    private CheckBox mPolicy2Box;

    private String mPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_1);

        initLayout();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MemberActivity.class));
        super.onBackPressed();
    }

    private void initLayout() {
        mPhoneNumber = SharedPreferenceUtils.loadPhoneNumber(mContext);

        Button mPolicy1Btn = (Button) findViewById(R.id.btn_policy);
        Button mPolicy2Btn = (Button) findViewById(R.id.btn_policy2);

        mPolicy1Box = (CheckBox) findViewById(R.id.chkbox_policy1);
        mPolicy2Box = (CheckBox) findViewById(R.id.chkbox_policy2);

        mEmailEditText = (EditText) findViewById(R.id.edittext_email);
        mPWEditText = (EditText) findViewById(R.id.edittext_pw);
        mPWConfirmEditText = (EditText) findViewById(R.id.edittext_pw_confirm);
        mNickNameEditText = (EditText) findViewById(R.id.edittext_nickname);
        mRecommedEditText = (EditText) findViewById(R.id.edittext_recommend);

        mPolicy1Btn.setOnClickListener(mPolicy1ClickListener);
        mPolicy2Btn.setOnClickListener(mPolicy2ClickListener);

        Button JoinBtn = (Button) findViewById(R.id.btn_join);
        JoinBtn.setOnClickListener(mJoinClickListener);
    }

    private View.OnClickListener mPolicy1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, PolicyActivity.class);
            intent.putExtra("title", mContext.getString(R.string.policy));
            intent.putExtra("type", true);
            startActivity(intent);
        }
    };

    private View.OnClickListener mPolicy2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, PolicyActivity.class);
            intent.putExtra("title", mContext.getString(R.string.policy2));
            intent.putExtra("type", false);
            startActivity(intent);
        }
    };

    private View.OnClickListener mJoinClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkData()) {
                checkDuplicatedAccount();
            } else {
                Utils.showDialog(mContext,
                        null,
                        getResources().getString(R.string.input_data_message),
                        getResources().getString(R.string.ok),
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }, null);
            }
        }
    };

    private boolean checkData() {
        String account = mEmailEditText.getText().toString();
        String password = mPWEditText.getText().toString();
        String passwordRepeat = mPWConfirmEditText.getText().toString();
        String nickname = mNickNameEditText.getText().toString();
        boolean useAgreement = mPolicy1Box.isChecked();
        boolean personalAgreement = mPolicy2Box.isChecked();

        if (account.isEmpty() || !Utils.checkPatternEmail(account)) {
            Utils.showToast(mContext, getResources().getString(R.string.join_account_error));
            return false;
        } else if (!password.equals(passwordRepeat) || password.isEmpty() || (password.length() < 4 || password.length() > 12)) {
            Utils.showToast(mContext, getResources().getString(R.string.join_password_error));
            return false;
        } else if (nickname.isEmpty() || (nickname.length() < 2 || nickname.length() > 8)) {
            Utils.showToast(mContext, getResources().getString(R.string.join_nickname_error));
            return false;
        } else if (!personalAgreement || !useAgreement) {
            Utils.showToast(mContext, getResources().getString(R.string.join_agreement_check_error));
            return false;
        }

        return true;
    }

    private void checkDuplicatedAccount() {
        String account = mEmailEditText.getText().toString();
        NAMember.MemberOverlapCheckID(mContext, account, mOnMemberAccountOverlapCheckListener);
    }

    private void join() {

        String account = mEmailEditText.getText().toString();
        String password = mPWEditText.getText().toString();
        String nickname = mPWConfirmEditText.getText().toString();
        String recommend = mRecommedEditText.getText().toString();

        MemberJoinData mJoinData = new MemberJoinData();

        mJoinData.setID(account);
        mJoinData.setPassword(password);
        mJoinData.setNickname(nickname);

        mJoinData.setRecommenderMemberNickname(recommend);
        mJoinData.setPushID(NAGcm.getToken(mContext));

        NAMember.SMSAfterMemberInsert(mContext, mJoinData, mPhoneNumber, mOnMemberInsertListener);
    }

    private void error(int errorCode) {
        String message;

        if (errorCode == -9999) {
            message = getResources().getString(R.string.network_error_message);
        } else {
            message = "[" + errorCode + "] " + getResources().getString(R.string.re_try_message);
        }

        if (!message.isEmpty()) {
            Utils.showDialog(mContext,
                    null,
                    message,
                    getResources().getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, null);
        }
    }

    private void checkDuplicatedNickname() {
        String nickname = mNickNameEditText.getText().toString();
        NAMember.MemberOverlapCheckNickname(mContext, nickname, mOnMemberNicknameOverlapCheckListener);
    }

    private NAMember.OnMemberOverlapCheckListener mOnMemberAccountOverlapCheckListener = new Common.OnMemberOverlapCheckListener() {
        @Override
        public void OnSuccess(boolean b) {

            Utils.Log("ACCOUNT CHECK _ " + b);
            if (!b) {
                checkDuplicatedNickname();
            } else {
                Utils.showToast(mContext, getResources().getString(R.string.account_duplicated_error));
            }
        }

        @Override
        public void OnError(int i) {
            error(i);
        }

        @Override
        public void OnJson(String s) {

        }
    };

    private NAMember.OnMemberOverlapCheckListener mOnMemberNicknameOverlapCheckListener = new Common.OnMemberOverlapCheckListener() {
        @Override
        public void OnSuccess(boolean b) {
            Utils.Log("NICKNAME CHECK _ " + b);

            if (!b) {
                join();
            } else {
                Utils.showToast(mContext, getResources().getString(R.string.nickname_duplicated_error));
            }
        }

        @Override
        public void OnError(int i) {
            error(i);
        }

        @Override
        public void OnJson(String s) {

        }
    };

    private NAMember.OnMemberInsertListener mOnMemberInsertListener = new Common.OnMemberInsertListener() {
        @Override
        public void OnSuccess(String s, String s1) {
            Intent intent = new Intent(mContext, InitActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void OnError(int i) {
            error(i);
        }

        @Override
        public void OnJson(String s) {

        }
    };
}
