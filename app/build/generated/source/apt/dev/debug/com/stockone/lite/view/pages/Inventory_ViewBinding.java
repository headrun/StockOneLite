// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import com.stockone.lite.R;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Inventory_ViewBinding implements Unbinder {
  private Inventory target;

  private View view2131230846;

  private View view2131230782;

  @UiThread
  public Inventory_ViewBinding(Inventory target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Inventory_ViewBinding(final Inventory target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_inventory, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_inventory, "field 'recyclerView'", RecyclerView.class);
    target.txt_inventory = Utils.findRequiredViewAsType(source, R.id.txt_inventory, "field 'txt_inventory'", TextView.class);
    target.lottie = Utils.findRequiredViewAsType(source, R.id.lottie_inv, "field 'lottie'", LottieAnimationView.class);
    target.empty_storage = Utils.findRequiredViewAsType(source, R.id.empty_storage, "field 'empty_storage'", RelativeLayout.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi_inventory, "field 'avi'", AVLoadingIndicatorView.class);
    view = Utils.findRequiredView(source, R.id.dwnld_inventory_excel, "method 'ongetExcel'");
    view2131230846 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ongetExcel();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_in_sku, "method 'onEmptyInventory'");
    view2131230782 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onEmptyInventory();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Inventory target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
    target.txt_inventory = null;
    target.lottie = null;
    target.empty_storage = null;
    target.avi = null;

    view2131230846.setOnClickListener(null);
    view2131230846 = null;
    view2131230782.setOnClickListener(null);
    view2131230782 = null;
  }
}
