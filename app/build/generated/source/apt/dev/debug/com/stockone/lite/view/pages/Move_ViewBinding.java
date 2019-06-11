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

public class Move_ViewBinding implements Unbinder {
  private Move target;

  private View view2131230768;

  private View view2131230784;

  @UiThread
  public Move_ViewBinding(Move target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Move_ViewBinding(final Move target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_move, "field 'toolbar'", Toolbar.class);
    target.move_sku_id = Utils.findRequiredViewAsType(source, R.id.move_sku_id, "field 'move_sku_id'", EditText.class);
    target.move_sku_quant = Utils.findRequiredViewAsType(source, R.id.move_sku_quant, "field 'move_sku_quant'", EditText.class);
    target.move_source_loc = Utils.findRequiredViewAsType(source, R.id.move_source_loc, "field 'move_source_loc'", EditText.class);
    target.move_destin_loc = Utils.findRequiredViewAsType(source, R.id.move_destin_loc, "field 'move_destin_loc'", EditText.class);
    view = Utils.findRequiredView(source, R.id.barcode_move, "method 'onBarcodeMove'");
    view2131230768 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBarcodeMove();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_move_sku, "method 'onBtnMove'");
    view2131230784 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBtnMove();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Move target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.move_sku_id = null;
    target.move_sku_quant = null;
    target.move_source_loc = null;
    target.move_destin_loc = null;

    view2131230768.setOnClickListener(null);
    view2131230768 = null;
    view2131230784.setOnClickListener(null);
    view2131230784 = null;
  }
}
