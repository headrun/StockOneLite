package com.stockone.lite.model;

import java.io.Serializable;

public class User implements Serializable {

   private String company_logo, company_name, email_address, phone_no, user_id;

    public User() {
    }

    public User(String company_logo, String company_name, String email_address, String phone_no, String user_id) {
        this.company_logo = company_logo;
        this.company_name = company_name;
        this.email_address = email_address;
        this.phone_no = phone_no;
        this.user_id = user_id;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
