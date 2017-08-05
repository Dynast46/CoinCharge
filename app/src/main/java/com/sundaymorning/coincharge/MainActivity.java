package com.sundaymorning.coincharge;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
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

import com.namember.NAMember;
import com.nextapps.naswall.NASWall;
import com.nextapps.naswall.NASWallAdInfo;
import com.sundaymorning.coincharge.activity.HelpActivity;
import com.sundaymorning.coincharge.activity.SettingAcitivity;
import com.sundaymorning.coincharge.data.MemberInfoData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.fragment.MyPageFragment;
import com.sundaymorning.coincharge.fragment.OfferwallFragment;
import com.sundaymorning.coincharge.object.NasEntry;
import com.sundaymorning.coincharge.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener {

    private Context mContext = this;

    private MemberInfoData memberInfoData;

    private ArrayList<NasEntry> nas_entries = new ArrayList<>();
    private ArrayList<NASWallAdInfo> mAdsInfo = new ArrayList<>();
    NavigationView navigationView;
    private int menuItemID = R.id.ads1;
    private TextView mCurrent_Coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initOffer();

        memberInfoData = SharedPreferenceUtils.loadMemberInfoData(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        LinearLayout HeaderView = (LinearLayout) navigationView.getHeaderView(0);

        mCurrent_Coin = (TextView)HeaderView.findViewById(R.id.current_coin);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (menuItemID == R.id.ads1) {
            mAdsInfo.clear();
            nas_entries.clear();
            initOffer();
        }
        mCurrent_Coin.setText(String.format(getResources().getString(R.string.current_coin), memberInfoData.getMoney()));
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
            if (menuItemID != R.id.ads1) {
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                navigationView.getMenu().getItem(0).setChecked(true);
            } else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(mContext, SettingAcitivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fr = null;
        Intent intent = null;

        int id = item.getItemId();
        menuItemID = id;

        if (id == R.id.ads1) {
            fr = new OfferwallFragment(mContext, nas_entries, mAdsInfo);
        }
//        else if (id == R.id.ads2) {
//            return true;
//        } else if (id == R.id.ads3) {
//            return true;
//        } else if (id == R.id.store) {
//            return true;
//        }
        else if (id == R.id.mypage) {
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

        if (id == R.id.notice || id == R.id.help) {
            startActivity(intent);
        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void askQuestion() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("문의사항이 있는 경우 sundaymorning0315.contact@gmail.com로 문의주세요.\n메일을 보내시겠습니까?")
                .setPositiveButton("문의하기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                sendEmail(mContext, "sundaymorning0315.contact@gmail.com",
                                        "["+ mContext.getString(R.string.app_name) + "/" + memberInfoData.getNickName() + "] - 문의사항",
                                        "App이름 : "+ mContext.getString(R.string.app_name) + " <br>닉네임 : " + memberInfoData.getNickName() + "<br>문의내용 : ");

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

    private static void sendEmail(Context context, String to, String title, String message) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(message));
        context.startActivity(Intent.createChooser(emailIntent, "Email:"));
    }
}
