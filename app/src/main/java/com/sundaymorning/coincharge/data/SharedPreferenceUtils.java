package com.sundaymorning.coincharge.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.namember.utils.Common;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sweet on 2017-07-08.
 */

public class SharedPreferenceUtils {

    private static final String MEMBER = "member";
    private static final String MARKET_MOVE_URL = "marketMoveURL";
    private static final String NOTICE_ID = "noticeID";
    private static final String HELP_ID = "helpID";
    private static final String VERSION = "version";
    private static final String MEMBER_INFO = "memberInfo";
    private static final String NICKNAME = "nickname";
    private static final String MONEY = "money";
    private static final String EMAIL = "email";
    private static final String MY_RECOMMEND_NICKNAME = "myRecommendNickname";
    private static final String AGE = "age";
    private static final String SEX = "sex";
    private static final String IGAW = "igaw";
    private static final String NAS = "nas";
    private static final String TNK = "tnk";

    public static boolean saveMemberInitData(Context context, MemberInitData memberInitData) {
        if (memberInitData == null)
            return false;

        SharedPreferences pref = context.getSharedPreferences(MEMBER, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(MARKET_MOVE_URL, memberInitData.getMarketMoveURL());
        editor.putInt(NOTICE_ID, memberInitData.getNoticeID());
        editor.putInt(HELP_ID, memberInitData.getHelpID());
        editor.putString(VERSION, String.valueOf(memberInitData.getVersion()));
        editor.putBoolean(IGAW, memberInitData.isIGAWORK());
        editor.putBoolean(NAS, memberInitData.isNAS());
        editor.putBoolean(TNK, memberInitData.isTNK());

        return editor.commit();
    }

    public static MemberInitData loadMemberInitData(Context context) {
        MemberInitData memberInitData = new MemberInitData();

        SharedPreferences pref = context.getSharedPreferences(MEMBER, MODE_PRIVATE);
        memberInitData.setMarketMoveURL(pref.getString(MARKET_MOVE_URL, ""));
        memberInitData.setNoticeID(pref.getInt(NOTICE_ID, 0));
        memberInitData.setHelpID(pref.getInt(HELP_ID, 0));
        memberInitData.setVersion(pref.getString(VERSION, ""));
        memberInitData.setIGAWORK(pref.getBoolean(IGAW, false));
        memberInitData.setTNK(pref.getBoolean(TNK, false));
        memberInitData.setNAS(pref.getBoolean(NAS, false));

        return memberInitData;
    }

    public static boolean removeMemberInitData(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MEMBER, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        return editor.commit();
    }

    public static boolean saveMemberInfoData(Context context, MemberInfoData memberInfoData) {
        if (memberInfoData == null)
            return false;

        SharedPreferences pref = context.getSharedPreferences(MEMBER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(NICKNAME, memberInfoData.getNickName());
        editor.putString(EMAIL, memberInfoData.getEmail());
        editor.putString(MY_RECOMMEND_NICKNAME, memberInfoData.getMyRecommendNickname());
        editor.putInt(MONEY, memberInfoData.getMoney());
        editor.putInt(AGE, memberInfoData.getAge());
        editor.putInt(SEX, memberInfoData.getSex().ordinal());

        return editor.commit();
    }

    public static MemberInfoData loadMemberInfoData(Context context) {
        MemberInfoData memberInfoData = new MemberInfoData();

        SharedPreferences p = context.getSharedPreferences(MEMBER_INFO, MODE_PRIVATE);
        memberInfoData.setNickName(p.getString(NICKNAME, ""));
        memberInfoData.setEmail(p.getString(EMAIL, ""));
        memberInfoData.setMyRecommendNickname(p.getString(MY_RECOMMEND_NICKNAME, ""));
        memberInfoData.setMoney(p.getInt(MONEY, 0));
        memberInfoData.setAge(p.getInt(AGE, 0));

        int sexOrdinal = p.getInt(SEX, 0);
        Common.MemberSex sex = Common.MemberSex.NONE;
        if (sexOrdinal == 1) sex = Common.MemberSex.MALE;
        else if (sexOrdinal == 2) sex = Common.MemberSex.FEMALE;

        memberInfoData.setSex(sex);

        return memberInfoData;
    }

    public static boolean removeMemberInfoData(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MEMBER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        return editor.commit();
    }


    private static final String JOIN = "join";
    private static final String PHONE_NUMBER = "phone_number";

    public static boolean savePhoneNumber(Context context, String number) {
        if (number == null || number.isEmpty())
            return false;

        SharedPreferences pref = context.getSharedPreferences(JOIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PHONE_NUMBER, number);
        return editor.commit();
    }

    public static String loadPhoneNumber(Context context) {
        SharedPreferences pref = context.getSharedPreferences(JOIN, MODE_PRIVATE);
        return pref.getString(PHONE_NUMBER, "");
    }

    public static boolean removePhoneNumber(Context context) {
        SharedPreferences pref = context.getSharedPreferences(JOIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        return editor.commit();
    }


    private static final String LOGIN = "login";
    private static final String MEMBER_ID = "memberID";
    private static final String LOGIN_KEY = "loginKey";
    private static final String MEMBER_DEVICE_ID = "memberDeviceID";

    public static boolean saveLogin(Context context, LoginData loginData) {
        if (loginData == null)
            return false;

        SharedPreferences pref = context.getSharedPreferences(LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(MEMBER_ID, loginData.getMID());
        editor.putString(LOGIN_KEY, loginData.getLKEY());
        editor.putString(MEMBER_DEVICE_ID, loginData.getMDID());
        return editor.commit();
    }

    public static LoginData loadLogin(Context context) {

        LoginData mLoginData = new LoginData();

        SharedPreferences pref = context.getSharedPreferences(LOGIN, MODE_PRIVATE);
        mLoginData.setMID(pref.getString(MEMBER_ID, ""));
        mLoginData.setLKEY(pref.getString(LOGIN_KEY, ""));
        mLoginData.setMDID(pref.getString(MEMBER_DEVICE_ID, ""));

        return mLoginData;
    }

    public static boolean removeLogin(Context context) {
        SharedPreferences pref = context.getSharedPreferences(LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        return editor.commit();
    }

    public static void removeAllData(Context context){
        removeMemberInfoData(context);
        removeLogin(context);
        removeMemberInitData(context);
        removePhoneNumber(context);
    }
}
