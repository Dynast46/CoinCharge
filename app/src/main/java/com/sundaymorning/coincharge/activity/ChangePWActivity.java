package com.sundaymorning.coincharge.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.namember.NAMember;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.utils.Utils;

/**
 * Created by sweet on 2017-07-09.
 */

public class ChangePWActivity extends AppCompatActivity {

    private Context mContext = this;

    private EditText mOldPW_EditText;
    private EditText mNewPW_EditText;
    private EditText mNewPW_EditText_Confirm;

    private View.OnClickListener mChangeConfirmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mOldPW_EditText.getText().toString().equals("")) {
                Toast.makeText(mContext, getString(R.string.old_pw_null), Toast.LENGTH_SHORT).show();
                return;
            }

            if (mNewPW_EditText.getText().toString().equals("")) {
                Toast.makeText(mContext, getString(R.string.new_pw_null), Toast.LENGTH_SHORT).show();
                return;
            }

            if (mNewPW_EditText_Confirm.getText().toString().equals("")) {
                Toast.makeText(mContext, getString(R.string.new_pw_confirm_null), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!mNewPW_EditText.getText().toString().equals(mNewPW_EditText_Confirm.getText().toString())) {
                Toast.makeText(mContext, getString(R.string.new_pw_confirm_error), Toast.LENGTH_SHORT).show();
                return;
            }

            mOldPW_EditText.clearFocus();
            mNewPW_EditText.clearFocus();
            mNewPW_EditText_Confirm.clearFocus();

            NAMember.MemberPasswordChange(mContext, mOldPW_EditText.getText().toString(), mNewPW_EditText.getText().toString(), mNewPW_EditText_Confirm.getText().toString(), MemberPwChangeListener);
        }
    };

    private Common.OnMemberPasswordChangeListener MemberPwChangeListener = new Common.OnMemberPasswordChangeListener() {
        @Override
        public void OnSuccess() {
            Utils.showDialog(mContext,
                    getString(R.string.change_password),
                    getString(R.string.success_password_change),
                    getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }, null);
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

        @Override
        public void OnNewPwdDisagree() {
            Toast.makeText(mContext, getString(R.string.new_pw_disagree), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnOverlapNewPwd() {
            Toast.makeText(mContext, getString(R.string.new_pw_overlap), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mOldPW_EditText = (EditText) findViewById(R.id.old_pw_edittext);
        mNewPW_EditText = (EditText) findViewById(R.id.new_pw_edittext);
        mNewPW_EditText_Confirm = (EditText) findViewById(R.id.new_pw_edittext_confirm);

        Button mChange_Confirm = (Button) findViewById(R.id.change_confirm);
        mChange_Confirm.setOnClickListener(mChangeConfirmClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void getData(boolean type){
//        if(Utils.checkNetworkAvailable(mContext)) {
//
//            Utils.showProgressDialog(mContext);
//            NAMember.AgreementList(mContext, type, new Common.OnAgreementListener() {
//                @Override
//                public void OnAgreement(String s) {
//                    Utils.hideProgressDialog(mContext);
//                    mPolicyText.setText(s);
//                }
//
//                @Override
//                public void OnError(int i) {
//                    Utils.hideProgressDialog(mContext);
//                    Utils.showToast(mContext, "[" + i + "] " + mContext.getResources().getString(R.string.error_try_again) );
//                    finish();
//                }
//
//                @Override
//                public void OnJson(String s) {
//
//                }
//            });
//        } else {
//            Utils.showDialog(mContext,
//                    null,
//                    mContext.getResources().getString(network_error_message),
//                    mContext.getResources().getString(R.string.ok),
//                    null,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    }, null);
//        }
//    }
}
