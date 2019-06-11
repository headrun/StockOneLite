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

public class StorageInventoryAdapter$ViewHolder_ViewBinding implements Unbinder {
  private StorageInventoryAdapter.ViewHolder target;

  @UiThread
  public StorageInventoryAdapter$ViewHolder_ViewBinding(StorageInventoryAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.product_id = Utils.findRequiredViewAsType(source, R.id.product_id_storage, "field 'product_id'", TextView.class);
    target.product_name = Utils.findRequiredViewAsType(source, R.id.product_name_storage, "field 'product_name'", TextView.class);
    target.product_quantity = Utils.findRequiredViewAsType(source, R.id.product_quantity_storage, "field 'product_quantity'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StorageInventoryAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.product_id = null;
    target.product_name = null;
    target.product_quantity = null;
  }
}
