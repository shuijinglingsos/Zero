package com.elf.zerodemo.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.elf.zerodemo.R;

public class GroupListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mGroupHeader;
    GroupListAdapter mGroupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
//        http://blog.csdn.net/chenguang79/article/details/52247912
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mGroupHeader = (TextView) findViewById(R.id.tv_header);
        mGroupHeader.setPadding(20,20,20,20);
        mGroupHeader.setBackgroundColor(Color.GRAY);
        mGroupHeader.setTextColor(Color.WHITE);

        //设置布局管理器
        final GridLayoutManager linearLayoutManager = new GridLayoutManager(this,4);
        linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mGroupListAdapter.isGroupHeader(position)) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.addItemDecoration(new MyDecoration(getContext()));

        mGroupListAdapter = new GroupListAdapter(this);
        mRecyclerView.setAdapter(mGroupListAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstItem < 8) {
                    mGroupHeader.setText("group0");
                } else if (firstItem < 30) {
                    mGroupHeader.setText("group8");
                } else if (firstItem < 65) {
                    mGroupHeader.setText("group30");
                } else {
                    mGroupHeader.setText("group65");
                }

                //找下一个分组头部
                for (int i = firstItem + 1; i < linearLayoutManager.findLastVisibleItemPosition(); i++) {
                    if (mGroupListAdapter.isGroupHeader(i)) {  //找到
                        View groupView = linearLayoutManager.findViewByPosition(i);
                        if (groupView != null) {
                            if (groupView.getTop() <= mGroupHeader.getMeasuredHeight()) {
                                mGroupHeader.layout(0, groupView.getTop() - mGroupHeader.getMeasuredHeight(),
                                        mGroupHeader.getMeasuredWidth(),
                                        groupView.getTop());
//                                mGroupHeader.invalidate();
                            }else{
                                mGroupHeader.layout(0, 0,
                                        mGroupHeader.getMeasuredWidth(),
                                        mGroupHeader.getMeasuredHeight());
//                                mGroupHeader.invalidate();
                            }
                        }
                        return;
                    }
                }

                mGroupHeader.layout(0, 0,
                        mGroupHeader.getMeasuredWidth(),
                        mGroupHeader.getMeasuredHeight());
            }
        });
    }


    public static class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private Context mContext;

        public GroupListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                return new GroupView(new TextView(mContext));
            } else {
                return new ItemView(new TextView(mContext));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof GroupView) {
                ((GroupView) holder).mTextView.setText("group" + position);
            } else if (holder instanceof ItemView) {
                ((ItemView) holder).mTextView.setText("item" + position);
            }
        }

        @Override
        public int getItemCount() {
            return 150;
        }

        @Override
        public int getItemViewType(int position) {
            return isGroupHeader(position) ? 0 : 1;
        }

        public boolean isGroupHeader(int position){
            return position == 0 || position == 8 || position == 30 || position==65;
        }
    }

    public static class GroupView extends RecyclerView.ViewHolder {
        public GroupView(View arg0) {
            super(arg0);
            mTextView = (TextView) arg0;
            mTextView.setPadding(20,20,20,20);
            mTextView.setBackgroundColor(Color.GRAY);
            mTextView.setTextColor(Color.WHITE);
        }

        TextView mTextView;
    }

    public static class ItemView extends RecyclerView.ViewHolder {
        public ItemView(View arg0) {
            super(arg0);
            mTextView = (TextView) arg0;
            mTextView.setPadding(20,20,20,20);
        }

        TextView mTextView;
    }

}
