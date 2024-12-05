package com.k.deeplinkingtesting.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.k.deeplinkingtesting.R;


public class AutoMeasureGridView extends GridView {

    public AutoMeasureGridView(Context context) {
        super(context);
    }

    public AutoMeasureGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoMeasureGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    static boolean didInit = false;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        GridViewMeasurableItemsAdapter adapter = (GridViewMeasurableItemsAdapter)getAdapter();
        if(adapter != null && changed) {
            int numColumns = 2;//getContext().getResources().getInteger(R.integer.layouts_demo_columns_number);
            GridViewItemLayout.initItemLayout(numColumns, adapter.getCount());

            if(!didInit) {
                int columnWidth = getMeasuredWidth() / numColumns;
                adapter.measureItems(columnWidth);
                didInit = true;
            }
        }
        super.onLayout(changed, l, t, r, b);
    }
}

