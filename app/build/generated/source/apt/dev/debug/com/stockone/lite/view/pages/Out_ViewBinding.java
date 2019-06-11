// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Out_ViewBinding implements Unbinder {
  private Out target;

  private View view2131230785;

  private View view2131230769;

  private View view2131230847;

  @UiThread
  public Out_ViewBinding(Out target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Out_ViewBinding(final Out target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_out, "field 'toolbar'", Toolbar.class);
    target.out_id = Utils.findRequiredViewAsType(source, R.id.out_sku_id, "field 'out_id'", EditText.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_out, "field 'recyclerView'", RecyclerView.class);
    target.text_locations = Utils.findRequiredViewAsType(source, R.id.text_locations, "field 'text_locations'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_out_sku, "field 'btn_out_sku' and method 'onOutButton'");
    target.btn_out_sku = Utils.castView(view, R.id.btn_out_sku, "field 'btn_out_sku'", CardView.class);
    view2131230785 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onOutButton();
      }
    });
    view = Utils.findRequiredView(source, R.id.barcode_out, "method 'onBarCodeOut'");
    view2131230769 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBarCodeOut();
      }
    });
    view = Utils.findRequiredView(source, R.id.dwnld_out_excel, "method 'downloadOutExcel'");
    view2131230847 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.downloadOutExcel();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Out target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.out_id = null;
    target.recyclerView = null;
    target.text_locations = null;
    target.btn_out_sku = null;

    view2131230785.setOnClickListener(null);
    view2131230785 = null;
    view2131230769.setOnClickListener(null);
    view2131230769 = null;
    view2131230847.setOnClickListener(null);
    view2131230847 = null;
  }
}
