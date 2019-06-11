// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Information_ViewBinding implements Unbinder {
  private Information target;

  private View view2131230780;

  @UiThread
  public Information_ViewBinding(Information target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Information_ViewBinding(final Information target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_information, "field 'toolbar'", Toolbar.class);
    target.info_sku_id = Utils.findRequiredViewAsType(source, R.id.info_sku_id, "field 'info_sku_id'", EditText.class);
    target.info_total_quantity = Utils.findRequiredViewAsType(source, R.id.info_total_quantity, "field 'info_total_quantity'", TextView.class);
    target.total_quant_ll = Utils.findRequiredViewAsType(source, R.id.total_quant_ll, "field 'total_quant_ll'", LinearLayout.class);
    target.location_text = Utils.findRequiredViewAsType(source, R.id.location_text, "field 'location_text'", TextView.class);
    target.transaction_text = Utils.findRequiredViewAsType(source, R.id.transaction_text, "field 'transaction_text'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_info, "field 'recyclerView'", RecyclerView.class);
    target.rv_info_transaction = Utils.findRequiredViewAsType(source, R.id.rv_info_transaction, "field 'rv_info_transaction'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btn_get_info, "method 'onInfoBtnClicked'");
    view2131230780 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onInfoBtnClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Information target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.info_sku_id = null;
    target.info_total_quantity = null;
    target.total_quant_ll = null;
    target.location_text = null;
    target.transaction_text = null;
    target.recyclerView = null;
    target.rv_info_transaction = null;

    view2131230780.setOnClickListener(null);
    view2131230780 = null;
  }
}
