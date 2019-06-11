// Generated code from Butter Knife. Do not modify!
package com.stockone.lite.view.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stockone.lite.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocationAdapter$ViewHolder_ViewBinding implements Unbinder {
  private LocationAdapter.ViewHolder target;

  @UiThread
  public LocationAdapter$ViewHolder_ViewBinding(LocationAdapter.ViewHolder target, View source) {
    this.target = target;

    target.location_name = Utils.findRequiredViewAsType(source, R.id.location_name, "field 'location_name'", TextView.class);
    target.parent = Utils.findRequiredViewAsType(source, R.id.card_location, "field 'parent'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.location_name = null;
    target.parent = null;
  }
}
