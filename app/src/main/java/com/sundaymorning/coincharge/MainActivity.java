package com.sundaymorning.coincharge;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igaworks.IgawCommon;
import com.igaworks.adpopcorn.IgawAdpopcorn;
import com.igaworks.displayad.IgawDisplayAd;
import com.namember.NAMember;
import com.nextapps.naswall.NASWall;
import com.nextapps.naswall.NASWallAdInfo;
import com.sundaymorning.coincharge.activity.HelpActivity;
import com.sundaymorning.coincharge.data.LoginData;
import com.sundaymorning.coincharge.data.MemberInfoData;
import com.sundaymorning.coincharge.data.MemberInitData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.fragment.AdsyncFragment;
import com.sundaymorning.coincharge.fragment.IGAWFragment;
import com.sundaymorning.coincharge.fragment.MyPageFragment;
import com.sundaymorning.coincharge.fragment.NasFragment;
import com.sundaymorning.coincharge.fragment.StoreFragment;
import com.sundaymorning.coincharge.fragment.TnkFragment;
import com.sundaymorning.coincharge.object.NasEntry;
import com.sundaymorning.coincharge.utils.Utils;
import com.tnkfactory.ad.TnkSession;
import com.vungle.publisher.AdConfig;
import com.vungle.publisher.VunglePub;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    final VunglePub vunglePub = VunglePub.getInstance();
    final AdConfig globalAdConfig = vunglePub.getGlobalAdConfig();
    public int mLastViewPager;
    NavigationView navigationView;
    private Context mContext = this;
    private MemberInfoData memberInfoData;
    private LoginData memberLoginData;
    private ArrayList<NasEntry> nas_entries = new ArrayList<>();
    private ArrayList<NASWallAdInfo> mAdsInfo = new ArrayList<>();
    private int menuItemID = R.id.nas_ads;
    private TextView mCurrent_Nick;
    private TextView mCurrent_Coin;
    private String vungle_Placement_id;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null && intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(Common.FINISH_APP)) {
                    finish();
                }
            }
        }
    };

    private static void sendEmail(Context context, String title, String message) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sundaymorning0315.contact@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(message));
        context.startActivity(Intent.createChooser(emailIntent, "Email:"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initOffer();
        memberLoginData = SharedPreferenceUtils.loadLogin(mContext);
        memberInfoData = SharedPreferenceUtils.loadMemberInfoData(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        MemberInitData initData = SharedPreferenceUtils.loadMemberInitData(this);

        navigationView.inflateMenu(R.menu.activity_main_drawer);

        MenuItem item;
        if (!initData.isNAS()) {
            item = navigationView.getMenu().findItem(R.id.nas_ads);
            item.setVisible(false);
        }

        if (!initData.isTNK()) {
            item = navigationView.getMenu().findItem(R.id.tnk_ads);
            item.setVisible(false);
        }

        if (!initData.isIGAWORK()) {
            item = navigationView.getMenu().findItem(R.id.adpopcorn);
            item.setVisible(false);
        }

        LinearLayout HeaderView = (LinearLayout) navigationView.getHeaderView(0);

        mCurrent_Coin = (TextView) HeaderView.findViewById(R.id.current_coin);
        mCurrent_Nick = (TextView) HeaderView.findViewById(R.id.current_nick);
        navigationView.setNavigationItemSelectedListener(this);

        TnkSession.setUserName(this, memberLoginData.getMID());

        vungle_Placement_id = getString(R.string.vungle_placement_id);
        vunglePub.loadAd(vungle_Placement_id);
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter f = new IntentFilter();
        f.addAction(Common.FINISH_APP);
        registerReceiver(receiver, f);
    }

    private void unregisterReceiver() {
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (menuItemID == R.id.nas_ads) {
            if (mLastViewPager == 2) {
                IgawCommon.startSession(this);
                IgawCommon.setUserId(this, memberLoginData.getMID());

            } else if (mLastViewPager == 1) {
                TnkSession.setUserName(this, memberLoginData.getMID());
            } else {
                mAdsInfo.clear();
                nas_entries.clear();
                initOffer();
            }
        }

        vunglePub.onResume();
        mCurrent_Nick.setText(memberInfoData.getNickName());
        mCurrent_Coin.setText(String.format(getResources().getString(R.string.current_coin1), memberInfoData.getMoney()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IgawDisplayAd.destroy();
        unregisterReceiver();
    }

    private void initOffer() {
        Utils.showProgressDialog(mContext);
        NASWall.init(mContext, BuildConfig.NAS_TEST);
        NASWall.getAdList(mContext, NAMember.getMemberID(mContext), new NASWall.OnAdListListener() {
            @Override
            public void OnSuccess(ArrayList<NASWallAdInfo> arrayList) {
                for (NASWallAdInfo info : arrayList) {
                    nas_entries.add(
                            new NasEntry(
                                    info.getAdId(),
                                    info.getTitle(),
                                    info.getMissionText(),
                                    info.getIconUrl(),
                                    String.valueOf(info.getRewardPrice())));
                }

                mAdsInfo.addAll(arrayList);

                navigationView.getMenu().getItem(0).setChecked(true);
                onNavigationItemSelected(navigationView.getMenu().getItem(0));

                Utils.hideProgressDialog(mContext);
            }

            @Override
            public void OnError(int i) {
                Utils.hideProgressDialog(mContext);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (menuItemID != R.id.nas_ads) {
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                navigationView.getMenu().getItem(0).setChecked(true);
            } else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(mContext, SettingAcitivity.class));
//            return true;
//        }
//
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        android.support.v4.app.Fragment fr = null;
        Intent intent = null;

        int id = item.getItemId();
        menuItemID = id;

        if (id == R.id.nas_ads) {
//            fr = new OfferwallFragment(mContext, nas_entries, mAdsInfo);
            fr = new NasFragment(mContext, nas_entries, mAdsInfo);
        } else if (id == R.id.tnk_ads) {
            fr = new TnkFragment(mContext);
        } else if (id == R.id.adsync) {
            fr = new AdsyncFragment(mContext);
        } else if (id == R.id.mypage) {
            fr = new MyPageFragment(mContext);
        } else if (id == R.id.question) {
            askQuestion();
            return true;
        } else if (id == R.id.notice) {
            intent = new Intent(mContext, HelpActivity.class);
            intent.putExtra(Common.TYPE, Common.NOTICE_TYPE);
        } else if (id == R.id.help) {
            intent = new Intent(mContext, HelpActivity.class);
            intent.putExtra(Common.TYPE, Common.HELP_TYPE);
        }
//        else if (id == R.id.settings) {
//            fr = new SettingFragment(mContext);
//        }
        else if (id == R.id.store) {
            fr = new StoreFragment(mContext);
        } else if (id == R.id.adpopcorn) {
            IgawAdpopcorn.openOfferWall(this);
//            fr = new IGAWFragment(mContext);
            return true;
        } else if (id == R.id.vungle) {
            if (vunglePub.isAdPlayable(vungle_Placement_id)) {
                vunglePub.playAd(vungle_Placement_id, globalAdConfig);
            } else {
                Toast.makeText(mContext, "isPlayable False", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.notice || id == R.id.help) {
            startActivity(intent);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    private void askQuestion() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("문의사항이 있는 경우 sundaymorning0315.contact@gmail.com로 문의주세요.\n메일을 보내시겠습니까?")
                .setPositiveButton("문의하기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                sendEmail(mContext,
                                        "[" + mContext.getString(R.string.app_name) + "/" + memberInfoData.getNickName() + "] - 문의사항",
                                        "App이름 : " + mContext.getString(R.string.app_name) + " <br>닉네임 : " + memberInfoData.getNickName() + "<br>문의내용 : ");

                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alt_bld.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setTitle(R.string.app_name);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 유저가 선택한 결과를 SDK에 넘겨줌.
//        if(offerwallLayout != null)
//            offerwallLayout.onRequestPermissionsResult(requestCode,permissions, grantResults);
    }
}