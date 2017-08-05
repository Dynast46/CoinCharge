package com.sundaymorning.coincharge.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.namember.NAMember;
import com.namember.data.MemberData;
import com.namember.data.MemberLoginData;
import com.sundaymorning.coincharge.MainActivity;
import com.sundaymorning.coincharge.NAGcm;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.data.LoginData;
import com.sundaymorning.coincharge.data.MemberInfoData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.utils.Utils;

/**
 * Created by sweet on 2017-07-08.
 */

public class MemberActivity extends AppCompatActivity {

    Context mContext = this;
    private EditText mIDEditText;
    private EditText mPWEditText;

//    private Button mQIDBtn;
//    private Button mQPWBtn;
//
//    private Button mSignInBtn; // 로그인
//    private Button mSignUpBtn; // 회원가입

    private static final String TAG = "MemberActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLayout();
    }

    private void initLayout() {
        mIDEditText = (EditText) findViewById(R.id.id_edittext);
        mPWEditText = (EditText) findViewById(R.id.pw_edittext);

        Button mQIDBtn = (Button) findViewById(R.id.qid_btn);
        Button mQPWBtn = (Button) findViewById(R.id.qpw_btn);

        Button mSignInBtn = (Button) findViewById(R.id.login_btn);
        Button mSignUpBtn = (Button) findViewById(R.id.join_btn);

        mQIDBtn.setOnClickListener(QIDBtnClickListener);
        mQPWBtn.setOnClickListener(QPWBtnClickListener);
        mSignInBtn.setOnClickListener(SignInClickListener);
        mSignUpBtn.setOnClickListener(SignUpClickListener);
    }

    private void moveMain() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    private View.OnClickListener QIDBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, FindAccountActivity.class));
            finish();
        }
    };

    private View.OnClickListener QPWBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, FindPWActivity.class));
            finish();
        }
    };

    private View.OnClickListener SignInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d(TAG, mIDEditText.getText().toString());
            Log.d(TAG, mPWEditText.getText().toString());

            String id = mIDEditText.getText().toString();
            String password = mPWEditText.getText().toString();
            String pushID = NAGcm.getToken(mContext);
            String googleID = "";

            if (id.isEmpty() || password.isEmpty() || !Utils.checkPatternEmail(id)) {


                Utils.showDialog(mContext,
                        null,
                        getResources().getString(R.string.input_account_data_message),
                        mContext.getResources().getString(R.string.ok),
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, null);
            } else {
                NAMember.MemberLogin(mContext, id, password, pushID, googleID, mOnMemberLoginListener);
            }
        }
    };

    private View.OnClickListener SignUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            startActivity(new Intent(mContext, SignUp0Activity.class));
            startActivity(new Intent(mContext, SignUpActivity.class));
            finish();
        }
    };


    private com.namember.utils.Common.OnMemberLoginListener mOnMemberLoginListener = new com.namember.utils.Common.OnMemberLoginListener() {
        @Override
        public void OnSuccess(MemberLoginData memberLoginData) {
            LoginData mLoginData = new LoginData();

            mLoginData.setMID(memberLoginData.getMemberID());
            mLoginData.setLKEY(memberLoginData.getLoginKey());
            mLoginData.setMDID(memberLoginData.getMemberDeviceID());

            SharedPreferenceUtils.saveLogin(mContext, mLoginData);

            NAMember.MemberView(mContext, mOnMemberViewListener);
        }

        @Override
        public void OnError(int i) {
            error(i);
        }

        @Override
        public void OnJson(String s) {

        }
    };

    private com.namember.utils.Common.OnMemberViewListener mOnMemberViewListener = new com.namember.utils.Common.OnMemberViewListener() {
        @Override
        public void OnSuccess(MemberData memberData) {

            if (memberData != null) {

                MemberInfoData memberInfoData = new MemberInfoData();
                memberInfoData.setNickName(memberData.getMemberNickname());
                memberInfoData.setMoney(memberData.getMoney());
                memberInfoData.setMyRecommendNickname(memberData.getRecommenderMemberNickName());
                memberInfoData.setEmail(memberData.getMemberEmail());
                memberInfoData.setAge(memberData.getAge());
                memberInfoData.setSex(memberData.getSex());

                SharedPreferenceUtils.saveMemberInfoData(mContext, memberInfoData);

                moveMain();
            }
        }

        @Override
        public void onError(int i) {
            error(i);
        }

        @Override
        public void onJson(String s) {

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
        } else if( errorCode == -100) {
            Utils.showDialog(mContext,
                    null,
                    "[" + errorCode + "] " + getResources().getString(R.string.input_account_data_message),
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
                    "[" + errorCode + "] " + getResources().getString(R.string.re_try_message),
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
