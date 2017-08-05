package com.sundaymorning.coincharge.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.namember.NAMember;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.utils.Utils;

import static com.sundaymorning.coincharge.R.string.network_error_message;

/**
 * Created by sweet on 2017-07-09.
 */

public class PolicyActivity extends AppCompatActivity {

    private Context mContext = this;
    private TextView mPolicyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String title = getIntent().getStringExtra("title");
        Boolean type = getIntent().getBooleanExtra("Type", false);
        getSupportActionBar().setTitle(title);

        mPolicyText = (TextView) findViewById(R.id.policy_text);
        getData(type);
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

    private void getData(boolean type){
        if(Utils.checkNetworkAvailable(mContext)) {

            Utils.showProgressDialog(mContext);
            NAMember.AgreementList(mContext, type, new Common.OnAgreementListener() {
                @Override
                public void OnAgreement(String s) {
                    Utils.hideProgressDialog(mContext);
                    mPolicyText.setText(s);
                }

                @Override
                public void OnError(int i) {
                    Utils.hideProgressDialog(mContext);
                    Utils.showToast(mContext, "[" + i + "] " + mContext.getResources().getString(R.string.error_try_again) );
                    finish();
                }

                @Override
                public void OnJson(String s) {

                }
            });
        } else {
            Utils.showDialog(mContext,
                    null,
                    mContext.getResources().getString(network_error_message),
                    mContext.getResources().getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }, null);
        }
    }
}
