package com.shop.anandnameplate;

public class UserDataClass {

    int no_of_referral;
    String code,phone_number,used_Referral,name,unit_name,imageUrl,uid;

    public UserDataClass() {
    }

    public UserDataClass(int no_of_referral, String code, String phone_number, String used_Referral, String name, String unit_name, String imageUrl, String uid) {
        this.no_of_referral = no_of_referral;
        this.code = code;
        this.phone_number = phone_number;
        this.used_Referral = used_Referral;
        this.name = name;
        this.unit_name = unit_name;
        this.imageUrl = imageUrl;
        this.uid = uid;
    }

    public int getNo_of_referral() {
        return no_of_referral;
    }

    public void setNo_of_referral(int no_of_referral) {
        this.no_of_referral = no_of_referral;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUsed_Referral() {
        return used_Referral;
    }

    public void setUsed_Referral(String used_Referral) {
        this.used_Referral = used_Referral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
