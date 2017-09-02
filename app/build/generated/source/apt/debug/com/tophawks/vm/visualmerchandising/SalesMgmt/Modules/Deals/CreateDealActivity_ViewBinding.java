// Generated code from Butter Knife. Do not modify!
package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Deals;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.tophawks.vm.visualmerchandising.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CreateDealActivity_ViewBinding implements Unbinder {
  private CreateDealActivity target;

  @UiThread
  public CreateDealActivity_ViewBinding(CreateDealActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CreateDealActivity_ViewBinding(CreateDealActivity target, View source) {
    this.target = target;

    target.et1 = Utils.findRequiredViewAsType(source, R.id.editText, "field 'et1'", EditText.class);
    target.et2 = Utils.findRequiredViewAsType(source, R.id.editText2, "field 'et2'", EditText.class);
    target.et3 = Utils.findRequiredViewAsType(source, R.id.editText3, "field 'et3'", EditText.class);
    target.tv4 = Utils.findRequiredViewAsType(source, R.id.editText4, "field 'tv4'", TextView.class);
    target.tv5 = Utils.findRequiredViewAsType(source, R.id.editText5, "field 'tv5'", TextView.class);
    target.tv6 = Utils.findRequiredViewAsType(source, R.id.editText6, "field 'tv6'", TextView.class);
    target.tv7 = Utils.findRequiredViewAsType(source, R.id.editText7, "field 'tv7'", TextView.class);
    target.et8 = Utils.findRequiredViewAsType(source, R.id.editText8, "field 'et8'", EditText.class);
    target.et9 = Utils.findRequiredViewAsType(source, R.id.editText9, "field 'et9'", EditText.class);
    target.tv10 = Utils.findRequiredViewAsType(source, R.id.editText10, "field 'tv10'", TextView.class);
    target.tv11 = Utils.findRequiredViewAsType(source, R.id.editText11, "field 'tv11'", TextView.class);
    target.create = Utils.findRequiredViewAsType(source, R.id.create_deal, "field 'create'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CreateDealActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.et1 = null;
    target.et2 = null;
    target.et3 = null;
    target.tv4 = null;
    target.tv5 = null;
    target.tv6 = null;
    target.tv7 = null;
    target.et8 = null;
    target.et9 = null;
    target.tv10 = null;
    target.tv11 = null;
    target.create = null;
  }
}
