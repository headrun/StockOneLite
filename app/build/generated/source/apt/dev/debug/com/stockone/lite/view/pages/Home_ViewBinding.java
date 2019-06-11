// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Home_ViewBinding implements Unbinder {
  private Home target;

  private View view2131230800;

  private View view2131230802;

  private View view2131230803;

  private View view2131230805;

  private View view2131230804;

  private View view2131230801;

  private View view2131230906;

  @UiThread
  public Home_ViewBinding(Home target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Home_ViewBinding(final Home target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.card_view_in, "method 'onInClicked'");
    view2131230800 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onInClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.card_view_inventory, "method 'onInventoryClicked'");
    view2131230802 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onInventoryClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.card_view_location, "method 'onLocationCliked'");
    view2131230803 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onLocationCliked();
      }
    });
    view = Utils.findRequiredView(source, R.id.card_view_out, "method 'onOutCliked'");
    view2131230805 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onOutCliked();
      }
    });
    view = Utils.findRequiredView(source, R.id.card_view_move, "method 'onMoveClicked'");
    view2131230804 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onMoveClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.card_view_info, "method 'onInformationCliked'");
    view2131230801 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onInformationCliked();
      }
    });
    view = Utils.findRequiredView(source, R.id.img_subscription, "method 'onSubscriptionClicked'");
    view2131230906 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSubscriptionClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view2131230800.setOnClickListener(null);
    view2131230800 = null;
    view2131230802.setOnClickListener(null);
    view2131230802 = null;
    view2131230803.setOnClickListener(null);
    view2131230803 = null;
    view2131230805.setOnClickListener(null);
    view2131230805 = null;
    view2131230804.setOnClickListener(null);
    view2131230804 = null;
    view2131230801.setOnClickListener(null);
    view2131230801 = null;
    view2131230906.setOnClickListener(null);
    view2131230906 = null;
  }
}
