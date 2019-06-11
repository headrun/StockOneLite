// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OutAdapter$ViewHolder_ViewBinding implements Unbinder {
  private OutAdapter.ViewHolder target;

  @UiThread
  public OutAdapter$ViewHolder_ViewBinding(OutAdapter.ViewHolder target, View source) {
    this.target = target;

    target.location_name = Utils.findRequiredViewAsType(source, R.id.out_storage_location, "field 'location_name'", TextView.class);
    target.location_quant = Utils.findRequiredViewAsType(source, R.id.out_storage_quant, "field 'location_quant'", TextView.class);
    target.edit_quantity = Utils.findRequiredViewAsType(source, R.id.out_storage_quantity, "field 'edit_quantity'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OutAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.location_name = null;
    target.location_quant = null;
    target.edit_quantity = null;
  }
}
