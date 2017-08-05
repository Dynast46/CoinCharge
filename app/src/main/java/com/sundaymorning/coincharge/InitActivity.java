package com.sundaymorning.coincharge;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.namember.NAMember;
import com.namember.data.InitData;
import com.namember.data.MemberData;
import com.namember.data.MemberLoginData;
import com.sundaymorning.coincharge.activity.MemberActivity;
import com.sundaymorning.coincharge.data.MemberInfoData;
import com.sundaymorning.coincharge.data.MemberInitData;
import com.sundaymorning.coincharge.data.SharedPreferenceUtils;
import com.sundaymorning.coincharge.utils.Utils;


/**
 * Created by Dynast on 2017. 6. 18..
 */

public class InitActivity extends Activity {

    private Context mContext = this;

    private String mid;
    private String lkey;
    private String mdid;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.GET_ACCOUNTS,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private com.namember.utils.Common.OnMemberViewListener mOnMemberViewListener = new com.namember.utils.Common.OnMemberViewListener() {

        @Override
        public void onJson(String json) {
        }

        @Override
        public void onError(int errorCode) {
            Utils.Log("MEMBER VIEW ERROR CODE : " + errorCode);
            moveLogin();
        }

        @Override
        public void OnSuccess(MemberData memberData) {

            if (memberData != null) {

                MemberInfoData memberInfoData = new MemberInfoData();
                memberInfoData.setNickName(memberData.getMemberNickname());
                memberInfoData.setMoney(memberData.getMoney());
                memberInfoData.setMyRecommendNickname(memberData.getRecommenderMemberNickName());
                memberInfoData.setEmail(memberData.getMemberEmail());
                memberInfoData.setAge(memberData.getAge());
                memberInfoData.setSex(memberData.getSex());

                SharedPreferenceUtils.saveMemberInfoData(mContext, memberInfoData);

                moveMain();
            }
        }
    };
    private com.namember.utils.Common.OnMemberAutoLoginListener mOnMemberAutoLoginListener = new com.namember.utils.Common.OnMemberAutoLoginListener() {

        @Override
        public void OnJson(String json) {
        }

        @Override
        public void OnSuccess(MemberLoginData memberLoginData) {

            Utils.Log("@@@@ : " + memberLoginData.getMemberID());
            Utils.Log("@@@@ : " + memberLoginData.getLoginKey());
            Utils.Log("@@@@ : " + memberLoginData.getMemberDeviceID());

            loginInfo();
        }

        @Override
        public void OnError(int errorCode) {
            String message = "[" + errorCode + "] 로그인에 실패하였습니다.\n잠시 후 다시 시도해주세요.";
            if (errorCode == -300) {
                message = "차단된 계정입니다.\n문의가 있으시면 sundaymorning0315.contact@gmail.com으로 문의주세요.";
            } else if (errorCode == -9999) {
                message = "네트워크 오류가 발생하였습니다.\n인터넷 연결상태를 확인 후 다시 시도해주세요.";
            } else {
                Utils.Log("AUTO LOGIN ERROR CODE : " + errorCode);
                moveLogin();
                return;
            }

            Utils.showToast(mContext, message);
        }
    };
    private com.namember.utils.Common.OnMemberInitListener mOnMemberInitListener = new com.namember.utils.Common.OnMemberInitListener() {
        @Override
        public void OnSuccess(InitData initData) {
            if (initData != null) {

                MemberInitData data = new MemberInitData();
                data.setNAS(initData.isUseNAS());
                data.setTNK(initData.isUseTNK());
                data.setIGAWORK(initData.isUseIGAW());
                data.setVersion(initData.getVersion());
                data.setHelpID(initData.getHelpID());
                data.setNoticeID(initData.getNoticeID());
                data.setMarketMoveURL(initData.getCompulsoryMarkertMoveURL());

                SharedPreferenceUtils.saveMemberInitData(mContext, data);

                if (initData.getUseCompulsoryUpdate() == 1) {
                    forceUpdate(initData.getCompulsoryMarkertMoveURL());
                } else {
                    autoLogin();
                }
            }
        }

        @Override
        public void OnError(int i) {
            if (i == -9999) {
                Utils.showDialog(mContext,
                        null,
                        getResources().getString(R.string.network_error_message),
                        getResources().getString(R.string.ok),
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                close();
                            }
                        }, null);
            } else {
                //초기화에 실패하였습니다.
                Utils.showDialog(mContext,
                        null,
                        "[" + i + "] " + getResources().getString(R.string.re_try_message),
                        getResources().getString(R.string.ok),
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                close();
                            }
                        }, null);
            }
        }

        @Override
        public void OnJson(String s) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        initData();
    }

    private void initData() {
        NAGcm.registerGCM(this, Common.GCM_KEY);

        mid = NAMember.getMemberID(mContext);
        lkey = NAMember.getLoginKey(mContext);
        mdid = NAMember.getMemberDeviceID(mContext);

        requestPermission();
    }

    private void requestPermission() {
    // 마시멜로우 이상
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             if (!hasPermissions(mContext, PERMISSIONS)) {
                 ActivityCompat.requestPermissions(this, PERMISSIONS, 123);
             } else
                 doSomething();
         } else
             doSomething();
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            String p = permissions[i];
            if (p.equals(android.Manifest.permission.READ_PHONE_STATE) && grantResults[i] == PackageManager.PERMISSION_GRANTED)
                doSomething();
        }
    }

    private void doSomething() {
        NAMember.MemberInit(mContext,
                false,
                BuildConfig.APP_ID,
                mid,
                lkey,
                mdid,
                BuildConfig.DEVICE_TYPE,
                com.namember.utils.Common.USE_APP.AUTO_CASH,
                mOnMemberInitListener);
    }

    private void close() {
        finish();
    }

    //강제 업데이트
    private void forceUpdate(final String marketMoveURL) {
        Utils.showDialog(mContext,
                null,
                getResources().getString(R.string.force_update_message),
                getResources().getString(R.string.update),
                null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            String sendLink = marketMoveURL;

                            if (sendLink == null || sendLink.isEmpty()) {
                                sendLink = "";
                            }

                            Intent intent = Intent.parseUri(sendLink, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mContext.startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        close();
                    }
                }, null);
    }

    // 자동 로그인
    private void autoLogin() {
        String token = "";
        NAMember.MemberAutoLogin(mContext, token, "", mOnMemberAutoLoginListener);
    }

    // 회원 정보
    private void loginInfo() {
        Utils.Log("1 @@@@@@@@@ : " + NAMember.getMemberID(mContext));
        Utils.Log("2 @@@@@@@@@ : " + NAMember.getLoginKey(mContext));
        Utils.Log("3 @@@@@@@@@ : " + NAMember.getMemberDeviceID(mContext));

        NAMember.MemberView(mContext, mOnMemberViewListener);
    }

    private void moveLogin() {
        Intent intent = new Intent(mContext, MemberActivity.class);
//        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveMain() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
