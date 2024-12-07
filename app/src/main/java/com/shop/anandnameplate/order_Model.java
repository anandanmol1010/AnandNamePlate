package com.shop.anandnameplate;

public class order_Model {

    private String icon,name,qty,rate,englishName,hindiName,extraCharge,orderId,orderedOn,deliveredOn;

    public order_Model(String icon, String name, String qty, String rate, String englishName, String hindiName, String extraCharge, String orderId, String orderedOn, String deliveredOn) {
        this.icon = icon;
        this.name = name;
        this.qty = qty;
        this.rate = rate;
        this.englishName = englishName;
        this.hindiName = hindiName;
        this.extraCharge = extraCharge;
        this.orderId = orderId;
        this.orderedOn = orderedOn;
        this.deliveredOn = deliveredOn;
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

    public String getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(String extraCharge) {
        this.extraCharge = extraCharge;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(String orderedOn) {
        this.orderedOn = orderedOn;
    }

    public String getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(String deliveredOn) {
        this.deliveredOn = deliveredOn;
    }
}
