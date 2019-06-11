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
import java.lang.IllegalStateException;
import java.lang.Override;

public class StorageInventory_ViewBinding implements Unbinder {
  private StorageInventory target;

  @UiThread
  public StorageInventory_ViewBinding(StorageInventory target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public StorageInventory_ViewBinding(StorageInventory target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_storage_inv, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_storage_inventory, "field 'recyclerView'", RecyclerView.class);
    target.empty_storage = Utils.findRequiredViewAsType(source, R.id.empty_storage, "field 'empty_storage'", RelativeLayout.class);
    target.lottie = Utils.findRequiredViewAsType(source, R.id.lottie_storage, "field 'lottie'", LottieAnimationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StorageInventory target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
    target.empty_storage = null;
    target.lottie = null;
  }
}
