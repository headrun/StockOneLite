package com.stockone.lite.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Products implements Serializable {

    private String SKUID, name, UID;
    private int minAmount, maxAmount, totalAmount;
    private long createdDate;
    private boolean inactive;
    private double price;
    private String location;

    public Products() {
    }

    public Products(String SKUID, String name, String UID, int minAmount, int maxAmount, int totalAmount, long createdDate, boolean inactive, double price, String location) {
        this.SKUID = SKUID;
        this.name = name;
        this.UID = UID;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.totalAmount = totalAmount;
        this.createdDate = createdDate;
        this.inactive = inactive;
        this.price = price;
        this.location = location;
    }

    public String getSKUID() {
        return SKUID;
    }

    public void setSKUID(String SKUID) {
        this.SKUID = SKUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
