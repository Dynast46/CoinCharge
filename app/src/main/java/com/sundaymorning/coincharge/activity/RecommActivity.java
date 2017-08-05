package com.sundaymorning.coincharge.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.namember.NAMember;
import com.namember.data.RecommenderData;
import com.namember.utils.Common;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.adapter.ReceiveFundAdapter;
import com.sundaymorning.coincharge.adapter.UseFundAdapter;
import com.sundaymorning.coincharge.data.MemberInfoData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.holder.FundViewHolder;
import com.sundaymorning.coincharge.holder.RecommViewHolder;
import com.sundaymorning.coincharge.object.ReceiveFundEntry;
import com.sundaymorning.coincharge.object.UseFundEntry;

import java.util.ArrayList;

/**
 * Created by sweet on 2017-07-08.
 */

public class RecommActivity extends AppCompatActivity {

    private Context mContext = this;
    private EditText mCompleteMission;

    private RecommAdapter mRecommAdapter;
    private ListView mRecomm_ListView;

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

//        ListView mFundListView = (ListView) findViewById(R.id.fund_listview);
//
//        View header = LayoutInflater.from(mContext).inflate(R.layout.header_receive_fund, mFundListView, false);
//        mFundListView.addHeaderView(header);
//
//        mFundListView.setAdapter(new ReceiveFundAdapter(mContext, entries));

        MemberInfoData memberInfoData = SharedPreferenceUtils.loadMemberInfoData(mContext);

        EditText mFriend_text = (EditText) findViewById(R.id.text_friend);
        mFriend_text.setText(memberInfoData.getMyRecommendNickname());

        mCompleteMission = (EditText) findViewById(R.id.complete_mission);

        mRecomm_ListView = (ListView) findViewById(R.id.recomm_listview);

        NAMember.MemberRecommenderList(mContext, 1, 100, new Common.OnMemberRecommenderListListener() {
            @Override
            public void OnSuccess(int i, int i1, String s, ArrayList<RecommenderData> arrayList) {
                mCompleteMission.setText(s);

                mRecommAdapter = new RecommAdapter(mContext, arrayList);
                mRecomm_ListView.setAdapter(mRecommAdapter);
            }

            @Override
            public void OnError(int i) {

            }

            @Override
            public void OnJson(String s) {

            }
        });
    }

    public class RecommAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<RecommenderData> mEntries;

        public RecommAdapter(Context context, ArrayList<RecommenderData> entries) {
            this.mContext = context;
            this.mEntries = entries;
        }

        @Override
        public int getCount() {
            return mEntries.size();
        }

        @Override
        public Object getItem(int position) {
            return mEntries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            RecommViewHolder recommViewHolder;

            if (v == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                v = inflater.inflate(R.layout.holder_recomm, parent, false);
                recommViewHolder = new RecommViewHolder(v);
                v.setTag(recommViewHolder);
            } else {
                recommViewHolder = (RecommViewHolder) v.getTag();
            }

            if (getItem(position) != null) {
                RecommenderData entry = (RecommenderData) getItem(position);

                recommViewHolder.setDate(entry.getInsertDate());
                recommViewHolder.setDescription(entry.getRecommenderMemberNickname());
                recommViewHolder.setMission(entry.getEventChargeStatusText());
                recommViewHolder.setFriend(String.valueOf(entry.getRecommender2deptCount()));

            }

            return v;
        }
    }
}
