package com.sundaymorning.coincharge.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.namember.NAMember;
import com.namember.data.NoticeData;
import com.sundaymorning.coincharge.Common;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.adapter.HelpExpandableAdapter;
import com.sundaymorning.coincharge.object.HelpNoticeEntry;
import com.sundaymorning.coincharge.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Dynast on 2017. 7. 7..
 */

public class HelpActivity extends AppCompatActivity {

    Context mContext = this;
    private int mType = 0;
    private int mPage = 1;
    private int mPageLimit = 30;

    private ExpandableListView mExpanableListView;
    private boolean mLockListView = true;
    private ArrayList<HelpNoticeEntry> mNoticeDataList;
    private HelpExpandableAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mExpanableListView = (ExpandableListView) findViewById(R.id.help_listView);
        mExpanableListView.setGroupIndicator(null);
        initType();

        mExpanableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int count = totalItemCount - visibleItemCount;

                if (firstVisibleItem >= count && totalItemCount != 0 && !mLockListView) {
                    mLockListView = true;
                    getListData();
                }
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

    private void initType() {
        mType = getIntent().getIntExtra(Common.TYPE, Common.NOTICE_TYPE);

        if (mType == Common.NOTICE_TYPE) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(getString(R.string.notice));
        } else {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(getString(R.string.help));
        }

        mNoticeDataList = new ArrayList<>();
        mAdapter = new HelpExpandableAdapter(mContext, mNoticeDataList);
        mExpanableListView.setAdapter(mAdapter);
        getListData();
    }

    private void getListData() {
        if (Utils.checkNetworkAvailable(mContext)) {

            Utils.showProgressDialog(mContext);

            NAMember.NoticeList(mContext, mPage, mPageLimit, mType, "", new com.namember.utils.Common.OnNoticeListListener() {
                @Override
                public void onSuccess(int totalCount, int newPage, ArrayList<NoticeData> arrayList) {
                    Utils.hideProgressDialog(mContext);

                    if (totalCount == 0) {
                        Utils.showDialog(mContext,
                                null,
                                getResources().getString(R.string.no_data),
                                getResources().getString(R.string.ok),
                                null,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                }, null);
                    } else {
                        ArrayList<HelpNoticeEntry> list = new ArrayList<>();
                        for (int Cnt = 0; Cnt < arrayList.size(); Cnt++) {
                            list.add(new HelpNoticeEntry(
                                    arrayList.get(Cnt).getNoticeID(),
                                    arrayList.get(Cnt).getTitle(),
                                    arrayList.get(Cnt).getContents(),
                                    arrayList.get(Cnt).getInsertDate()));
                        }
                        mNoticeDataList.addAll(list);
                        if (totalCount > mNoticeDataList.size()) {
                            mLockListView = false;
                            mPage++;
                        }

                        for( int i = 0; i< mNoticeDataList.size(); i++){
                            Utils.Log(mNoticeDataList.get(i).getTitle());
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void OnError(int i) {
                    Utils.hideProgressDialog(mContext);
                }

                @Override
                public void OnJson(String s) {
                    Utils.hideProgressDialog(mContext);
                }
            });
        } else {
            Utils.showDialog(mContext,
                    null,
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
}
