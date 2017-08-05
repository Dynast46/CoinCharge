package com.sundaymorning.coincharge;

/**
 * Created by Moons on 2017-04-16.
 */

public class Common {
    public static final boolean NAS_TEST_MODE = false;

    public static final String BASE_URL = "http://freeonappstore.kr/";
    public static final String KAKAO_EMOTICON_TYPE_ID = "KAKAO_EMOTICON_TYPE_ID";
    public static final String MARKET_MONEY = "MARKET_MONEY";
    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    public static final int NOTICE_TYPE = 1;
    public static final int HELP_TYPE = 2;

    public static final String UPDATE_MONEY = "UPDATE_MONEY";
    public static final String FINISH_APP = "FINISH_APP";

    static final String GCM_KEY = "389367641269";

    public interface OnMemberListener {
        public void onDisplay(MemberView view);
    }

    public static interface OnSpKakaoEmoticonInfoListListener {
        public void OnResult(int result);
    }

    public static interface OnSpKakaoEmoticonPurchaseInsertListener {
        public void OnResult(int result);
    }

    public static interface OnUseDataUpdateListener {
        public void OnUpdate(int position);
    }

    public static interface OnSpKakaoEmoticonPurchaseCancelListener {
        public void OnResult(int result);
    }

    public enum MemberView {
        LOGIN, FIND_ACCOUNT, FIND_PASSWORD, CONFIRM, JOIN
    }
}
