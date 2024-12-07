package com.shop.anandnameplate;

public class wishlist_Model {

    private String icon, name, rate;

    public wishlist_Model(String icon, String name, String rate) {
        this.icon = icon;
        this.name = name;
        this.rate = rate;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}