package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nextapps.naswall.NASWallAdInfo;
import com.sundaymorning.coincharge.R;
import com.sundaymorning.coincharge.adapter.NasAdapter;
import com.sundaymorning.coincharge.object.NasEntry;

import java.util.ArrayList;

/**
 * Created by Dynast on 2017. 5. 21..
 */

@SuppressLint("ValidFragment")
public class NasFragment extends Fragment {

    private Context mContext;
    private ArrayList<NasEntry> mNas_Entries;
    private ArrayList<NASWallAdInfo> mAdsInfo;

    public NasFragment(Context mContext, ArrayList<NasEntry> mNas_Entries, ArrayList<NASWallAdInfo> adInfos) {
        this.mContext = mContext;
        this.mNas_Entries = mNas_Entries;
        this.mAdsInfo = adInfos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nas, container, false);

        ListView mOfferwall_List = (ListView) v.findViewById(R.id.nas_offer);
        mOfferwall_List.setAdapter(new NasAdapter(mContext, mNas_Entries, mAdsInfo));

        return v;
    }
}
