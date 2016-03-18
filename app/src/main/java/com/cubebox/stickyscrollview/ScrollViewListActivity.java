package com.cubebox.stickyscrollview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cubebox.library.FloatView;
import com.cubebox.library.OnStickyListener;
import com.cubebox.library.StickyScrollView;

import java.util.ArrayList;
import java.util.List;


public class ScrollViewListActivity extends AppCompatActivity implements OnStickyListener {

    private FrameLayout frameLayout, frameLayout2;
    private LinearLayout linearLayout;

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;


    StickyScrollView stickyView;
    FloatView floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_stichy_list);

        stickyView = (StickyScrollView) findViewById(R.id.stickyview);
        floatView = (FloatView) findViewById(R.id.act_scroll_sticky_multiply_floatview);

        mDatas = new ArrayList<>();
        for (int i = 'A'; i <= 'z'; i++) {
            mDatas.add("" + (char) i);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());

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
    public void onStickyScrolling(View stickyView, int viewIndex, float offset, float viewHeight, boolean isShow) {

        switch (viewIndex) {
            case 0:
                if (isShow) {
                    if (frameLayout.getVisibility() == View.INVISIBLE)//为防止重复调用
                        frameLayout.setVisibility(View.VISIBLE);
                } else {
                    if (frameLayout.getVisibility() == View.VISIBLE)//为防止重复调用
                        frameLayout.setVisibility(View.INVISIBLE);
                }
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
                if (isShow) {
                    if (linearLayout.getVisibility() == View.INVISIBLE)//为防止重复调用
                        linearLayout.setVisibility(View.VISIBLE);
                } else {
                    if (linearLayout.getVisibility() == View.VISIBLE)//为防止重复调用
                        linearLayout.setVisibility(View.INVISIBLE);
                }
                linearLayout.setTranslationY(offset - viewHeight);
                break;
            case 2:
                if (isShow) {
                    if (frameLayout2.getVisibility() == View.INVISIBLE)//为防止重复调用
                        frameLayout2.setVisibility(View.VISIBLE);
                } else {
                    if (frameLayout2.getVisibility() == View.VISIBLE)//为防止重复调用
                        frameLayout2.setVisibility(View.INVISIBLE);
                }
                frameLayout2.setTranslationX(offset);
                break;
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ScrollViewListActivity.this).inflate(R.layout.row_recycler_view, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.row_recycler_view_txt);
            }
        }
    }


}
