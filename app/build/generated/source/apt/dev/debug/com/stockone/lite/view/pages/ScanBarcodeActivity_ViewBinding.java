// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ScanBarcodeActivity_ViewBinding implements Unbinder {
  private ScanBarcodeActivity target;

  @UiThread
  public ScanBarcodeActivity_ViewBinding(ScanBarcodeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ScanBarcodeActivity_ViewBinding(ScanBarcodeActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_scan, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ScanBarcodeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
  }
}
