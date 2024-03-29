// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductInventory_ViewBinding implements Unbinder {
  private ProductInventory target;

  @UiThread
  public ProductInventory_ViewBinding(ProductInventory target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProductInventory_ViewBinding(ProductInventory target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_product_inv, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_product_inventory, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductInventory target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
  }
}
