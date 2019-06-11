package com.stockone.lite.application;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        Utils.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
    }
}
