// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Settings_ViewBinding implements Unbinder {
  private Settings target;

  private View view2131230788;

  private View view2131230786;

  @UiThread
  public Settings_ViewBinding(Settings target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Settings_ViewBinding(final Settings target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_settings, "field 'toolbar'", Toolbar.class);
    target.logo = Utils.findRequiredViewAsType(source, R.id.company_logo, "field 'logo'", ImageView.class);
    target.company_name = Utils.findRequiredViewAsType(source, R.id.et_settings_name, "field 'company_name'", EditText.class);
    target.company_email = Utils.findRequiredViewAsType(source, R.id.et_settings_email, "field 'company_email'", EditText.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi_settings, "field 'avi'", AVLoadingIndicatorView.class);
    view = Utils.findRequiredView(source, R.id.btn_upload_logo, "method 'onLogoUpload'");
    view2131230788 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onLogoUpload();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_save_settings, "method 'onSaveSettings'");
    view2131230786 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSaveSettings();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Settings target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.logo = null;
    target.company_name = null;
    target.company_email = null;
    target.avi = null;

    view2131230788.setOnClickListener(null);
    view2131230788 = null;
    view2131230786.setOnClickListener(null);
    view2131230786 = null;
  }
}
