package com.elf.zerodemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.elf.zero.utils.LogUtils;
import com.elf.zero.widget.PullRefreshLayout;
import com.elf.zerodemo.R;
import com.elf.zerodemo.widget.LoadMoreWidget;

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

                if(mArrayAdapter==null) {
                    return;
                }

                if (mListView.getLastVisiblePosition() != mArrayAdapter.getCount() - 1) {
                    return;
                }
                View lastVisibleItemView = mListView.getChildAt(mListView.getChildCount() - 1);
                if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListView.getHeight()) {
//                    mPullRefreshLayout.startLoadMore();
                }
            }
        });
//        mListView.addFooterView(new LoadMoreWidget(this));

        mPullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public boolean canPull() {
                if(mListView.getFirstVisiblePosition() !=0){
                    return false;
                }
                return mListView.getChildCount() > 0 ? mListView.getChildAt(0).getTop() == 0 : true;
            }

            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showData(10);
                        mPullRefreshLayout.stopRefresh();
                    }
                }, 5000);
            }
        });
        mPullRefreshLayout.setOnLoadMoreListener(new PullRefreshLayout.OnLoadMoreListener() {
            @Override
            public boolean canPull() {
                if(mArrayAdapter==null) {
                    return false;
                }

                if (mListView.getLastVisiblePosition() != mArrayAdapter.getCount() - 1) {
                    return false;
                }
                View lastVisibleItemView = mListView.getChildAt(mListView.getChildCount() - 1);
                return lastVisibleItemView != null && lastVisibleItemView.getBottom() <= mListView.getHeight();
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                        mPullRefreshLayout.stopLoadMore();
                    }
                }, 3000);
            }
        });

//        mPullRefreshLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPullRefreshLayout.startRefresh();
//            }
//        },300);
    }

    public void showData(int count) {
        String[] names = new String[count];
        for (int i = 1; i <= names.length; i++) {
            names[i - 1] = i + "";
        }
        if (mArrayAdapter == null) {
            mArrayAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1);
        }else {
            mArrayAdapter.clear();
        }
        mArrayAdapter.addAll(names);
        mListView.setAdapter(mArrayAdapter);
    }

    public void loadMoreData(){
        String[] names = new String[3];
        for (int i = 1; i <= names.length; i++) {
            names[i - 1] = i + "+load";
        }
        mArrayAdapter.addAll(names);

    }
}
