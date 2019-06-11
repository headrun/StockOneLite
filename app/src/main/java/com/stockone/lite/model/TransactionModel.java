package com.stockone.lite.model;

import java.io.Serializable;

public class TransactionModel implements Serializable {

    private String product_id, product_name, product_loc, user_id;
    private String product_quant;
    private long transaction_time;

    public TransactionModel() {
    }

    public TransactionModel(String product_id, String product_name, String product_loc, String user_id, String product_quant, long transaction_time) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_loc = product_loc;
        this.user_id = user_id;
        this.product_quant = product_quant;
        this.transaction_time = transaction_time;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_loc() {
        return product_loc;
    }

    public void setProduct_loc(String product_loc) {
        this.product_loc = product_loc;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(long transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getProduct_quant() {
        return product_quant;
    }

    public void setProduct_quant(String product_quant) {
        this.product_quant = product_quant;
    }
}
