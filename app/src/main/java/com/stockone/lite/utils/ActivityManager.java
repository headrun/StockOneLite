package com.stockone.lite.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.blankj.utilcode.util.ToastUtils;
import com.stockone.lite.view.pages.Count;
import com.stockone.lite.view.pages.EditProducts;
import com.stockone.lite.view.pages.Home;
import com.stockone.lite.view.pages.In;
import com.stockone.lite.view.auth.Login;
import com.stockone.lite.view.pages.Information;
import com.stockone.lite.view.pages.Inventory;
import com.stockone.lite.view.pages.Locations;
import com.stockone.lite.view.pages.Move;
import com.stockone.lite.view.pages.Out;
import com.stockone.lite.view.pages.ProductInventory;
import com.stockone.lite.view.pages.Products;
import com.stockone.lite.view.pages.StorageInventory;
import com.stockone.lite.view.pages.Subscription;
import com.stockone.lite.view.pages.Transactions;

import java.util.ArrayList;

public class ActivityManager {

    public static void LOGIN(Context context){

        context.startActivity(new Intent(context, Login.class));
    }

    public static void HOME(Context context){

        context.startActivity(new Intent(context, Home.class));
    }

    public static void IN(Context context){

        context.startActivity(new Intent(context, In.class));
    }

    public static void INVENTORY(Context context){

        context.startActivity(new Intent(context, Inventory.class));
    }

    public static void LOCATIONS(Context context){

        context.startActivity(new Intent(context, Locations.class));
    }

    public static void STORAGE_INVENTORY(Context context, String loc_name){

        Intent intent = new Intent(context, StorageInventory.class);
        intent.putExtra("loc_name", loc_name);
        context.startActivity(intent);
    }

    public static void OUT(Context context){

        context.startActivity(new Intent(context, Out.class));
    }

    public static void PRODUCT_INVENTORY(Context context, String pro_id){

        Intent intent = new Intent(context, ProductInventory.class);
        intent.putExtra("pro_id", pro_id);
        context.startActivity(intent);
    }

    public static void MOVE(Context context){

        context.startActivity(new Intent(context, Move.class));
    }

    public static void INFORMATION(Context context){

        context.startActivity(new Intent(context, Information.class));
    }

    public static void PRODUCTS(Context context){

        context.startActivity(new Intent(context, Products.class));
    }

    public static void EDIT_PRODUCTS(Context context, String pro_id, long created, String name, int min_amt, int max_amt, double price, boolean inactive, int totalAmount){

        Intent intent = new Intent(context, EditProducts.class);
        intent.putExtra("pro_id", pro_id);
        intent.putExtra("created", created);
        intent.putExtra("name", name);
        intent.putExtra("min_amt", min_amt);
        intent.putExtra("max_amt", max_amt);
        intent.putExtra("price", price);
        intent.putExtra("inactive", inactive);
        intent.putExtra("totalAmount", totalAmount);
        context.startActivity(intent);
    }

    public static void TRANSCATIONS(Context context){

        context.startActivity(new Intent(context, Transactions.class));
    }

    public static void COUNT(Context context){

        context.startActivity(new Intent(context, Count.class));
    }

    public static void SUBSCRIPTION(Context context){

        context.startActivity(new Intent(context, Subscription.class));
    }

    public static void PERMISSION_TAB(Context context) {
        ToastUtils.showLong("Please Tap on Permission and allow us required permission.");
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void SETTINGS(Context context){

        context.startActivity(new Intent(context, com.stockone.lite.view.pages.Settings.class));
    }
}
