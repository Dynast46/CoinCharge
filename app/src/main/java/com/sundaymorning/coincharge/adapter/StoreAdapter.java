package com.sundaymorning.coincharge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.object.StoreCardEntry;

import java.util.ArrayList;

/**
 * Created by Dynast on 2017. 8. 20..
 */

public class StoreAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<StoreCardEntry> mArrayList;

    public StoreAdapter(Context context, ArrayList<StoreCardEntry> arrayList) {
        this.mContext = context;
        this.mArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mArrayList.get(position)._CARD_TYPE_ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        viewHolder mViewHolder;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.holder_store_items, parent, false);
            mViewHolder = new viewHolder(v);
            v.setTag(mViewHolder);
        } else {
            mViewHolder = (viewHolder) v.getTag();
        }

        if (getItem(position) != null) {
            StoreCardEntry entry = (StoreCardEntry) getItem(position);
            mViewHolder.setProductTitle(entry._NAME);
            mViewHolder.setCash(String.valueOf(entry._MARKET_MONEY) + "코인");
        }

        return v;
    }

    private class viewHolder {
        private View mView;
        private TextView mTitle;
        private TextView mCash;

        viewHolder(View v) {
            mView = v;
        }

        private void setCash(String _cash) {
            if (mCash == null) {
                this.mCash = (TextView) mView.findViewById(R.id.store_product_money);
            }

            mCash.setText(_cash);
        }

        private void setProductTitle(String title) {
            if (mTitle == null) {
                this.mTitle = (TextView) mView.findViewById(R.id.store_product_title);
            }

            mTitle.setText(title);
        }
    }
}
