package com.stockone.lite.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.stockone.lite.R;

public class UpdateDialog {

    private Context context;
    private Dialog dialog;
    private CardView updateBtn;
    private LottieAnimationView lottie;

    public UpdateDialog(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_force_update);
        dialog.setCancelable(false);

        updateBtn = (CardView) dialog.findViewById(R.id.card_force_update);
        lottie = (LottieAnimationView) dialog.findViewById(R.id.update_image);

        lottie.setAnimation("new_notification_bell.json");
        lottie.loop(true);
        lottie.playAnimation();

        // onClick on Update Button
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/details";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
                context.startActivity(intent);
            }
        });

        dialog.setCanceledOnTouchOutside(false);

        if(dialog.getWindow() != null) {
//            dialog.getWindow().getAttributes().windowAnimations = R.style.Slide_Up_Down;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    public void show(){
        try {
            dialog.show();
        } catch (Exception e){

        }
    }

    public void close(){
        dialog.hide();
    }
}
