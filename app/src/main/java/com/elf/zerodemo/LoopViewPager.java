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

/**
 * 循环自动播放的ViewPager
 * Created by Lidong on 2017/11/20.
 */
public class LoopViewPager extends FrameLayout {

    /**
     * 自动播放间隔时间
     */
    private final static int INTERVAL = 3000;

    private ViewPager mViewPager;
    private LoopViewPagerAdapter mAdapter;
    private boolean mAutoPlaying = false;

    /**
     * 自动播放
     */
    private Runnable mAutoPlayRunnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            postDelayed(this, INTERVAL);
        }
    };

    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mViewPager = new ViewPager(getContext());
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        removeCallbacks(mAutoPlayRunnable);
                        break;

                    case MotionEvent.ACTION_UP:
                        if (mAutoPlaying) {
                            postDelayed(mAutoPlayRunnable, INTERVAL);
                        }
                        break;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("ViewPagerImpl", position + "_" + positionOffset + "_" + positionOffsetPixels);
                Log.d("ViewPagerImpl", "position = " + mViewPager.getCurrentItem());

                if (positionOffsetPixels == 0 || position < mViewPager.getCurrentItem()) {
                    ViewPagerItem view = getViewByPosition(mViewPager, position);
                    if (view != null) {
                        view.loadData();
                    }
                } else {   //后一页显示
                    ViewPagerItem view = getViewByPosition(mViewPager, position + 1);
                    if (view != null) {
                        view.loadData();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("ViewPagerImpl", "onPageSelected position = " + position);
                ViewPagerItem view = getViewByPosition(mViewPager, position);
                if (view != null) {
                    view.loadData();
                }
                index = position;
            }

            private int index;

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("ViewPagerImpl", "onPageScrollStateChanged state = " + state);
                if (state == 0) {
                    if (mAdapter != null) {
                        if (index == 0) {
                            mViewPager.setCurrentItem(mAdapter.realCount(), false);
                        } else if (index == mAdapter.getCount() - 1) {
                            mViewPager.setCurrentItem(mAdapter.realCount() * 2 - 1, false);
                        }
                    }
                }
            }
        });

        addView(mViewPager, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 开始自动播放
     */
    public void startAutoPlay(){
        removeCallbacks(mAutoPlayRunnable);
        postDelayed(mAutoPlayRunnable, INTERVAL);
        mAutoPlaying =true;
    }

    /**
     * 停止自动播放
     */
    public void stopAutoPlay() {
        mAutoPlaying = false;
        removeCallbacks(mAutoPlayRunnable);
    }

    public void setAdapter(LoopViewPagerAdapter adapter) {
        mAdapter = adapter;
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(adapter.realCount());
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (mAdapter == null || mAdapter.realCount() < 0 || mAdapter.realCount() - 1 < item) {
            return;
        }
        int currentIndex = mViewPager.getCurrentItem() % mAdapter.realCount();
        int toIndex = mViewPager.getCurrentItem() - (currentIndex - item);
        mViewPager.setCurrentItem(toIndex, smoothScroll);
    }

    public int getCurrentItem() {
        if (mAdapter == null || mAdapter.getCount() < 0) {
            return 0;
        }
        return mViewPager.getCurrentItem() % mAdapter.realCount();
    }

    private static ViewPagerItem getViewByPosition(ViewGroup container, int position) {
        int count = container.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = container.getChildAt(i);
            if (view instanceof ViewPagerItem) {
                ViewPagerItem item = (ViewPagerItem) view;
                if (item.getIndex() == position) {
                    return item;
                }
            }
        }
        return null;
    }


    /**
     * ViewPager适配器
     */
    public static abstract class LoopViewPagerAdapter extends PagerAdapter {

        protected Context mContext;

        public LoopViewPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return realCount() < 2 ? realCount() : realCount() * 3;  //Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewPagerItem item = new ViewPagerItem(mContext);
            item.setView(position, instantiateItemView(tranfserPosition(position)));
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPagerItem view = getViewByPosition(container ,position);
            if (view != null) {
                container.removeView(view);
            }
        }

        private int tranfserPosition(int position) {
            if (realCount() < 2) {
                return position;
            }

            return position % realCount();
        }



        public abstract View instantiateItemView(int position);

        public abstract int realCount();
    }

    /**
     * ViewPager项目
     */
    public static class ViewPagerItem extends FrameLayout {

        private View mView;
        private int mIndex;

        public ViewPagerItem(@NonNull Context context) {
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
