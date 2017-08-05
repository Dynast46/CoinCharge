package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sundaymorning.coincharge.R;
import com.tnkfactory.ad.AdListView;
import com.tnkfactory.ad.TnkAdListener;
import com.tnkfactory.ad.TnkLayout;
import com.tnkfactory.ad.TnkSession;

/**
 * Created by sweet on 2017-08-05.
 */

@SuppressLint("ValidFragment")
public class StoreFragment extends Fragment {

    private Context mContext;

    public StoreFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store, container, false);

        getActivity().setTitle(R.string.store);

        return v;
    }


    private TnkLayout makePopupLayout() {
        TnkLayout res = new TnkLayout();

        res.adwall.numColumnsPortrait = 1;
        res.adwall.numColumnsLandscape = 3;

        res.adwall.layout = R.layout.content_store;
        res.adwall.idTitle = 0;
        res.adwall.idList = R.id.adlistView;
        res.adwall.idClose = 0;

        res.adwall.item.layout = R.layout.holder_tnk;
        res.adwall.item.height = 80;
        res.adwall.item.idIcon = R.id.icon_image;
        res.adwall.item.idTitle = R.id.tnk_title;
        res.adwall.item.idSubtitle = R.id.tnk_subtitle;
        res.adwall.item.idTag = R.id.tnk_btn;

        res.adwall.item.tag.bgTagFree = R.drawable.btn_layout;
        res.adwall.item.tag.bgTagPaid = R.drawable.btn_layout;
        res.adwall.item.tag.bgTagWeb = R.drawable.btn_layout;
        res.adwall.item.tag.bgTagCheck = R.drawable.btn_layout;

        res.adwall.item.tag.tcTagFree = 0xffffffff;
        res.adwall.item.tag.tcTagPaid = 0xffffffff;
        res.adwall.item.tag.tcTagWeb = 0xffffffff;
        res.adwall.item.tag.tcTagCheck = 0xffffffff;

        return res;

    }

    private TnkAdListener mTnkAdListener = new TnkAdListener() {
        @Override
        public void onClose(int i) {

        }

        @Override
        public void onShow() {

        }

        @Override
        public void onFailure(int i) {

        }

        @Override
        public void onLoad() {

        }
    };


}
