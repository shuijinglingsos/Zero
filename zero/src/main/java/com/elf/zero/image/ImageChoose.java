package com.elf.zero.image;

import android.content.Context;
import android.util.SparseArray;

import com.elf.zero.utils.LogUtils;

import java.io.Serializable;
import java.util.Random;

/**
 * 图片选择
 * Created by Lidong on 2018/3/12.
 */
public class ImageChoose {

    private final static String TAG=ImageChoose.class.getSimpleName();

    protected final static int OPEN_TYPE_SELECT = 0;
    protected final static int OPEN_TYPE_ALBUM = 1;
    protected final static int OPEN_TYPE_CAMERA = 2;

    private static ChooseListenerManager mChooseListener;

    public static synchronized ChooseListenerManager chooseListener() {
        if (mChooseListener == null) {
            mChooseListener = new ChooseListenerManager();
        }
        return mChooseListener;
    }

    public static synchronized Build get(Context context) {
        return new Build(context);
    }

    protected static class ImageChooseParameter implements Serializable {

        public int openType;  //0 选择  1相册   2相机

        public int listener;

        public boolean isCrop;
        public int aspectX;
        public int aspectY;

        public int outputX;
        public int outputY;
    }

    /**
     * 构建器
     */
    public static class Build {
        private Context mContext;
        private OnChooseListener mOnChooseListener;
        ImageChooseParameter mParameter;

        Build(Context context) {
            mContext = context;
            mParameter = new ImageChooseParameter();
        }

        /**
         * 裁剪
         * @return this
         */
        public Build crop() {
            mParameter.isCrop = true;
            return this;
        }

        /**
         * 裁剪比例
         * @param x x
         * @param y y
         * @return this
         */
        public Build cropAspect(int x, int y) {
            mParameter.isCrop = true;
            mParameter.aspectX = x;
            mParameter.aspectY = y;
            return this;
        }

        /**
         * 裁剪输出大小
         * @param x x
         * @param y y
         * @return this
         */
        public Build cropOutput(int x, int y) {
            mParameter.isCrop = true;
            mParameter.outputX = x;
            mParameter.outputY = y;
            return this;
        }

        /**
         * 监听
         * @param listener listener
         * @return this
         */
        public Build listener(OnChooseListener listener) {
            mOnChooseListener = listener;
            return this;
        }

        /**
         * 打开
         */
        public void open() {
            while (true) {
                int random = new Random().nextInt(999);
                if(!chooseListener().contains(random)){
                    mParameter.listener = random;
                    chooseListener().put(random,mOnChooseListener);
                    break;
                }
            }
            ImageChooseActivity.open(mContext, mParameter);
        }

        /**
         * 打开相册
         */
        public void openAlbum() {
            mParameter.openType = 1;
            open();
        }

        /**
         * 打开相机
         */
        public void openCamera() {
            mParameter.openType = 2;
            open();
        }
    }

    /**
     * 选择监听管理
     */
    public static class ChooseListenerManager {

        protected SparseArray<OnChooseListener> mOnChooseListeners;

        protected ChooseListenerManager() {
            mOnChooseListeners = new SparseArray<>();
        }

        protected boolean contains(int key) {
            return mOnChooseListeners.indexOfKey(key) >= 0;
        }

        protected void put(int key, OnChooseListener listener) {
            mOnChooseListeners.put(key, listener);
            printSize();
        }

        protected OnChooseListener get(int key) {
            return mOnChooseListeners.get(key);
        }

        protected void remove(int key) {
            mOnChooseListeners.remove(key);
            printSize();
        }

        protected void choose(int key, String[] files) {
            ImageChoose.OnChooseListener listener = mOnChooseListeners.get(key);
            if (listener != null) {
//                remove(key);
                listener.choose(files);
            }
        }

        protected void cancel(int key) {
            ImageChoose.OnChooseListener listener = mOnChooseListeners.get(key);
            if (listener != null) {
//                remove(key);
                listener.cancel();
            }
        }

        private void printSize() {
            LogUtils.v(TAG, "choose listener size : " + mOnChooseListeners.size());
        }
    }

    /**
     * 选择监听
     */
    public interface OnChooseListener {
        /**
         * 选择
         *
         * @param files 选择的文件
         */
        void choose(String[] files);

        /**
         * 取消
         */
        void cancel();
    }

}
