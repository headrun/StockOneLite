package com.stockone.lite.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stockone.lite.R;
import com.stockone.lite.model.Products;
import com.stockone.lite.model.Zone;
import com.stockone.lite.utils.Constants;

public class AddLocation {

    private Context context;
    private Dialog dialog;
    private EditText et_add_location;
    private CardView card_add_loc;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    public AddLocation(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_location);
        dialog.setCancelable(false);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.Zone_Path);

        et_add_location = (EditText) dialog.findViewById(R.id.et_add_location);
        card_add_loc = (CardView) dialog.findViewById(R.id.card_add_loc);

        dialog.setCanceledOnTouchOutside(true);

        if(dialog.getWindow() != null) {
//            dialog.getWindow().getAttributes().windowAnimations = R.style.Slide_Up_Down;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        card_add_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location = et_add_location.getText().toString().trim();

                if (TextUtils.isEmpty(location)) {
                    ToastUtils.showLong("Please enter location name");
                    return;
                }

                Zone model = new Zone();

                model.setLocation(location);
                model.setUserid(mAuth.getUid());


                // Getting the ID from firebase database.
                String IDFromServer = databaseReference.push().getKey();

                // Adding the both name and number values using student details class object using ID.
                if (IDFromServer != null) {
                    databaseReference.child(IDFromServer).setValue(model);
                }

                et_add_location.setText("");
                dialog.hide();

                ToastUtils.showLong("Added");
            }
        });

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
