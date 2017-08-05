package com.sundaymorning.coincharge.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.adapter.ReceiveFundAdapter;
import com.sundaymorning.coincharge.object.ReceiveFundEntry;

import java.util.ArrayList;

/**
 * Created by sweet on 2017-07-08.
 */

public class RecommActivity extends AppCompatActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recomm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<ReceiveFundEntry> entries = new ArrayList<>();

        entries.add(new ReceiveFundEntry(0, "2017.01.11", "테스트1", "1", "100"));
        entries.add(new ReceiveFundEntry(1, "2017.01.12", "테스트2", "1", "200"));
        entries.add(new ReceiveFundEntry(2, "2017.01.22", "테스트3", "1", "300"));
        entries.add(new ReceiveFundEntry(3, "2017.01.23", "테스트4", "1", "400"));

        ListView mFundListView = (ListView) findViewById(R.id.fund_listview);
//        View header = LayoutInflater.from(mContext).inflate(R.layout.header_receive_fund, null);
        View header = LayoutInflater.from(mContext).inflate(R.layout.header_receive_fund, mFundListView, false);
        mFundListView.addHeaderView(header);

        mFundListView.setAdapter(new ReceiveFundAdapter(mContext, entries));
    }

}
