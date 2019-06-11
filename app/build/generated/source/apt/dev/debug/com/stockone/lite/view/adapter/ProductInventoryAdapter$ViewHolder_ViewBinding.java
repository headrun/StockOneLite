// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductInventoryAdapter$ViewHolder_ViewBinding implements Unbinder {
  private ProductInventoryAdapter.ViewHolder target;

  @UiThread
  public ProductInventoryAdapter$ViewHolder_ViewBinding(ProductInventoryAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.product_name = Utils.findRequiredViewAsType(source, R.id.inventory_product_name, "field 'product_name'", TextView.class);
    target.product_count = Utils.findRequiredViewAsType(source, R.id.inventory_product_count, "field 'product_count'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductInventoryAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.product_name = null;
    target.product_count = null;
  }
}
