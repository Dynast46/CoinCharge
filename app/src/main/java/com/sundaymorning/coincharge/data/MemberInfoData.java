package com.sundaymorning.coincharge.data;

import com.namember.utils.Common;

/**
 * Created by Dynast on 2017. 6. 25..
 */

public class MemberInfoData {

    private String nickName = "";
    private int money = 0;
    private String email = "";
    private String myRecommendNickname = "";
    private int age = 0;
    private Common.MemberSex sex;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMyRecommendNickname() {
        return myRecommendNickname;
    }

    public void setMyRecommendNickname(String myRecommendNickname) {
        this.myRecommendNickname = myRecommendNickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Common.MemberSex getSex() {
        return sex;
    }

    public void setSex(Common.MemberSex sex) {
        this.sex = sex;
    }

}
