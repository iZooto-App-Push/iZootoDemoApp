package com.k.deeplinkingtesting.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Arrays;

/**
 * This class makes sure that all items in a GridView row are of the same height.
 * (Could extend FrameLayout, LinearLayout etc as well, RelativeLayout was just my choice here)
 * @author Anton Spaans
 *
 */
public class GridViewItemContainer extends RelativeLayout {
    private View[] viewsInRow;

    public GridViewItemContainer(Context context) {
        super(context);
    }

    public GridViewItemContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GridViewItemContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewsInRow(View[] viewsInRow) {
        if  (viewsInRow != null) {
            if (this.viewsInRow == null) {
                this.viewsInRow = new View[viewsInRow.length];
                for (int i = 0; i < viewsInRow.length; i++) {
                    this.viewsInRow[i] = viewsInRow[i];
                }
            }
            else {
                System.arraycopy(viewsInRow, 0, this.viewsInRow, 0, viewsInRow.length);
            }
        }
        else if (this.viewsInRow != null){
            Arrays.fill(this.viewsInRow, null);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (viewsInRow == null) {
            return;
        }

        int measuredHeight = getMeasuredHeight();
        int maxHeight      = measuredHeight;
        for (View siblingInRow : viewsInRow) {
            if  (siblingInRow != null) {
                maxHeight = Math.max(maxHeight, siblingInRow.getMeasuredHeight());
            }
        }

        if (maxHeight == measuredHeight) {
            return;
        }

        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        switch(heightMode) {
            case View.MeasureSpec.AT_MOST:
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.min(maxHeight, heightSize), View.MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                break;

            case View.MeasureSpec.EXACTLY:
                // No debate here. Final measuring already took place. That's it.
                break;

            case View.MeasureSpec.UNSPECIFIED:
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                break;

        }
    }
}