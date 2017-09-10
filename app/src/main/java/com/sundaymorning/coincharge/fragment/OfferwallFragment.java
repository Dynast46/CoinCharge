package com.sundaymorning.coincharge.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.nextapps.naswall.NASWallAdInfo;
import com.sundaymorning.coincharge.MainActivity;
import com.sundaymorning.coincharge.R;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.sundaymorning.coincharge.data.MemberInitData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.object.NasEntry;

import java.util.ArrayList;

/**
 * Created by Dynast on 2017. 5. 21..
 */

@SuppressLint("ValidFragment")
public class OfferwallFragment extends Fragment {

    private Context mContext;
    private ArrayList<NasEntry> mNas_Entries;
    private ArrayList<NASWallAdInfo> mAdsInfo;

    private ViewPager mPager;
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            mLastViewPager = position;
            MainActivity activity = (MainActivity)mContext;
            activity.mLastViewPager = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public OfferwallFragment(Context mContext, ArrayList<NasEntry> mNas_Entries, ArrayList<NASWallAdInfo> adInfos) {
        this.mContext = mContext;
        this.mNas_Entries = mNas_Entries;
        this.mAdsInfo = adInfos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_offerwall, container, false);

        getActivity().setTitle(R.string.app_name);

        TypedValue tv = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true);
        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.addOnPageChangeListener(mPageChangeListener);
        setPager(v);

        return v;
    }

    private void setPager(View v) {
        NavigationAdapter mPagerAdapter = new NavigationAdapter(getFragmentManager(), mContext, mNas_Entries, mAdsInfo);
        mPager.setAdapter(mPagerAdapter);

        mPager.setOffscreenPageLimit(2);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator_maker, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimary));
        slidingTabLayout.setDividerColors(getResources().getColor(android.R.color.transparent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);
    }

    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"NAS", "TNK"};
        private Context context;
        private ArrayList<NasEntry> nas_Entries;
        private ArrayList<NASWallAdInfo> adsInfo;

        NavigationAdapter(FragmentManager fm, Context mContext, ArrayList<NasEntry> mNas_Entries, ArrayList<NASWallAdInfo> adInfos) {
            super(fm);

            this.context = mContext;
            this.nas_Entries = mNas_Entries;
            this.adsInfo = adInfos;
        }

        @Override
        protected Fragment createItem(int position) {
            final int pattern = position % TITLES.length;
            Fragment f;
            switch (pattern) {
                case 1:
                    f = new TnkFragment(context);
                    break;
//                case 2:
//                    f = new IGAWFragment(context);
//                    break;
                default:
                    f = new NasFragment(context, nas_Entries, adsInfo);
                    break;
            }

            Bundle args = new Bundle();
            args.putInt("Fragment", pattern);
            f.setArguments(args);
            return f;
        }

        @Override
        public int getCount() {
            MemberInitData initData = SharedPreferenceUtils.loadMemberInitData(context);

            if (initData.isTNK())
                return 2;
            else
                return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
