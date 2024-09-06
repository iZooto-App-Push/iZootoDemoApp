package com.k.deeplinkingtesting.Drawer;


public interface ScrollViewListener {
    void onScrollChanged(ListenableScrollView scrollView,
                         int x, int y, ListenableScrollView.ScrollDirection direction, int delta, boolean endReached);
}
