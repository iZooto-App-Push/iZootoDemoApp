// Generated by view binder compiler. Do not edit!
package com.k.deeplinkingtesting.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.k.deeplinkingtesting.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ToolbarActionBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final RelativeLayout ActionBarWrapper;

  @NonNull
  public final Toolbar toolbarActionbar;

  private ToolbarActionBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull RelativeLayout ActionBarWrapper, @NonNull Toolbar toolbarActionbar) {
    this.rootView = rootView;
    this.ActionBarWrapper = ActionBarWrapper;
    this.toolbarActionbar = toolbarActionbar;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ToolbarActionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ToolbarActionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.toolbar_action, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ToolbarActionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ActionBarWrapper;
      RelativeLayout ActionBarWrapper = ViewBindings.findChildViewById(rootView, id);
      if (ActionBarWrapper == null) {
        break missingId;
      }

      id = R.id.toolbar_actionbar;
      Toolbar toolbarActionbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbarActionbar == null) {
        break missingId;
      }

      return new ToolbarActionBinding((LinearLayoutCompat) rootView, ActionBarWrapper,
          toolbarActionbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
