package com.cubebox.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by luozi on 2016/2/2.
 */
public class StickyView extends RelativeLayout implements StickyScrollView.OnScrollerChangeListener {
    private View rootView;
    private StickyScrollView stickyScrollView;
    private FloatView floatView;
    private ViewGroup contentView;
    private LinearLayout scrollViewLayout;
    private View paddingView;

    private int startHeight = 0;//开始的高度
    private int initTop = 0, currTop = 0;//初始化高度和实时高度边距
    private boolean isReleaseTouch = true;
    private int stickyBackgroundColor = 0xFFFFFFFF;//init 为白色


    public StickyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) return;
        /**
         * 加载root view
         * */
        rootView = inflate(context, R.layout.view_roll_root, null);
        addView(rootView);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RollView);
        int contentID = ta.getResourceId(R.styleable.RollView_contentLayout, -1);
        int floatID = ta.getResourceId(R.styleable.RollView_floatLayout, -1);
        startHeight = ta.getDimensionPixelSize(R.styleable.RollView_startHeight, 0);
        isReleaseTouch = ta.getBoolean(R.styleable.RollView_releaseTouch, true);
        stickyBackgroundColor = ta.getColor(R.styleable.RollView_stickyBackground, 0xFFFFFFFF);//init 为白色

        /**
         * 如果用户未设置contentlayout的id的话再抛出异常
         * */
        if (contentID == -1 || floatID == -1)
            throw new IllegalArgumentException("contentLayout or floatLayout cannot be null!");

        paddingView = rootView.findViewById(R.id.roll_root_stickyview_paddingview);
        scrollViewLayout = (LinearLayout) rootView.findViewById(R.id.roll_root_stickyview_scrollview_content);
        stickyScrollView = (StickyScrollView) rootView.findViewById(R.id.roll_root_stickyview);
        floatView = (FloatView) rootView.findViewById(R.id.roll_root_floatview);


        contentView = (ViewGroup) inflate(context, contentID, null);
        scrollViewLayout.addView(contentView);//添加内容图层

        floatView.addFlotView(inflate(context, floatID, null));


        //准备透明层界面
        setBackgroundColor(Color.TRANSPARENT);
        stickyScrollView.setBackgroundColor(Color.TRANSPARENT);
        contentView.setBackgroundColor(stickyBackgroundColor);
        floatView.setBackgroundColor(Color.TRANSPARENT);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        floatView.attchScrollView(stickyScrollView);

        stickyScrollView.setOnScrollerChangeListener(this);
    }

    /**
     * 悬浮view 滚动位置监听
     */
    public void setOnStickyListener(OnStickyListener onStickyListener) {
        stickyScrollView.setOnStickyListener(onStickyListener);
    }

    public StickyScrollView getStickyScrollView() {
        return stickyScrollView;
    }

    public FloatView getFloatView() {
        return floatView;
    }

    /**
     * 增加偏移量
     */
    public void setTopFloatOffset(int index, int offset) {
        stickyScrollView.setTopFloatOffset(index, offset);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCatchTouch(ev);
    }

    private boolean isCatchTouch(MotionEvent ev) {
        if (!isReleaseTouch)
            return false;

        if (ev.getY() >= currTop) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onScrolling(int l, int t, int oldl, int oldt) {
        currTop = initTop - t;
    }

    /**
     * 在子布局中吧paddingview设置为超过屏幕的高度，为避免闪烁
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            if (isInEditMode()) return;
            initTop = bottom - startHeight;
            currTop = initTop;
            paddingView.getLayoutParams().height = initTop;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
