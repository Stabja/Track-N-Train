// Generated code from Butter Knife. Do not modify!
package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Leads;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.tophawks.vm.visualmerchandising.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CreateLeadActivity_ViewBinding implements Unbinder {
  private CreateLeadActivity target;

  @UiThread
  public CreateLeadActivity_ViewBinding(CreateLeadActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CreateLeadActivity_ViewBinding(CreateLeadActivity target, View source) {
    this.target = target;

    target.photo = Utils.findRequiredViewAsType(source, R.id.dis_image, "field 'photo'", ImageView.class);
    target.et1 = Utils.findRequiredViewAsType(source, R.id.editText, "field 'et1'", EditText.class);
    target.et2 = Utils.findRequiredViewAsType(source, R.id.editText2, "field 'et2'", EditText.class);
    target.et3 = Utils.findRequiredViewAsType(source, R.id.editText3, "field 'et3'", EditText.class);
    target.et4 = Utils.findRequiredViewAsType(source, R.id.editText4, "field 'et4'", EditText.class);
    target.et5 = Utils.findRequiredViewAsType(source, R.id.editText5, "field 'et5'", EditText.class);
    target.et6 = Utils.findRequiredViewAsType(source, R.id.editText6, "field 'et6'", EditText.class);
    target.tv7 = Utils.findRequiredViewAsType(source, R.id.editText7, "field 'tv7'", TextView.class);
    target.tv8 = Utils.findRequiredViewAsType(source, R.id.editText8, "field 'tv8'", TextView.class);
    target.tv9 = Utils.findRequiredViewAsType(source, R.id.editText9, "field 'tv9'", TextView.class);
    target.et10 = Utils.findRequiredViewAsType(source, R.id.editText10, "field 'et10'", EditText.class);
    target.et11 = Utils.findRequiredViewAsType(source, R.id.editText11, "field 'et11'", EditText.class);
    target.tv12 = Utils.findRequiredViewAsType(source, R.id.editText12, "field 'tv12'", TextView.class);
    target.create = Utils.findRequiredViewAsType(source, R.id.create_lead, "field 'create'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CreateLeadActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.photo = null;
    target.et1 = null;
    target.et2 = null;
    target.et3 = null;
    target.et4 = null;
    target.et5 = null;
    target.et6 = null;
    target.tv7 = null;
    target.tv8 = null;
    target.tv9 = null;
    target.et10 = null;
    target.et11 = null;
    target.tv12 = null;
    target.create = null;
  }
}
