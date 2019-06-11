// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.auth;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Login_ViewBinding implements Unbinder {
  private Login target;

  private View view2131230787;

  private View view2131230778;

  private View view2131231163;

  @UiThread
  public Login_ViewBinding(Login target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Login_ViewBinding(final Login target, View source) {
    this.target = target;

    View view;
    target.background = Utils.findRequiredViewAsType(source, R.id.bg, "field 'background'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.btn_sign_in, "field 'btnSignIn' and method 'onSignClicked'");
    target.btnSignIn = Utils.castView(view, R.id.btn_sign_in, "field 'btnSignIn'", Button.class);
    view2131230787 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSignClicked();
      }
    });
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi_login, "field 'avi'", AVLoadingIndicatorView.class);
    target.otp_parent = Utils.findRequiredViewAsType(source, R.id.otp_rl, "field 'otp_parent'", RelativeLayout.class);
    target.number_parent = Utils.findRequiredViewAsType(source, R.id.phone_rl, "field 'number_parent'", RelativeLayout.class);
    target.phoneNo = Utils.findRequiredViewAsType(source, R.id.edt_phone_no, "field 'phoneNo'", EditText.class);
    target.otpNo = Utils.findRequiredViewAsType(source, R.id.edt_otp, "field 'otpNo'", EditText.class);
    target.countryCode = Utils.findRequiredViewAsType(source, R.id.edt_country_code, "field 'countryCode'", EditText.class);
    target.avi_otp = Utils.findRequiredViewAsType(source, R.id.avi_otp, "field 'avi_otp'", AVLoadingIndicatorView.class);
    view = Utils.findRequiredView(source, R.id.btn_confirm, "method 'onConfirmOTP'");
    view2131230778 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onConfirmOTP();
      }
    });
    view = Utils.findRequiredView(source, R.id.txt_resend_otp, "method 'onResendOTP'");
    view2131231163 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onResendOTP();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Login target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.background = null;
    target.btnSignIn = null;
    target.avi = null;
    target.otp_parent = null;
    target.number_parent = null;
    target.phoneNo = null;
    target.otpNo = null;
    target.countryCode = null;
    target.avi_otp = null;

    view2131230787.setOnClickListener(null);
    view2131230787 = null;
    view2131230778.setOnClickListener(null);
    view2131230778 = null;
    view2131231163.setOnClickListener(null);
    view2131231163 = null;
  }
}
