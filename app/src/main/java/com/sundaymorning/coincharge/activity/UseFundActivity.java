package com.sundaymorning.coincharge.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.namember.NAMember;
import com.namember.data.MoneyUseData;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.adapter.UseFundAdapter;
import com.sundaymorning.coincharge.object.ReceiveFundEntry;
import com.sundaymorning.coincharge.object.UseFundEntry;
import com.sundaymorning.coincharge.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Dynast on 2017. 7. 7..
 */

public class UseFundActivity extends AppCompatActivity {

    private Context mContext = this;

    private final int SIZE = 30;
    private int mPage = 1;
    private ArrayList<UseFundEntry> mEntries = new ArrayList<>();

    private ListView mFundListView;
    private UseFundAdapter mUseFundAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_receive_fund);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFundListView = (ListView) findViewById(R.id.fund_listview);
        View header = LayoutInflater.from(mContext).inflate(R.layout.header_use_fund, mFundListView, false);
        mFundListView.addHeaderView(header);

        mUseFundAdapter = new UseFundAdapter(mContext, mEntries);
        mFundListView.setAdapter(mUseFundAdapter);

        getUseListData();

        mFundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                DetailDialog detailDialog = new DetailDialog(mContext);
//                detailDialog.init(mUseListAdapter.getItem(i), i, mOnUseDataUpdateListener);
//                detailDialog.show();
            }
        });
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



    private void getUseListData() {
        if (Utils.checkNetworkAvailable(mContext)) {

            Utils.showProgressDialog(mContext);
            NAMember.MoneyUseList(mContext, mPage, SIZE, new Common.OnMoneyUseListListener() {
                @Override
                public void OnSuccess(int i, int i1, ArrayList<MoneyUseData> arrayList) {

                    Utils.hideProgressDialog(mContext);
                    // i == TOTAL COUNT
                    if (mPage == 1) {
                        mEntries.clear();
                    }


                    if (arrayList.size() != 0) {

                        ArrayList<UseFundEntry> list = new ArrayList<>();
                        for (int Cnt = 0; Cnt < arrayList.size(); Cnt++) {
                            String title = arrayList.get(Cnt).getUseTypeText() + " / " + arrayList.get(Cnt).getUseComment();
                            list.add(new UseFundEntry(
                                    arrayList.get(Cnt).getMoneyUseID(),
                                    arrayList.get(Cnt).getInsertDate(),
                                    title,
                                    arrayList.get(Cnt).getUseTypeText(),
                                    String.valueOf(arrayList.get(Cnt).getMoney()),
                                    arrayList.get(Cnt).getUseStatusText()));
                        }
                        mEntries.addAll(list);
                        mPage++;
                        mUseFundAdapter.notifyDataSetChanged();

                    } else {
                        if (mPage == 1) {
                            Utils.showDialog(mContext,
                                    getResources().getString(R.string.no_list_data),
                                    getResources().getString(R.string.no_use_list_data),
                                    getResources().getString(R.string.ok),
                                    null,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    }, null);
                        }
                    }
                }

                @Override
                public void OnJson(String s) {

                }

                @Override
                public void OnError(int i) {
                    Utils.hideProgressDialog(mContext);

                    Utils.showDialog(mContext,
                            getResources().getString(R.string.error),
                            "[" + i + "] " + getResources().getString(R.string.error_try_again),
                            getResources().getString(R.string.ok),
                            null,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }, null);
                }
            });
        } else {
            Utils.showDialog(mContext,
                    getResources().getString(R.string.error),
                    getResources().getString(R.string.network_error_message),
                    getResources().getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }, null);
        }
    }

//    private com.sundaymorning.emoticon.Common.OnUseDataUpdateListener mOnUseDataUpdateListener = new com.sundaymorning.emoticon.Common.OnUseDataUpdateListener() {
//        @Override
//        public void OnUpdate(int position) {
//            mUseDataList.get(position).setUseStatus(3);
//            mUseDataList.get(position).setUseStatusText("환불");
//
//            mUseListAdapter.notifyDataSetChanged();
//        }
//    };
}
