package com.cubebox.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by luozi on 2016/2/1.
 */
public class FloatView extends LinearLayout  {

    /**
     * float root view
     */
    private View floatView;
    /**
     * root scroll view
     */
    private StickyScrollView stickyView = null;




    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setOrientation(VERTICAL);

    }

    /**
     * 添加悬浮view
     */
    public void addFlotView(View v) {
        floatView = v;
        if (floatView != null)
            addView(floatView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (stickyView != null) {
            //把本页面的子view触摸事件传递到上层scrollview
            stickyView.onTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }




    public void attchScrollView(StickyScrollView view) {
        this.stickyView = view;
    }


}
