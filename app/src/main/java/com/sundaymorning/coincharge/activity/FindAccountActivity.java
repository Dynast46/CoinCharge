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

public class FindAccountActivity extends AppCompatActivity {

    private Context mContext = this;
    private EditText mFindAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        initLayout();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MemberActivity.class));
        super.onBackPressed();
    }

    private void initLayout() {
        mFindAccount = (EditText) findViewById(R.id.edittext_find_account);
        Button mFindAccountBtn = (Button) findViewById(R.id.btn_find_account);

        mFindAccountBtn.setOnClickListener(FindAccountClickListener);
    }

    private View.OnClickListener FindAccountClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            findAccount();
        }
    };

    private void findAccount() {
        String phoneNumber = mFindAccount.getText().toString();

        if (phoneNumber.startsWith("+82"))
            phoneNumber = phoneNumber.replace("+82", "0");

        if (!phoneNumber.isEmpty() && Utils.checkPatternPhoneNumber(phoneNumber)) {
            NAMember.MemberEmailSearch(mContext, phoneNumber, mOnMemberEmailSearchListener);
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

    private Common.OnMemberEmailSearchListener mOnMemberEmailSearchListener = new Common.OnMemberEmailSearchListener() {
        @Override
        public void OnSuccess(String s) {
            Utils.showDialog(mContext,
                    null,
                    s,
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
