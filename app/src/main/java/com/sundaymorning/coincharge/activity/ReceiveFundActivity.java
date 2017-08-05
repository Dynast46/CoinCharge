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
import android.widget.AbsListView;
import android.widget.ListView;

import com.namember.NAMember;
import com.namember.data.MoneyChargeData;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.adapter.ReceiveFundAdapter;
import com.sundaymorning.coincharge.object.ReceiveFundEntry;
import com.sundaymorning.coincharge.utils.Utils;

import java.util.ArrayList;

import static com.sundaymorning.coincharge.R.string.network_error_message;

/**
 * Created by Dynast on 2017. 7. 6..
 */

public class ReceiveFundActivity extends AppCompatActivity {

    private Context mContext = this;
    private ListView mFundListView;

    private ArrayList<ReceiveFundEntry> mSaveDataList = new ArrayList<>();
    private ReceiveFundAdapter mReceiveFundAdapter;
    private int mPage = 1;
    private boolean mLockListView = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_receive_fund);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFundListView = (ListView) findViewById(R.id.fund_listview);
        View header = LayoutInflater.from(mContext).inflate(R.layout.header_receive_fund, mFundListView, false);
        mFundListView.addHeaderView(header);

        mReceiveFundAdapter = new ReceiveFundAdapter(mContext, mSaveDataList);
        mFundListView.setAdapter(mReceiveFundAdapter);

        getData();
        initEvent();
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

    private void initEvent() {
        mFundListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int count = totalItemCount - visibleItemCount;

                if (firstVisibleItem >= count && totalItemCount != 0 && !mLockListView) {
                    mLockListView = true;
                    getData();
                }
            }
        });
    }

    private void getData() {
        if (Utils.checkNetworkAvailable(mContext)) {

            Utils.showProgressDialog(mContext);

            NAMember.MoneyChargeList(mContext, mPage, 50, new Common.OnMoneyChargeListListener() {
                @Override
                public void OnSuccess(int i, int i1, ArrayList<MoneyChargeData> arrayList) {

                    Utils.hideProgressDialog(mContext);

                    ArrayList<ReceiveFundEntry> list = new ArrayList<>();
                    for (int Cnt = 0; Cnt < arrayList.size(); Cnt++) {
                        String title = arrayList.get(Cnt).getChargeTypeText() + " / " + arrayList.get(Cnt).getChargeComment();
                        list.add(new ReceiveFundEntry(
                                arrayList.get(Cnt).getMoneyChargeID(),
                                arrayList.get(Cnt).getInsertDate(),
                                title,
                                arrayList.get(Cnt).getChargeTypeText(),
                                String.valueOf(arrayList.get(Cnt).getMoney())));
                    }
                    mSaveDataList.addAll(list);

                    if (mSaveDataList.size() == 0) {
                        Utils.showDialog(mContext,
                                null,
                                getResources().getString(R.string.no_save_list_data),
                                getResources().getString(R.string.ok),
                                null,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }, null);
                    } else {
                        mReceiveFundAdapter.notifyDataSetChanged();
                        mPage++;
                    }
                }

                @Override
                public void OnJson(String s) {

                }

                @Override
                public void OnError(int i) {
                    Utils.hideProgressDialog(mContext);

                    if (mSaveDataList.size() == 0) {
                        Utils.showDialog(mContext,
                                null,
                                getResources().getString(R.string.error_try_again),
                                getResources().getString(R.string.ok),
                                null,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }, null);
                    } else {
                        Utils.showToast(mContext, getResources().getString(R.string.error_try_again));
                    }
                }
            });
        } else {
            if (mSaveDataList.size() == 0) {
                Utils.showDialog(mContext,
                        null,
                        getResources().getString(network_error_message),
                        getResources().getString(R.string.ok),
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }, null);
            } else {
                Utils.showToast(mContext, getResources().getString(network_error_message));
            }
        }
    }
}
