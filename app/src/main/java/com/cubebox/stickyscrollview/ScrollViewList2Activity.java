package com.cubebox.stickyscrollview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.cubebox.library.OnStickyListener;
import com.cubebox.library.StickyView;
import com.cubebox.stickyscrollview.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ScrollViewList2Activity extends AppCompatActivity implements OnStickyListener {

    private LinearLayout headLayout;
    private TextView headTxt;

    private LinearLayout describeLayout;
    private ViewPager viewPager1, viewPager2;
    private StickyView stickyView;
    private Button booking;

    private MapView mapView;
    private AMap aMap;

    private MyViewPagerAdapter viewPagerAdapter1 = null, viewPagerAdapter2 = null;
    private List<View> viewList1 = null, viewList2;

    private List<String> mDatas, mDatas2;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_stichy_list2);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        aMap = mapView.getMap();

        stickyView = (StickyView) findViewById(R.id.act_roll_view_stickyView);
        stickyView.setOnStickyListener(this);

        //content headLayout
        viewPager1 = (ViewPager) findViewById(R.id.roll_content_view_viewpager);
        viewPager2 = (ViewPager) findViewById(R.id.roll_content_view_viewpager2);

        //content headLayout

        headLayout = (LinearLayout) findViewById(R.id.float_view_layout);
        describeLayout = (LinearLayout) findViewById(R.id.float_view_layout3);

        headTxt = (TextView) findViewById(R.id.float_view_head_txt);
        booking = (Button) findViewById(R.id.float_view_booking);

        headLayout.setVisibility(View.INVISIBLE);
        describeLayout.setVisibility(View.INVISIBLE);
        booking.setVisibility(View.INVISIBLE);

        viewList1 = new ArrayList<>();
        viewList2 = new ArrayList<>();

        mDatas = new ArrayList<>();
        for (int i = 'A'; i <= 'o'; i++) {
            mDatas.add("" + (char) i);
        }
        mDatas2 = new ArrayList<>();
        for (int i = 'A'; i <= 'o'; i++) {
            mDatas2.add("" + (char) i);
        }


        setViewList();

        viewPagerAdapter1 = new MyViewPagerAdapter(viewList1);
        viewPagerAdapter2 = new MyViewPagerAdapter(viewList2);

        viewPager1.setAdapter(viewPagerAdapter1);
        viewPager2.setAdapter(viewPagerAdapter2);


        headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(ScrollViewList2Activity.this, "head");
            }
        });
    }


    @Override
    public void onStickyScrolling(View v, int viewIndex, float offset, float viewHeight, boolean isShow) {
        switch (viewIndex) {
            case 0:
                if (isShow) {
                    if (headLayout.getVisibility() == View.INVISIBLE)//为防止重复调用
                        headLayout.setVisibility(View.VISIBLE);
                } else {
                    if (headLayout.getVisibility() == View.VISIBLE)//为防止重复调用
                        headLayout.setVisibility(View.INVISIBLE);
                }

                if (offset >= 0) {
                    float percent = offset / viewHeight;
                    int minHeight = dip2px(this, 60);
                    int minSize = 12;
                    ViewGroup.LayoutParams params = headLayout.getLayoutParams();
                    params.height = minHeight + (int) ((viewHeight - minHeight) * (1 - percent));
                    headTxt.setTextSize(minSize + ((14 - minSize) * (1 - percent)));
                    headLayout.setLayoutParams(params);
                    /**
                     * 如果悬浮出来的view和scrollview设置tag 的view 存在高度差则需要使用此方法来补全高度差,否则滑动会对不齐
                     * 如果想要scroll对齐则当contentview和floatview的view高度不同时必须设置此方法
                     * */
                    stickyView.setTopFloatOffset(viewIndex, (int) viewHeight - minHeight);
                } else {

                }
                break;
            case 1:

                if (isShow) {
                    if (describeLayout.getVisibility() == View.INVISIBLE)//为防止重复调用
                        describeLayout.setVisibility(View.VISIBLE);
                } else {
                    if (describeLayout.getVisibility() == View.VISIBLE)//为防止重复调用
                        describeLayout.setVisibility(View.INVISIBLE);
                }

                describeLayout.setTranslationY(offset - viewHeight);
                break;
            case 2:
                if (isShow) {
                    if (booking.getVisibility() == View.INVISIBLE)//为防止重复调用
                        booking.setVisibility(View.VISIBLE);
                } else {
                    if (booking.getVisibility() == View.VISIBLE)//为防止重复调用
                        booking.setVisibility(View.INVISIBLE);
                }
                booking.setTranslationY(viewHeight - offset - 20);
                break;
        }

    }

    private void setViewList() {

        RecyclerView r1 = (RecyclerView) getLayoutInflater().inflate(R.layout.view_list, null);
        r1.setLayoutManager(new FullyLinearLayoutManager(this));
        r1.setAdapter(mAdapter = new HomeAdapter());
        viewList1.add(r1);

        RecyclerView r2 = (RecyclerView) getLayoutInflater().inflate(R.layout.view_list, null);
        r2.setLayoutManager(new FullyLinearLayoutManager(this));
        r2.setAdapter(mAdapter = new HomeAdapter());
        viewList1.add(r2);

        ImageView v2 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v2.setImageResource(R.mipmap.f2);
        viewList1.add(v2);


        ImageView v3 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v3.setImageResource(R.mipmap.f3);
        viewList1.add(v3);

        ImageView v4 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v4.setImageResource(R.mipmap.f4);
        viewList1.add(v4);

        ImageView v5 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v5.setImageResource(R.mipmap.f5);
        viewList1.add(v5);

        ImageView v6 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v6.setImageResource(R.mipmap.f5);
        ImageView v7 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v7.setImageResource(R.mipmap.f4);
        ImageView v8 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v8.setImageResource(R.mipmap.f3);
        ImageView v9 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v9.setImageResource(R.mipmap.f2);
        ImageView v10 = (ImageView) getLayoutInflater().inflate(R.layout.view_img, null);
        v10.setImageResource(R.mipmap.f1);


        viewList2.add(v6);
        viewList2.add(v7);
        viewList2.add(v8);
        viewList2.add(v9);
        viewList2.add(v10);
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ScrollViewList2Activity.this).inflate(R.layout.row_recycler_view, parent,
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

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(ScrollViewList2Activity.this, mDatas.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    class HomeAdapter2 extends RecyclerView.Adapter<HomeAdapter2.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ScrollViewList2Activity.this).inflate(R.layout.row_recycler_view, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas2.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas2.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.row_recycler_view_txt);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(ScrollViewList2Activity.this, mDatas2.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
