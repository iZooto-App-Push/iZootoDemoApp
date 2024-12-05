package com.k.deeplinkingtesting.insteam;

import android.content.Context;

import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.k.deeplinkingtesting.OBViewWizard.OBWidgetListener;
import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse;

public class SwipableCellViewPager extends ViewPager {
    private SwipableCellAdapter adapter;

    public SwipableCellViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new SwipableCellAdapter(getContext());
        setAdapter(adapter);
        postInit();

    }

    public void update(OBRecommendationsResponse recs, OBWidgetListener obViewListener) {
        adapter.setObListener(obViewListener);
        adapter.setRecommendationsResponse(recs);
        adapter.notifyDataSetChanged();
    }

    public ViewGroup getItem(int index) {
        return adapter.getItem(index);
    }

    private void postInit() {
        setHorizontalFadingEdgeEnabled(true);
        setFadingEdgeLength(30);
        setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        setOffscreenPageLimit(10);
    }
}