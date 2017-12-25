package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.elf.zero.utils.LogUtils;
import com.elf.zero.widget.PullRefreshLayout;
import com.elf.zerodemo.R;

public class PullRefreshLayoutActivity extends AppBaseActivity {

    private final static String TAG = PullRefreshLayoutActivity.class.getSimpleName();

    private PullRefreshLayout mPullRefreshLayout;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_layout);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setEmptyView(findViewById(R.id.emptyText));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast(position + "");
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showToast(position + ",long");
                return true;
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                LogUtils.v(TAG, "--onScrollStateChanged--");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                LogUtils.v(TAG, "--onScroll--");
            }
        });

        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);
        mPullRefreshLayout.setPullRefreshInterface(new PullRefreshLayout.PullRefreshInterface() {
            @Override
            public boolean isCanPull() {
                if(mListView.getFirstVisiblePosition() !=0){
                    return false;
                }

                return mListView.getChildCount() > 0 ? mListView.getChildAt(0).getTop() == 0 : true;
            }

            @Override
            public void refresh() {
                startRefresh();
            }
        });

//        showData(20);
    }

    public void startRefresh(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPullRefreshLayout.stopRefresh();
                        showData(50);
                    }
                });
            }
        }.start();
    }

    public void onStopRefresh(View view) {
        mPullRefreshLayout.stopRefresh();
    }

    public void showData(int count){
        String[] names = new String[count];
        for (int i = 1; i <= names.length; i++) {
            names[i - 1] = i + "";
        }
        mArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, names);
        mListView.setAdapter(mArrayAdapter);
    }
}
