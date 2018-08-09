package com.elf.zero.image;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

/**
 * 图片选择对话框
 * Created by Lidong on 2018/8/9.
 */
public class ImageChooseDialog {

    private BaseImageChooseDialog mDialog;

    public ImageChooseDialog(Context context) {
        mDialog = new DefaultImageChooseDialog(context);
    }

    public ImageChooseDialog show() {
        mDialog.show();
        return this;
    }

    public ImageChooseDialog listener(OnOperateListener listener) {
        mDialog.setListener(listener);
        return this;
    }

    public static ImageChooseDialog build(Context context) {
        return new ImageChooseDialog(context);
    }

    /**
     * 操作监听
     */
    public interface OnOperateListener {

        /**
         * 打开相机
         */
        void camera();

        /**
         * 打开相册
         */
        void album();

        /**
         * 取消
         */
        void cancel();
    }

    /**
     * 图片选择对话框基类
     */
    public static abstract class BaseImageChooseDialog implements OnOperateListener {

        private OnOperateListener mOnOperateListener;

        public void setListener(OnOperateListener listener) {
            mOnOperateListener = listener;
        }

        public abstract void show();

        @Override
        public void camera() {
            if (mOnOperateListener != null) {
                mOnOperateListener.camera();
            }
        }

        @Override
        public void album() {
            if (mOnOperateListener != null) {
                mOnOperateListener.album();
            }
        }

        @Override
        public void cancel() {
            if (mOnOperateListener != null) {
                mOnOperateListener.cancel();
            }
        }
    }

    /**
     * 默认图片选择对话框实现
     */
    private static class DefaultImageChooseDialog extends BaseImageChooseDialog {

        private Context mContext;

        public DefaultImageChooseDialog(Context context) {
            mContext = context;
        }

        @Override
        public void show() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setItems(new String[]{"从相册中选择", "拍照"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        album();
                    } else {
                        camera();
                    }
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cancel();
                        }
                    }, 200);
                }
            });
            builder.show();
        }
    }
}
