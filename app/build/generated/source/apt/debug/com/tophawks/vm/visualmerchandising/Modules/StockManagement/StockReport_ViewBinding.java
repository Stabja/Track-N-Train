// Generated code from Butter Knife. Do not modify!
package com.tophawks.vm.visualmerchandising.Modules.StockManagement;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.tophawks.vm.visualmerchandising.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StockReport_ViewBinding implements Unbinder {
  private StockReport target;

  @UiThread
  public StockReport_ViewBinding(StockReport target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public StockReport_ViewBinding(StockReport target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StockReport target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
  }
}
