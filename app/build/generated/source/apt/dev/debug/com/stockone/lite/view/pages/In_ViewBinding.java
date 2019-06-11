// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class In_ViewBinding implements Unbinder {
  private In target;

  private View view2131230783;

  private View view2131230767;

  private View view2131230845;

  @UiThread
  public In_ViewBinding(In target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public In_ViewBinding(final In target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_in, "field 'toolbar'", Toolbar.class);
    target.sku_id = Utils.findRequiredViewAsType(source, R.id.in_sku_id, "field 'sku_id'", EditText.class);
    target.sku_amt = Utils.findRequiredViewAsType(source, R.id.in_sku_amt, "field 'sku_amt'", EditText.class);
    target.sku_location = Utils.findRequiredViewAsType(source, R.id.in_sku_location, "field 'sku_location'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_in_sku_add, "field 'add' and method 'onSKUadded'");
    target.add = Utils.castView(view, R.id.btn_in_sku_add, "field 'add'", CardView.class);
    view2131230783 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSKUadded();
      }
    });
    view = Utils.findRequiredView(source, R.id.barcode_in, "field 'barcode_in' and method 'onBarcodeScannedIn'");
    target.barcode_in = Utils.castView(view, R.id.barcode_in, "field 'barcode_in'", ImageView.class);
    view2131230767 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBarcodeScannedIn();
      }
    });
    view = Utils.findRequiredView(source, R.id.dwnld_in_excel, "method 'onInExcelDownload'");
    view2131230845 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onInExcelDownload();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    In target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.sku_id = null;
    target.sku_amt = null;
    target.sku_location = null;
    target.add = null;
    target.barcode_in = null;

    view2131230783.setOnClickListener(null);
    view2131230783 = null;
    view2131230767.setOnClickListener(null);
    view2131230767 = null;
    view2131230845.setOnClickListener(null);
    view2131230845 = null;
  }
}
