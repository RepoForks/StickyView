package com.cubebox.stickyscrollview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void stickyView(View v) {
        startActivity(new Intent(this, ScrollViewMultiplyActivity.class));
    }

    public void stickyView_header(View view) {
        startActivity(new Intent(this, RollViewActivity.class));
    }

    public void stickyView_list(View view) {
        startActivity(new Intent(this, ScrollViewListActivity.class));
    }

    public void stickyView_list2(View view) {
        startActivity(new Intent(this, ScrollViewList2Activity.class));
    }
}
