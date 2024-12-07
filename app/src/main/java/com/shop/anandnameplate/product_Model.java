package com.shop.anandnameplate;

public class product_Model {

    private String product_IconLink;
    private String product_Name;
    private String product_Rate;

    public product_Model(String product_IconLink, String product_Name, String product_Rate) {
        this.product_IconLink = product_IconLink;
        this.product_Name = product_Name;
        this.product_Rate = product_Rate;
    }

    public String getProduct_IconLink() {
        return product_IconLink;
    }

    public void setProduct_IconLink(String product_IconLink) {
        this.product_IconLink = product_IconLink;
    }

    public String getProduct_Name() {
        return product_Name;
    }

    public void setProduct_Name(String product_Name) {
        this.product_Name = product_Name;
    }

    public String getProduct_Rate() {
        return product_Rate;
    }

    public void setProduct_Rate(String product_Rate) {
        this.product_Rate = product_Rate;
    }
}
