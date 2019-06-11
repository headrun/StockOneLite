package com.stockone.lite.model;

import java.io.Serializable;

public class SubscriptionModel implements Serializable {

    private long purchase_time;
    private String purchaseState, product_id, purchase_token, user_id;

    public SubscriptionModel() {
    }

    public SubscriptionModel(long purchase_time, String purchaseState, String product_id, String purchase_token, String user_id) {
        this.purchase_time = purchase_time;
        this.purchaseState = purchaseState;
        this.product_id = product_id;
        this.purchase_token = purchase_token;
        this.user_id = user_id;
    }

    public long getPurchase_time() {
        return purchase_time;
    }

    public void setPurchase_time(long purchase_time) {
        this.purchase_time = purchase_time;
    }

    public String getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(String purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPurchase_token() {
        return purchase_token;
    }

    public void setPurchase_token(String purchase_token) {
        this.purchase_token = purchase_token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
