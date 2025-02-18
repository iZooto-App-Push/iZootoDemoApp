// Generated by view binder compiler. Do not edit!
package com.k.deeplinkingtesting.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.k.deeplinkingtesting.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ArticleTextRowBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView articleTextView;

  private ArticleTextRowBinding(@NonNull LinearLayout rootView, @NonNull TextView articleTextView) {
    this.rootView = rootView;
    this.articleTextView = articleTextView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ArticleTextRowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ArticleTextRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.article_text_row, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ArticleTextRowBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.article_text_view;
      TextView articleTextView = ViewBindings.findChildViewById(rootView, id);
      if (articleTextView == null) {
        break missingId;
      }

      return new ArticleTextRowBinding((LinearLayout) rootView, articleTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
