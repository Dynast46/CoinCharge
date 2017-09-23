package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fpang.lib.FpangSession;
import com.sundaymorning.coincharge.R;

/**
 * Created by Dynast on 2017. 9. 3..
 */

@SuppressLint("ValidFragment")
public class AdsyncFragment extends Fragment {

    private Context mContext;

    public AdsyncFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adsync, container, false);

        getActivity().setTitle(R.string.adsync_ads);
        setInit();

        FpangSession.showAdSyncFragment(getActivity(), R.id.fragment_adsync, null);
        return v;
    }

    public void setInit() {
        FpangSession.init(mContext);
        FpangSession.setDebug(true);
        FpangSession.setUserId("1234");  //최대 100자  // 추가 정보 설정
        FpangSession.setAge(0); // 0: 값없음
        FpangSession.setGender("A");  // M: 남자, F: 여자, A: 선택없음
    }
}