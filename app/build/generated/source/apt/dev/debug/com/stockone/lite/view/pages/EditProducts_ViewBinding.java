// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import com.zcw.togglebutton.ToggleButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditProducts_ViewBinding implements Unbinder {
  private EditProducts target;

  private View view2131230779;

  @UiThread
  public EditProducts_ViewBinding(EditProducts target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditProducts_ViewBinding(final EditProducts target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_edit_products, "field 'toolbar'", Toolbar.class);
    target.text_edit_id = Utils.findRequiredViewAsType(source, R.id.text_edit_id, "field 'text_edit_id'", TextView.class);
    target.text_edit_created = Utils.findRequiredViewAsType(source, R.id.text_edit_created, "field 'text_edit_created'", TextView.class);
    target.et_edit_name = Utils.findRequiredViewAsType(source, R.id.et_edit_name, "field 'et_edit_name'", EditText.class);
    target.et_edit_min_amt = Utils.findRequiredViewAsType(source, R.id.et_edit_min_amt, "field 'et_edit_min_amt'", EditText.class);
    target.et_edit_max_amt = Utils.findRequiredViewAsType(source, R.id.et_edit_max_amt, "field 'et_edit_max_amt'", EditText.class);
    target.et_edit_price = Utils.findRequiredViewAsType(source, R.id.et_edit_price, "field 'et_edit_price'", EditText.class);
    target.toggle_rl = Utils.findRequiredViewAsType(source, R.id.toggle_rl, "field 'toggle_rl'", RelativeLayout.class);
    target.toggleButton = Utils.findRequiredViewAsType(source, R.id.inactive_switch, "field 'toggleButton'", ToggleButton.class);
    view = Utils.findRequiredView(source, R.id.btn_edit_products, "method 'onEditProductsClicked'");
    view2131230779 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onEditProductsClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    EditProducts target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.text_edit_id = null;
    target.text_edit_created = null;
    target.et_edit_name = null;
    target.et_edit_min_amt = null;
    target.et_edit_max_amt = null;
    target.et_edit_price = null;
    target.toggle_rl = null;
    target.toggleButton = null;

    view2131230779.setOnClickListener(null);
    view2131230779 = null;
  }
}
