package com.elf.zerodemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.elf.zero.Zero;
import com.elf.zerodemo.R;

/**
 * 主页
 * Created by Lidong on 2017/11/15.
 */
public class MainActivity extends AppBaseActivity {

    private String[] names = {
            "NetRequest",
            "LoopViewPager",
            "NewsDetail - ListView",
            "NewsDetail - ViewGroup",
            "AppUtils",
            "DeviceUtils",
            "NetworkUtils",
            "TranslucentStatus",
            "FlowLayout",
            "PullRefreshLayout",
            "Album",
            "About"
    };

    private Class<?>[] clazzs = {
            NetRequestActivity.class,
            LoopViewPagerActivity.class,
            NewsDetailActivity.class,
            NewsDetailViewGroupActivity.class,
            AppUtilsActivity.class,
            DeviceUtilsActivity.class,
            NetworkUtilsActivity.class,
            TranslucentStatusActivity.class,
            FlowLayoutActivity.class,
            PullRefreshLayoutActivity.class,
            AlbumActivity.class,
            AboutActivity.class
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Zero.init(getApplicationContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, names);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(position);
            }
        });
    }

    private void startActivity(int position) {
        Intent intent = new Intent(this, clazzs[position]);
        intent.putExtra(AppBaseActivity.ARG_TITLE, names[position]);
        startActivity(intent);
    }
}
