// Generated code from Butter Knife. Do not modify!
package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Feeds;

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

public class CreateFeedActivity_ViewBinding implements Unbinder {
  private CreateFeedActivity target;

  @UiThread
  public CreateFeedActivity_ViewBinding(CreateFeedActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CreateFeedActivity_ViewBinding(CreateFeedActivity target, View source) {
    this.target = target;

    target.selectAccount = Utils.findRequiredViewAsType(source, R.id.editText, "field 'selectAccount'", TextView.class);
    target.titleText = Utils.findRequiredViewAsType(source, R.id.editText2, "field 'titleText'", EditText.class);
    target.statusText = Utils.findRequiredViewAsType(source, R.id.editText3, "field 'statusText'", EditText.class);
    target.create = Utils.findRequiredViewAsType(source, R.id.post_status, "field 'create'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CreateFeedActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.selectAccount = null;
    target.titleText = null;
    target.statusText = null;
    target.create = null;
  }
}
