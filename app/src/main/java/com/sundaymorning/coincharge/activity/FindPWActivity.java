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
import android.widget.EditText;

import com.namember.NAMember;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.utils.Utils;

/**
 * Created by sweet on 2017-07-09.
 */

public class FindPWActivity extends AppCompatActivity {

    private Context mContext = this;
    private EditText mEmail;
    private EditText mPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        initLayout();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MemberActivity.class));
        super.onBackPressed();
    }

    private void initLayout() {
        mEmail = (EditText) findViewById(R.id.edittext_email);
        mPhoneNumber = (EditText) findViewById(R.id.edittext_phone);
        Button mFindPWBtn = (Button) findViewById(R.id.btn_find_pw);

        mFindPWBtn.setOnClickListener(FindPWClickListener);
    }

    private View.OnClickListener FindPWClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            findPW();
        }
    };

    private void findPW() {
        String phoneNumber = mPhoneNumber.getText().toString();

        if (phoneNumber.startsWith("+82"))
            phoneNumber = phoneNumber.replace("+82", "0");

        String email = mEmail.getText().toString();

        if (!email.isEmpty() && Utils.checkPatternEmail(email) && !phoneNumber.isEmpty() && Utils.checkPatternPhoneNumber(phoneNumber)) {
            NAMember.MemberPwdFind(mContext, email, phoneNumber, mOnPasswordFindListener);
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

    private Common.OnPasswordFindListener mOnPasswordFindListener = new Common.OnPasswordFindListener() {
        @Override
        public void OnSuccess() {

            Utils.showDialog(mContext,
                    null,
                    getResources().getString(R.string.check_your_email),
                    getResources().getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) mContext).onBackPressed();
                        }
                    }, null);
        }

        @Override
        public void OnError(int i) {
            error(i);
        }

        @Override
        public void OnJson(String s) {

        }

        @Override
        public void OnErrorEmailPattern() {
            error(-1);
        }
    };

    private void error(int errorCode) {

        if (errorCode == -9999) {
            Utils.showDialog(mContext,
                    null,
                    getResources().getString(R.string.network_error_message),
                    getResources().getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, null);
        } else {
            Utils.showDialog(mContext,
                    null,
                    "[" + errorCode + "] " + getResources().getString(R.string.fail_no_data),
                    getResources().getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, null);
        }
    }
}
