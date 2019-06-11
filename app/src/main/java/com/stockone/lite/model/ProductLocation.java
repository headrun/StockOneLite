package com.stockone.lite.model;

import java.io.Serializable;

public class ProductLocation implements Serializable{

    private String location_name, user_id, product_id;

    private long careated;

    private int product_quantity;
    private String product_edt_qty;

    private String firebase_key;

    public ProductLocation() {
    }

    public String getProduct_edt_qty() {
        return product_edt_qty;
    }

    public void setProduct_edt_qty(String product_edt_qty) {
        this.product_edt_qty = product_edt_qty;
    }

    public ProductLocation(String location_name, String user_id, String product_id, int product_quantity, long careated, String firebase_key) {
        this.location_name = location_name;
        this.user_id = user_id;
        this.product_id = product_id;
        this.product_quantity = product_quantity;
        this.careated = careated;
        this.firebase_key = firebase_key;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public long getCareated() {
        return careated;
    }

    public void setCareated(long careated) {
        this.careated = careated;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getFirebase_key() {
        return firebase_key;
    }

    public void setFirebase_key(String firebase_key) {
        this.firebase_key = firebase_key;
    }
}
