package com.stockone.lite.model;

import java.io.Serializable;

public class TrialTimeModel implements Serializable {

    private long time, purchase_time, expire_time;
    private boolean isFirstLogin;
    private String user_id, purchase_token, mob_no;

    public TrialTimeModel() {
    }

    public TrialTimeModel(long time, boolean isFirstLogin, String user_id) {
        this.time = time;
        this.isFirstLogin = isFirstLogin;
        this.user_id = user_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPurchase_token() {
        return purchase_token;
    }

    public void setPurchase_token(String purchase_token) {
        this.purchase_token = purchase_token;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public long getPurchase_time() {
        return purchase_time;
    }

    public void setPurchase_time(long purchase_time) {
        this.purchase_time = purchase_time;
    }

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }
}
