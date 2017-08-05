package com.sundaymorning.coincharge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.sundaymorning.coincharge.R;

/**
 * Created by Dynast on 2017. 5. 21..
 */

public class SettingAcitivity extends AppCompatActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        LinearLayout mPolicy1_layout = (LinearLayout) findViewById(R.id.policy1_layout);
        LinearLayout mPolicy2_layout = (LinearLayout) findViewById(R.id.policy2_layout);

        mPolicy1_layout.setOnClickListener(Policy1ClickListener);
        mPolicy2_layout.setOnClickListener(Policy2ClickListener);


        LinearLayout mLoginOut_Layout = (LinearLayout) findViewById(R.id.logout_layout);
        mLoginOut_Layout.setOnClickListener(LogoutClickListener);
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

        }
    };
}
