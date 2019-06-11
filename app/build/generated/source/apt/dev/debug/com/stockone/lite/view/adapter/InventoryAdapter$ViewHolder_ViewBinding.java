// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class InventoryAdapter$ViewHolder_ViewBinding implements Unbinder {
  private InventoryAdapter.ViewHolder target;

  @UiThread
  public InventoryAdapter$ViewHolder_ViewBinding(InventoryAdapter.ViewHolder target, View source) {
    this.target = target;

    target.product_id = Utils.findRequiredViewAsType(source, R.id.product_id, "field 'product_id'", TextView.class);
    target.product_name = Utils.findRequiredViewAsType(source, R.id.product_name, "field 'product_name'", TextView.class);
    target.product_quantity = Utils.findRequiredViewAsType(source, R.id.product_quantity, "field 'product_quantity'", TextView.class);
    target.parent = Utils.findRequiredViewAsType(source, R.id.card_inventory, "field 'parent'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    InventoryAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.product_id = null;
    target.product_name = null;
    target.product_quantity = null;
    target.parent = null;
  }
}
