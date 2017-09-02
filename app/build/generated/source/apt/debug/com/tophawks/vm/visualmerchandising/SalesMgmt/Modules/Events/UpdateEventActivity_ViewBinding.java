// Generated code from Butter Knife. Do not modify!
package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Events;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.tophawks.vm.visualmerchandising.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UpdateEventActivity_ViewBinding implements Unbinder {
  private UpdateEventActivity target;

  @UiThread
  public UpdateEventActivity_ViewBinding(UpdateEventActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UpdateEventActivity_ViewBinding(UpdateEventActivity target, View source) {
    this.target = target;

    target.et1 = Utils.findRequiredViewAsType(source, R.id.editText, "field 'et1'", EditText.class);
    target.et2 = Utils.findRequiredViewAsType(source, R.id.editText2, "field 'et2'", EditText.class);
    target.tv3 = Utils.findRequiredViewAsType(source, R.id.editText3, "field 'tv3'", TextView.class);
    target.tv4 = Utils.findRequiredViewAsType(source, R.id.editText4, "field 'tv4'", TextView.class);
    target.tv5 = Utils.findRequiredViewAsType(source, R.id.editText5, "field 'tv5'", TextView.class);
    target.tv6 = Utils.findRequiredViewAsType(source, R.id.editText6, "field 'tv6'", TextView.class);
    target.tv7 = Utils.findRequiredViewAsType(source, R.id.editText7, "field 'tv7'", TextView.class);
    target.swt = Utils.findRequiredViewAsType(source, R.id.alldayee, "field 'swt'", Switch.class);
    target.update = Utils.findRequiredViewAsType(source, R.id.update_event, "field 'update'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UpdateEventActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.et1 = null;
    target.et2 = null;
    target.tv3 = null;
    target.tv4 = null;
    target.tv5 = null;
    target.tv6 = null;
    target.tv7 = null;
    target.swt = null;
    target.update = null;
  }
}
