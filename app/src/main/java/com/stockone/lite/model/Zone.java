package com.stockone.lite.model;

import java.io.Serializable;

public class Zone implements Serializable {

    private String location, userid;

    public Zone() {
    }

    public Zone(String location, String userid) {
        this.location = location;
        this.userid = userid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
