// Generated code from Butter Knife. Do not modify!
package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Accounts;

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

public class CreateAccountActivity_ViewBinding implements Unbinder {
  private CreateAccountActivity target;

  @UiThread
  public CreateAccountActivity_ViewBinding(CreateAccountActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CreateAccountActivity_ViewBinding(CreateAccountActivity target, View source) {
    this.target = target;

    target.et1 = Utils.findRequiredViewAsType(source, R.id.editText, "field 'et1'", EditText.class);
    target.et2 = Utils.findRequiredViewAsType(source, R.id.editText2, "field 'et2'", EditText.class);
    target.et3 = Utils.findRequiredViewAsType(source, R.id.editText3, "field 'et3'", EditText.class);
    target.et4 = Utils.findRequiredViewAsType(source, R.id.editText4, "field 'et4'", EditText.class);
    target.et5 = Utils.findRequiredViewAsType(source, R.id.editText5, "field 'et5'", EditText.class);
    target.et6 = Utils.findRequiredViewAsType(source, R.id.editText6, "field 'et6'", EditText.class);
    target.et7 = Utils.findRequiredViewAsType(source, R.id.editText7, "field 'et7'", EditText.class);
    target.et8 = Utils.findRequiredViewAsType(source, R.id.editText8, "field 'et8'", TextView.class);
    target.et9 = Utils.findRequiredViewAsType(source, R.id.editText9, "field 'et9'", TextView.class);
    target.et11 = Utils.findRequiredViewAsType(source, R.id.editText10, "field 'et11'", EditText.class);
    target.et12 = Utils.findRequiredViewAsType(source, R.id.editText11, "field 'et12'", EditText.class);
    target.create = Utils.findRequiredViewAsType(source, R.id.create_account, "field 'create'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CreateAccountActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.et1 = null;
    target.et2 = null;
    target.et3 = null;
    target.et4 = null;
    target.et5 = null;
    target.et6 = null;
    target.et7 = null;
    target.et8 = null;
    target.et9 = null;
    target.et11 = null;
    target.et12 = null;
    target.create = null;
  }
}
