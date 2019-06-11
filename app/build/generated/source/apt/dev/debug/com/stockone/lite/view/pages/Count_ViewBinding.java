// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Count_ViewBinding implements Unbinder {
  private Count target;

  private View view2131230777;

  @UiThread
  public Count_ViewBinding(Count target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Count_ViewBinding(final Count target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_count, "field 'toolbar'", Toolbar.class);
    target.count_location_name = Utils.findRequiredViewAsType(source, R.id.count_location_name, "field 'count_location_name'", EditText.class);
    target.count_product_name = Utils.findRequiredViewAsType(source, R.id.count_product_name, "field 'count_product_name'", EditText.class);
    target.count_sku_quant = Utils.findRequiredViewAsType(source, R.id.count_sku_quant, "field 'count_sku_quant'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_change_count, "method 'onChangeCountClicked'");
    view2131230777 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onChangeCountClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Count target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.count_location_name = null;
    target.count_product_name = null;
    target.count_sku_quant = null;

    view2131230777.setOnClickListener(null);
    view2131230777 = null;
  }
}
