// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
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

public class Products_ViewBinding implements Unbinder {
  private Products target;

  private View view2131230781;

  @UiThread
  public Products_ViewBinding(Products target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Products_ViewBinding(final Products target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_products, "field 'toolbar'", Toolbar.class);
    target.pro_sku_id = Utils.findRequiredViewAsType(source, R.id.pro_sku_id, "field 'pro_sku_id'", EditText.class);
    target.text_products = Utils.findRequiredViewAsType(source, R.id.text_products, "field 'text_products'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_products, "field 'recyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btn_get_products, "method 'onClickProducts'");
    view2131230781 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickProducts();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Products target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.pro_sku_id = null;
    target.text_products = null;
    target.recyclerView = null;

    view2131230781.setOnClickListener(null);
    view2131230781 = null;
  }
}
