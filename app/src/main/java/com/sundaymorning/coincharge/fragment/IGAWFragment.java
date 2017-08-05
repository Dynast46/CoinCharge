package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igaworks.displayad.IgawDisplayAd;
import com.igaworks.displayad.nativead.IgawNativeAd;
import com.igaworks.displayad.nativead.IgawNativeAdErrorCode;
import com.igaworks.displayad.nativead.IgawNativeAdListener;
import com.sundaymorning.coincharge.R;

/**
 * Created by sweet on 2017-08-05.
 */

@SuppressLint("ValidFragment")
public class IGAWFragment extends Fragment {

    private Context mContext;

    public IGAWFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store, container, false);

//        setTnkStyle();
//        TnkLayout layout = makePopupLayout();
//        AdListView adlistView = TnkSession.createAdListView(mContext, layout);
//
//        RelativeLayout adlistLayout = (RelativeLayout) v.findViewById(R.id.adlist_layout);
//        adlistLayout.addView(adlistView);
//        adlistView.setListener(mTnkAdListener);
//        adlistView.loadAdList();

//        IgawAdpopcorn.openOfferWall(getActivity());
        IgawDisplayAd.init(getActivity());
        IgawDisplayAd.setRefreshTime(40);

        IgawNativeAd nativeAd = new IgawNativeAd(mContext, "b299344870",
                new IgawNativeAdListener() {
                    @Override
                    public void OnNativeAdRequestSucceeded(IgawNativeAd nativeAdInfo) {

                    }

                    @Override
                    public void OnNativeAdRequestFailed(IgawNativeAdErrorCode daErrorCode) {
                        Log.d("DEBUG", "NativeAd Loaing Fail, ResultCode : " + daErrorCode.getErrorCode() + ", ResultMsg : " + daErrorCode.getErrorMessage());
                    }
                });

        nativeAd.loadAd();
        return v;
    }
}
