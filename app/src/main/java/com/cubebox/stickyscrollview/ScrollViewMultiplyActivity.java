package com.cubebox.stickyscrollview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cubebox.library.FloatView;
import com.cubebox.library.OnStickyListener;
import com.cubebox.library.StickyScrollView;


public class ScrollViewMultiplyActivity extends AppCompatActivity implements OnStickyListener {

    private FrameLayout frameLayout, frameLayout2;
    private LinearLayout linearLayout;

    StickyScrollView stickyView;
    FloatView floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_stichy_multiply);

        stickyView = (StickyScrollView) findViewById(R.id.stickyview);
        floatView = (FloatView) findViewById(R.id.act_scroll_sticky_multiply_floatview);


        stickyView.setOnStickyListener(this);
        /**
         * 传递触摸事件
         * */
        floatView.attchScrollView(stickyView);

        frameLayout = (FrameLayout) findViewById(R.id.float_view_layout);
        linearLayout = (LinearLayout) findViewById(R.id.float_view_layout3);
        frameLayout2 = (FrameLayout) findViewById(R.id.float_view_layout2);

        frameLayout.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        frameLayout2.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onStickyScrolling(View stickyView, int viewIndex, float offset, float viewHeight) {

        switch (viewIndex) {
            case 0:
                if (offset >= 0) {
                    if (frameLayout.getVisibility() == View.INVISIBLE)//为防止重复调用
                        frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.setTranslationX(offset);
                } else {
                    if (frameLayout.getVisibility() == View.VISIBLE)
                        frameLayout.setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                if (offset >= 0) {
                    if (linearLayout.getVisibility() == View.INVISIBLE)//为防止重复调用
                        linearLayout.setVisibility(View.VISIBLE);
                    linearLayout.setTranslationY(offset - viewHeight);
                } else {
                    if (linearLayout.getVisibility() == View.VISIBLE)//为防止重复调用
                        linearLayout.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                if (offset >= 0) {
                    if (frameLayout2.getVisibility() == View.INVISIBLE)//为防止重复调用
                        frameLayout2.setVisibility(View.VISIBLE);
                    frameLayout2.setTranslationX(offset);
                } else {
                    if (frameLayout2.getVisibility() == View.VISIBLE)//为防止重复调用
                        frameLayout2.setVisibility(View.INVISIBLE);
                }
                break;
        }

    }

}
