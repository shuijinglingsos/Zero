package com.elf.zerodemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.elf.zero.utils.DeviceUtils;
import com.elf.zerodemo.R;
import com.elf.zerodemo.model.AlbumFile;
import com.elf.zerodemo.widget.ImageFileListItem;
import com.elf.zerodemo.widget.VideoFileListItem;

/**
 * 相册文件列表适配器
 * Created by Lidong on 2018/1/5.
 */
public class AlbumFileListAdapter extends ArrayAdapter<AlbumFile> {

    private final static int TYPE_IMAGE = AlbumFile.TYPE_IMAGE;
    private final static int TYPE_VIDEO = AlbumFile.TYPE_VIDEO;
    private final static int TYPE_COUNT = 2;
    private int mItemWidth;

    public AlbumFileListAdapter(@NonNull Context context) {
        super(context, 0);

        int screenWidth = DeviceUtils.getScreenWidth();
        int space = getContext().getResources().getDimensionPixelSize(R.dimen.album_file_list_space) * 2;
        mItemWidth = (screenWidth - space) / 4;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        switch (getItemViewType(position)) {
            case TYPE_IMAGE:
                if (convertView == null) {
                    convertView = new ImageFileListItem(getContext());
                    convertView.setLayoutParams(new ViewGroup.LayoutParams(mItemWidth, mItemWidth));
                }
                ((ImageFileListItem) convertView).setData(getItem(position));
                break;

            case TYPE_VIDEO:
                if (convertView == null) {
                    convertView = new VideoFileListItem(getContext());
                    convertView.setLayoutParams(new ViewGroup.LayoutParams(mItemWidth, mItemWidth));
                }
                ((VideoFileListItem) convertView).setData(getItem(position));
                break;
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).fileType == TYPE_IMAGE ? TYPE_IMAGE : TYPE_VIDEO;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }
}
