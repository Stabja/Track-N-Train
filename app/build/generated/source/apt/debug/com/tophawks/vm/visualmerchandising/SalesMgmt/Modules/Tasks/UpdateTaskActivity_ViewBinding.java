// Generated code from Butter Knife. Do not modify!
package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Tasks;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.tophawks.vm.visualmerchandising.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UpdateTaskActivity_ViewBinding implements Unbinder {
  private UpdateTaskActivity target;

  @UiThread
  public UpdateTaskActivity_ViewBinding(UpdateTaskActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UpdateTaskActivity_ViewBinding(UpdateTaskActivity target, View source) {
    this.target = target;

    target.tv1 = Utils.findRequiredViewAsType(source, R.id.editText, "field 'tv1'", TextView.class);
    target.tv2 = Utils.findRequiredViewAsType(source, R.id.editText2, "field 'tv2'", TextView.class);
    target.tv3 = Utils.findRequiredViewAsType(source, R.id.editText3, "field 'tv3'", TextView.class);
    target.tv4 = Utils.findRequiredViewAsType(source, R.id.editText4, "field 'tv4'", TextView.class);
    target.tv5 = Utils.findRequiredViewAsType(source, R.id.editText5, "field 'tv5'", TextView.class);
    target.tv6 = Utils.findRequiredViewAsType(source, R.id.editText6, "field 'tv6'", TextView.class);
    target.tv7 = Utils.findRequiredViewAsType(source, R.id.editText7, "field 'tv7'", TextView.class);
    target.tv8 = Utils.findRequiredViewAsType(source, R.id.editText8, "field 'tv8'", TextView.class);
    target.update = Utils.findRequiredViewAsType(source, R.id.update_task, "field 'update'", Button.class);
    target.swt = Utils.findRequiredViewAsType(source, R.id.notification_trigger, "field 'swt'", Switch.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UpdateTaskActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv1 = null;
    target.tv2 = null;
    target.tv3 = null;
    target.tv4 = null;
    target.tv5 = null;
    target.tv6 = null;
    target.tv7 = null;
    target.tv8 = null;
    target.update = null;
    target.swt = null;
  }
}
