// Generated code from Butter Knife. Do not modify!
package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Feeds;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.tophawks.vm.visualmerchandising.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReadFeedActivity_ViewBinding implements Unbinder {
  private ReadFeedActivity target;

  @UiThread
  public ReadFeedActivity_ViewBinding(ReadFeedActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReadFeedActivity_ViewBinding(ReadFeedActivity target, View source) {
    this.target = target;

    target.scrollView = Utils.findRequiredViewAsType(source, R.id.parent_scrollview, "field 'scrollView'", ScrollView.class);
    target.authorIcon = Utils.findRequiredViewAsType(source, R.id.authorimage, "field 'authorIcon'", CircleImageView.class);
    target.authorName = Utils.findRequiredViewAsType(source, R.id.author, "field 'authorName'", TextView.class);
    target.postTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'postTitle'", TextView.class);
    target.postBody = Utils.findRequiredViewAsType(source, R.id.body, "field 'postBody'", TextView.class);
    target.timeStamp = Utils.findRequiredViewAsType(source, R.id.timestamp, "field 'timeStamp'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.comments_recyclerview, "field 'recyclerView'", RecyclerView.class);
    target.commentText = Utils.findRequiredViewAsType(source, R.id.comment_text, "field 'commentText'", EditText.class);
    target.publishComment = Utils.findRequiredViewAsType(source, R.id.button_publish_comment, "field 'publishComment'", Button.class);
    target.progressLayout = Utils.findRequiredViewAsType(source, R.id.loading_layout, "field 'progressLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReadFeedActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.scrollView = null;
    target.authorIcon = null;
    target.authorName = null;
    target.postTitle = null;
    target.postBody = null;
    target.timeStamp = null;
    target.recyclerView = null;
    target.commentText = null;
    target.publishComment = null;
    target.progressLayout = null;
  }
}
