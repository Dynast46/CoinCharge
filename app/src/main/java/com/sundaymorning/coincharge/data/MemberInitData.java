package com.sundaymorning.coincharge.data;

/**
 * Created by Dynast on 2017. 6. 25..
 */

public class MemberInitData {

    private String marketMoveURL = "";
    private int noticeID = 0;
    private int helpID = 0;
    private String version = "";
    private boolean isNAS = false;
    private boolean isTNK = false;
    private boolean isIGAWORK = false;

    public String getMarketMoveURL() {
        return marketMoveURL;
    }

    public void setMarketMoveURL(String marketMoveURL) {
        this.marketMoveURL = marketMoveURL;
    }

    public int getNoticeID() {
        return noticeID;
    }

    public void setNoticeID(int noticeID) {
        this.noticeID = noticeID;
    }

    public int getHelpID() {
        return helpID;
    }

    public void setHelpID(int helpID) {
        this.helpID = helpID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isNAS() {
        return isNAS;
    }

    public void setNAS(boolean NAS) {
        isNAS = NAS;
    }

    public boolean isTNK() {
        return isTNK;
    }

    public void setTNK(boolean TNK) {
        isTNK = TNK;
    }

    public boolean isIGAWORK() {
        return isIGAWORK;
    }

    public void setIGAWORK(boolean IGAWORK) {
        isIGAWORK = IGAWORK;
    }

}
