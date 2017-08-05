package com.sundaymorning.coincharge.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.sundaymorning.coincharge.InitActivity;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.utils.Utils;

/**
 * Created by sweet on 2017-08-06.
 */

public class ExpireActivity extends AppCompatActivity {

    private Context mContext = this;
    private Button mReceive_Expire_Code;
    private Button mConfirm_Expire;

    private EditText mExpire_Code_EditText;
    private EditText mExpire_Dueto_EditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mExpire_Code_EditText = (EditText) findViewById(R.id.expire_code_edit);
        mExpire_Dueto_EditText = (EditText) findViewById(R.id.dueto_expire);

        mReceive_Expire_Code = (Button) findViewById(R.id.receive_expire_code);
        mConfirm_Expire = (Button) findViewById(R.id.expire_confirm);

        mReceive_Expire_Code.setOnClickListener(mExpireCodeClickListener);
        mConfirm_Expire.setOnClickListener(mExpireClickListener);
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

    private View.OnClickListener mExpireCodeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent i = getBaseContext().getPackageManager()
//                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(i);
//            finish();
            receiveUnSignCode();
        }
    };

    private View.OnClickListener mExpireClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mExpire_Code_EditText.getText().toString().equals("")) {
                Toast.makeText(mContext, getString(R.string.unsign_code_null), Toast.LENGTH_SHORT).show();
                return;
            }

            unSign();
        }
    };

    private void receiveUnSignCode() {
        if (Utils.checkNetworkAvailable(mContext)) {

            Utils.showProgressDialog(mContext);

            NAMember.MemberSecessionCertificationNumber(mContext, new Common.OnMemberSecessionCertificationNumberListenenr() {
                @Override
                public void OnSuccess(String s) {
                    Utils.hideProgressDialog(mContext);

                    Utils.showDialog(mContext,
                            null,
                            s,
                            mContext.getResources().getString(R.string.ok),
                            null,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }, null);
                }

                @Override
                public void OnError(int i) {
                    Utils.hideProgressDialog(mContext);

                    String message = "[" + i + "] " + mContext.getResources().getString(R.string.error_try_again);

                    Utils.showDialog(mContext,
                            null,
                            message,
                            mContext.getResources().getString(R.string.ok),
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
            });
        } else {
            Utils.showToast(mContext, mContext.getResources().getString(R.string.network_error_message));
        }
    }

    private void unSign() {
        if (Utils.checkNetworkAvailable(mContext)) {

            Utils.showProgressDialog(mContext);

            NAMember.MemberSecessionCertificationCompare(mContext,
                    mExpire_Code_EditText.getText().toString(),
                    "",
                    mExpire_Dueto_EditText.getText().toString(),
                    new Common.OnMemberSecessionCertificationCompareListenenr() {
                        @Override
                        public void OnSuccess() {

                            Utils.showDialog(mContext,
                                    null,
                                    mContext.getResources().getString(R.string.success_unsign),
                                    mContext.getResources().getString(R.string.ok),
                                    null,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferenceUtils.removeAllData(mContext);
//                                            Intent intent = new Intent();
//                                            intent.setAction(com.sundaymorning.emoticon.Common.FINISH_APP);
//                                            mContext.sendBroadcast(intent);

                                            Intent i = getBaseContext().getPackageManager().
                                                    getLaunchIntentForPackage(getBaseContext().getPackageName());
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                            finish();
                                        }
                                    }, null);
                        }

                        @Override
                        public void OnError(int i) {
                            Utils.hideProgressDialog(mContext);

                            String message = "[" + i + "] " + mContext.getResources().getString(R.string.error_try_again);
                            if (i == -101) {
                                message = mContext.getResources().getString(R.string.fail_code);
                            }

                            Utils.showDialog(mContext,
                                    null,
                                    message,
                                    mContext.getResources().getString(R.string.ok),
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
                    });

        } else {
            Utils.showToast(mContext, mContext.getResources().getString(R.string.network_error_message));
        }
    }
}
