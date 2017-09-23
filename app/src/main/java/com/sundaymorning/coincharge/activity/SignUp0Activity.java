package com.sundaymorning.coincharge.activity;

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
import com.namember.data.MemberLoginData;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.utils.Utils;


/**
 * Created by sweet on 2017-07-09.
 */

public class SignUp0Activity extends AppCompatActivity {

    private Context mContext = this;
    private EditText mPhoneEdittext;
    private EditText mCodeEditText;
    private Common.OnMobileCertificationListener mOnMobileCertificationListener = new Common.OnMobileCertificationListener() {
        @Override
        public void OnSuccess() {
            Utils.showDialog(mContext,
                    null,
                    getResources().getString(R.string.success_send_confirm_code),
                    getResources().getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, null);
        }

        @Override
        public void OnError(int i) {
            if (i == -103) {
                Utils.showDialog(mContext,
                        null,
                        getResources().getString(R.string.sms_cert_error_103),
                        getResources().getString(R.string.ok),
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }, null);
            }
        }

        @Override
        public void OnJson(String s) {

        }
    };
    private View.OnClickListener mRequestCodeCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phoneNumber = mPhoneEdittext.getText().toString();

            if (phoneNumber.startsWith("+82"))
                phoneNumber = phoneNumber.replace("+82", "0");

            if (!phoneNumber.isEmpty() && Utils.checkPatternPhoneNumber(phoneNumber)) {
                NAMember.MobileCertificationInsert(mContext, phoneNumber, mOnMobileCertificationListener);
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
    private Common.OnMobileCompareListener mOnMobileCompareListener = new Common.OnMobileCompareListener() {
        @Override
        public void OnSuccess(String s, int i) {
            SharedPreferenceUtils.savePhoneNumber(mContext, s);
            Intent intent = new Intent(mContext, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void OnSuccess(MemberLoginData memberLoginData) {

        }

        @Override
        public void OnError(int i) {

        }

        @Override
        public void OnJson(String s) {

        }
    };
    private View.OnClickListener mCodeConfirmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phoneNumber = mPhoneEdittext.getText().toString();
            String checkString = mCodeEditText.getText().toString();

            if (phoneNumber.startsWith("+82"))
                phoneNumber = phoneNumber.replace("+82", "0");

            if (!phoneNumber.isEmpty() && !checkString.isEmpty() && Utils.checkPatternPhoneNumber(phoneNumber)) {
                NAMember.BeforeMobileCertificationCompare(mContext, checkString, phoneNumber, mOnMobileCompareListener);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_0);

        initLayout();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MemberActivity.class));
        super.onBackPressed();
    }

    private void initLayout() {
        mPhoneEdittext = (EditText) findViewById(R.id.edittext_phone);
        mCodeEditText = (EditText) findViewById(R.id.edittext_code);

        Button mRequestCodeBtn = (Button) findViewById(R.id.btn_request_code);
        Button mCodeConfirmBtn = (Button) findViewById(R.id.btn_request_code_confirm);

        mRequestCodeBtn.setOnClickListener(mRequestCodeCLickListener);
        mCodeConfirmBtn.setOnClickListener(mCodeConfirmClickListener);
    }
}
