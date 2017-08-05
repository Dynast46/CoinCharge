package com.sundaymorning.coincharge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.holder.FundViewHolder;
import com.sundaymorning.coincharge.object.UseFundEntry;

import java.util.ArrayList;

/**
 * Created by Dynast on 2017. 7. 6..
 */

public class UseFundAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<UseFundEntry> mEntries;

    public UseFundAdapter(Context context, ArrayList<UseFundEntry> entries) {
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
        FundViewHolder fundViewHolder;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.holder_use_fund, parent, false);
            fundViewHolder = new FundViewHolder(v, R.layout.holder_use_fund);
            v.setTag(fundViewHolder);
        } else {
            fundViewHolder = (FundViewHolder) v.getTag();
        }

        if (getItem(position) != null) {
            UseFundEntry entry = (UseFundEntry) getItem(position);

            fundViewHolder.setDate(entry.getDate());
            fundViewHolder.setDescription(entry.getDescription());
            fundViewHolder.setCash(entry.getCash() + " 코인");
        }

        return v;
    }
}
