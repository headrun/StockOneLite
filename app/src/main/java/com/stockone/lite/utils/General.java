package com.stockone.lite.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.stockone.lite.R;
import com.stockone.lite.view.pages.Inventory;
import com.tapadoo.alerter.Alerter;

import java.io.File;

import io.paperdb.Paper;

public class General {

    public static void alertDialog(Activity activity, String title, String message){
        Alerter.create(activity)
                .setTitle(title)
                .setText(message)
                .setIcon(R.drawable.ic_error)
                .setBackgroundColorRes(R.color.red)
                .setDuration(3000)
                .show();
    }

    public static boolean validatePhoneNumber(Activity activity, EditText mPhoneNumberField) {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length()>10 || phoneNumber.length()<9) {
            General.alertDialog(activity, null, "Invalid phone number");
            return false;
        }

        return true;
    }

    public static void hideKeyboard(Context context, EditText editText){

        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(editText.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void storeUserNumber(String userName){
        Paper.book().write("userNumber",userName);
    }

    public static String getUserNumber(){
        return Paper.book().read("userNumber");
    }


    public static void showNotification(Context context, Uri ImageUri, String nameFILE){

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/StockOneLite/"+ nameFILE)), "application/vnd.ms-excel");
        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, target, PendingIntent.FLAG_ONE_SHOT);


        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setSmallIcon(R.drawable.ic_in);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentTitle(ImageUri.getPath());
        notificationBuilder.setContentText("StockOne Lite excel");

        nm.notify(9, notificationBuilder.build());
    }

    public static void rateApp(Context context) {
        try {
            Intent rateIntent = rateIntentForUrl("market://details", context);
            context.startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details", context);
            context.startActivity(rateIntent);
        }
    }

    private static Intent rateIntentForUrl(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}
