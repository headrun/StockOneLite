// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Subscription_ViewBinding implements Unbinder {
  private Subscription target;

  private View view2131230948;

  private View view2131231181;

  @UiThread
  public Subscription_ViewBinding(Subscription target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Subscription_ViewBinding(final Subscription target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_subscription, "field 'toolbar'", Toolbar.class);
    target.subscription_text_parent = Utils.findRequiredViewAsType(source, R.id.upper_ll, "field 'subscription_text_parent'", LinearLayout.class);
    target.monthly_rl = Utils.findRequiredViewAsType(source, R.id.monthly_rl, "field 'monthly_rl'", RelativeLayout.class);
    target.yearly_rl = Utils.findRequiredViewAsType(source, R.id.yearly_rl, "field 'yearly_rl'", RelativeLayout.class);
    target.trial_date = Utils.findRequiredViewAsType(source, R.id.trial_date, "field 'trial_date'", TextView.class);
    target.subscription_status = Utils.findRequiredViewAsType(source, R.id.subscription_status, "field 'subscription_status'", TextView.class);
    view = Utils.findRequiredView(source, R.id.monthly_card, "method 'onMonthlyButtonCliked'");
    view2131230948 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onMonthlyButtonCliked();
      }
    });
    view = Utils.findRequiredView(source, R.id.yearly_card, "method 'onYearlyButtonCliked'");
    view2131231181 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onYearlyButtonCliked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Subscription target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.subscription_text_parent = null;
    target.monthly_rl = null;
    target.yearly_rl = null;
    target.trial_date = null;
    target.subscription_status = null;

    view2131230948.setOnClickListener(null);
    view2131230948 = null;
    view2131231181.setOnClickListener(null);
    view2131231181 = null;
  }
}
