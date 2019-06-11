// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import com.stockone.lite.R;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Transactions_ViewBinding implements Unbinder {
  private Transactions target;

  @UiThread
  public Transactions_ViewBinding(Transactions target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Transactions_ViewBinding(Transactions target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_transactions, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_transactions, "field 'recyclerView'", RecyclerView.class);
    target.empty_storage = Utils.findRequiredViewAsType(source, R.id.empty_storage, "field 'empty_storage'", RelativeLayout.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi_transactions, "field 'avi'", AVLoadingIndicatorView.class);
    target.lottie = Utils.findRequiredViewAsType(source, R.id.lottie_storage, "field 'lottie'", LottieAnimationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Transactions target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
    target.empty_storage = null;
    target.avi = null;
    target.lottie = null;
  }
}
