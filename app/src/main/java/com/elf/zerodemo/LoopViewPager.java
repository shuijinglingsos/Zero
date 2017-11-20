package com.elf.zerodemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 循环自动播放的ViewPager
 * Created by Lidong on 2017/11/20.
 */
public class LoopViewPager extends ViewPager {

    private Context mContext;
    private VPAdapter adapter;

    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        removeCallbacks(autoRunnable);
                        break;

                    case MotionEvent.ACTION_UP:
                        postDelayed(autoRunnable,2000);
                        break;
                }
                return false;
            }
        });

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("ViewPagerImpl", position + "_" + positionOffset + "_" + positionOffsetPixels);
                Log.d("ViewPagerImpl", "position = " + getCurrentItem());

                if (positionOffsetPixels == 0 || position < getCurrentItem()) {
                    FrameLayoutImpl view = getViewByPosition(position);
                    if (view != null) {
                        view.loadData();
                    }
                } else {   //后一页显示
                    FrameLayoutImpl view = getViewByPosition(position + 1);
                    if (view != null) {
                        view.loadData();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("ViewPagerImpl", "onPageSelected position = " + position);
                FrameLayoutImpl view = getViewByPosition(position);
                if (view != null) {
                    view.loadData();
                }
                index=position;
            }

            private int index;

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("ViewPagerImpl", "onPageScrollStateChanged state = " + state);
                if(state==0){

                    if (adapter != null) {
                        if (index == 0 )
                            setCurrentItem(adapter.realCount(), false);
                        else if (index == adapter.getCount() - 1 )
                            setCurrentItem(adapter.realCount() * 2 - 1, false);
                    }
                }
            }
        });
    }

    public void setItems(List<View> views) {

        removeCallbacks(autoRunnable);

        adapter = new VPAdapter(mContext);
        adapter.setmItems(views);
        setAdapter(adapter);

        setCurrentItem(views.size());

        postDelayed(autoRunnable, 2000);
    }

    private Runnable autoRunnable=new Runnable() {
        @Override
        public void run() {
            setCurrentItem(getCurrentItem() + 1);
            postDelayed(this,2000);
        }
    };

    private FrameLayoutImpl getViewByPosition(int position) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof FrameLayoutImpl) {
                FrameLayoutImpl item = (FrameLayoutImpl) view;
                if (item.getIndex() == position) {
                    return item;
                }
            }
        }
        return null;
    }

    private static class VPAdapter extends PagerAdapter {

        private Context mContext;
        private List<View> mItems = new ArrayList<>();

        VPAdapter(Context context) {
            mContext = context;
        }

        void setmItems(List<View> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size() < 2 ? mItems.size() : mItems.size() *3;  //Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrameLayoutImpl item = new FrameLayoutImpl(mContext);
            item.setView(position, mItems.get(tranfserPosition(position)));
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            FrameLayoutImpl view =((LoopViewPager)container).getViewByPosition(position);
            if (view != null) {
                container.removeView(view);
            }
        }

        private int tranfserPosition(int position) {
            if (mItems.size() < 2) {
                return position;
            }

            return position % mItems.size();
        }

        int realCount(){
            return mItems.size();
        }
    }

    public static class FrameLayoutImpl extends FrameLayout {

        private View mView;
        private int mIndex;

        public FrameLayoutImpl(@NonNull Context context) {
            super(context);
        }

        public void loadData() {
            if (mView == null) {
                return;
            }
            if (mView.getParent() != null) {
                if (mView.getParent() == this) {
                    return;
                }
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
            addView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        public void setView(int index, View view) {
            mIndex = index;
            mView = view;
        }

        public int getIndex() {
            return mIndex;
        }
    }
}
