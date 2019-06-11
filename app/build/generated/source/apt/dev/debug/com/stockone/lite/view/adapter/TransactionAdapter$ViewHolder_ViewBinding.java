// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TransactionAdapter$ViewHolder_ViewBinding implements Unbinder {
  private TransactionAdapter.ViewHolder target;

  @UiThread
  public TransactionAdapter$ViewHolder_ViewBinding(TransactionAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.transcation_pro_id = Utils.findRequiredViewAsType(source, R.id.transcation_pro_id, "field 'transcation_pro_id'", TextView.class);
    target.transcation_pro_name = Utils.findRequiredViewAsType(source, R.id.transcation_pro_name, "field 'transcation_pro_name'", TextView.class);
    target.transcation_pro_quant = Utils.findRequiredViewAsType(source, R.id.transcation_pro_quant, "field 'transcation_pro_quant'", TextView.class);
    target.transcation_pro_loc = Utils.findRequiredViewAsType(source, R.id.transcation_pro_loc, "field 'transcation_pro_loc'", TextView.class);
    target.transcation_pro_time = Utils.findRequiredViewAsType(source, R.id.transcation_pro_time, "field 'transcation_pro_time'", TextView.class);
    target.img_transaction = Utils.findRequiredViewAsType(source, R.id.img_transaction, "field 'img_transaction'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TransactionAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.transcation_pro_id = null;
    target.transcation_pro_name = null;
    target.transcation_pro_quant = null;
    target.transcation_pro_loc = null;
    target.transcation_pro_time = null;
    target.img_transaction = null;
  }
}
