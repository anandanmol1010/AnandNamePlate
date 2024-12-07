package com.shop.anandnameplate;

public class cart_Model {

    private String icon,name,qty,rate,englishName,hindiName;

    public cart_Model(String icon, String name, String qty, String rate, String englishName, String hindiName) {
        this.icon = icon;
        this.name = name;
        this.qty = qty;
        this.rate = rate;
        this.englishName = englishName;
        this.hindiName = hindiName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getHindiName() {
        return hindiName;
    }

    public void setHindiName(String hindiName) {
        this.hindiName = hindiName;
    }
}
