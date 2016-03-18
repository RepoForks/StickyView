package com.cubebox.library;

import android.view.View;

/**
 * Created by luozi on 2016/2/1.
 */
public interface OnStickyListener {
    /**
     * 滚动时回调
     *
     * @param stickyView 当前滚动的界面
     * @param offset     滚动偏移量 如果未滑动到该控件的顶部 则返回-1
     */
    void onStickyScrolling(View stickyView, int viewIndex, float offset, float viewHeight, boolean isShow);
}
