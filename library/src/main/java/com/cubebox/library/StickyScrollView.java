package com.cubebox.library;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luozi on 2016/1/29.
 */
public class StickyScrollView extends ScrollView {


    /**
     * 是否悬浮标记
     */
    public static final String STICKY_TAG = "sticky";
    /**
     * 要悬浮的view 的背景是否为隐藏
     */
    public static final String FLAG_NOT_TRANSPARENT = "-nottransparent";
    /**
     * * 从底部开始悬浮偏移
     */
    public static final String FLAG_STICKY_BOTTOM = "-stickybottom";
    /**
     * Sticky Views list
     */
    private ArrayList<View> stickyViews;
    /**
     * old move的offset
     */
    private List<Float> lastInts;
    /**
     * offsets of float views in sticky view
     */
    private List<Integer> offsets;

    private OnStickyListener onStickyListener = null;
    private OnScrollerChangeListener onScrollerChangeListener = null;
    /**
     * 由于子控件的绘制可能导致scrollview自动滚动到控件更新位置，于是做了在第一次layout的时候则初始化scrollview的滑动位置标记
     */
    private boolean hasScrollTop = false;

    /**
     * touch参数保存
     */
    private int downX;
    private int downY;
    private int mTouchSlop;

    public StickyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    /**
     * 初始化
     */
    private void init() {
        stickyViews = new ArrayList<>();
        lastInts = new ArrayList<>();
        offsets = new ArrayList<>();

    }


    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollerChangeListener != null)
            onScrollerChangeListener.onScrolling(l, t, oldl, oldt);
        for (int i = 0; i < stickyViews.size(); i++) {
            View v = stickyViews.get(i);
            /**
             * 屏幕顶部加高度 + 本次view的上一个view的的高度
             * */
            float childTop = getFloatViewTopHeight(i);
            if (t >= childTop) {
                /**
                 * view 本来为可见并且tag中包含透明属性，才隐藏
                 * */
                if (v.getVisibility() == VISIBLE && !getStringTagForView(v).contains(FLAG_NOT_TRANSPARENT)) {
                    v.setVisibility(INVISIBLE);
                }

                float offset = t - childTop;
                offset = offset >= v.getHeight() ? v.getHeight() : offset;
                /**
                 * 判断本次的偏移量是否和上次一样如果一样则不回调
                 * */
                if (offset == lastInts.get(i)) {
                    continue;
                }
                lastInts.set(i, offset);

                if (onStickyListener != null)
                    onStickyListener.onStickyScrolling(v, i, offset, v.getHeight(), true);
            } else {
                if (v.getVisibility() == INVISIBLE) {
                    v.setVisibility(VISIBLE);
                    lastInts.set(i, 0f);
                    if (onStickyListener != null)
                        onStickyListener.onStickyScrolling(v, i, 0, v.getHeight(), false);
                }

                /**
                 * 判断本次的偏移量是否和上次一样如果一样则不回调
                 * */
                if (lastInts.get(i) == -1) {
                    continue;
                }
                lastInts.set(i, -1f);

                if (onStickyListener != null)
                    onStickyListener.onStickyScrolling(v, i, -1f, v.getHeight(), false);


            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isInEditMode()) {
            findStickyViews(getChildAt(0));
            initList();
            if (hasScrollTop == false && !changed) {
                scrollTo(0, 0);
                hasScrollTop = true;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public void setOnStickyListener(OnStickyListener onStickyListener) {
        this.onStickyListener = onStickyListener;
    }

    /**
     * find sticky Views when  ScrollView init
     */
    private void findStickyViews(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                String tag = getStringTagForView(vg.getChildAt(i));
                if (tag != null && tag.contains(STICKY_TAG) && !stickyViews.contains(vg.getChildAt(i))) {
                    stickyViews.add(vg.getChildAt(i));
                } else if (vg.getChildAt(i) instanceof ViewGroup) {
                    findStickyViews(vg.getChildAt(i));
                }
            }
        } else {
            String tag = (String) v.getTag();
            if (tag != null && tag.contains(STICKY_TAG) && !stickyViews.contains(v)) {
                stickyViews.add(v);
            }
        }
    }

    /**
     * 初始化lastint 列表
     */
    private void initList() {
        for (int i = 0; i < stickyViews.size(); i++) {
            lastInts.add(i, -1f);
            offsets.add(i, 0);
        }
    }

    /**
     * get View from tag
     *
     * @param v
     */
    private String getStringTagForView(View v) {
        Object tagObject = v.getTag();
        return String.valueOf(tagObject);
    }

    /**
     * 增加偏移量
     */
    public void setTopFloatOffset(int index, int offset) {
        if (index >= 0 && offsets.size() - 1 >= index) {//如果已经存在数值
            offsets.set(index, offset);
        } else {
            offsets.add(index, offset);
        }
    }

    /**
     * 得到偏移量，如果list不存在此index的偏移量测返回0
     */
    public int getTopFloatOffset(int index) {
        if (index >= 0 && offsets.size() - 1 >= index) {//如果已经存在数值
            int countOffset = 0;
            for (int i = 0; i < index; i++) {
                countOffset += offsets.get(i);
            }
            return countOffset;
        } else {
            return 0;
        }
    }

    /**
     * 得到浮动页面的顶部所有页面相加的顶部高度
     */
    private int getFloatViewTopHeight(int index) {
        int countTop = getTopForViewRelativeOnlyChild(stickyViews.get(index)) +
                (getStringTagForView(stickyViews.get(index)).contains(FLAG_STICKY_BOTTOM) ? stickyViews.get(index).getHeight() : 0) + getTopFloatOffset(index);
        for (int i = index - 1; i >= 0; i--) {
            countTop -= stickyViews.get(i).getHeight();
        }
        return countTop;
    }

    /**
     * get top int fro View Realative only child
     */
    private int getTopForViewRelativeOnlyChild(View v) {
        int top = v.getTop();
        while (v.getParent() != getChildAt(0)) {
            v = (View) v.getParent();
            top += v.getTop();
        }
        return top;
    }


    private int getLeftForViewRelativeOnlyChild(View v) {
        int left = v.getLeft();
        while (v.getParent() != getChildAt(0)) {
            v = (View) v.getParent();
            left += v.getLeft();
        }
        return left;
    }


    private int getRightForViewRelativeOnlyChild(View v) {
        int right = v.getRight();
        while (v.getParent() != getChildAt(0)) {
            v = (View) v.getParent();
            right += v.getRight();
        }
        return right;
    }

    private int getBottomForViewRelativeOnlyChild(View v) {
        int bottom = v.getBottom();
        while (v.getParent() != getChildAt(0)) {
            v = (View) v.getParent();
            bottom += v.getBottom();
        }
        return bottom;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public void setOnScrollerChangeListener(OnScrollerChangeListener onScrollerChangeListener) {
        this.onScrollerChangeListener = onScrollerChangeListener;
    }

    public interface OnScrollerChangeListener {
        void onScrolling(int l, int t, int oldl, int oldt);
    }


}
