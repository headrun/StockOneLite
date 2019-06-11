// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.pages;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Locations_ViewBinding implements Unbinder {
  private Locations target;

  private View view2131230749;

  @UiThread
  public Locations_ViewBinding(Locations target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Locations_ViewBinding(final Locations target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_location, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_locations, "field 'recyclerView'", RecyclerView.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi_locations, "field 'avi'", AVLoadingIndicatorView.class);
    view = Utils.findRequiredView(source, R.id.add_location, "method 'addLocation'");
    view2131230749 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addLocation();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Locations target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
    target.avi = null;

    view2131230749.setOnClickListener(null);
    view2131230749 = null;
  }
}
