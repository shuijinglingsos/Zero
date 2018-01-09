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
import com.elf.zerodemo.widget.AlbumFileListItem;

/**
 * 相册文件列表适配器
 * Created by Lidong on 2018/1/5.
 */
public class AlbumFileListAdapter extends ArrayAdapter<AlbumFile> {

    private int mItemWidth;
    private OnItemCheckChangeListener mOnItemCheckChangeListener;

    public AlbumFileListAdapter(@NonNull Context context) {
        super(context, 0);

        int screenWidth = DeviceUtils.getScreenWidth();
        int space = getContext().getResources().getDimensionPixelSize(R.dimen.album_file_list_space) * 2;
        mItemWidth = (screenWidth - space) / 4;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = new AlbumFileListItem(getContext());
            convertView.setLayoutParams(new ViewGroup.LayoutParams(mItemWidth, mItemWidth));
        }

        final AlbumFile albumFile = getItem(position);
        final AlbumFileListItem albumFileListItem = (AlbumFileListItem) convertView;
        albumFileListItem.setData(albumFile);
        albumFileListItem.setChecked(albumFile.checked);
        albumFileListItem.setOnCheckedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemCheckChangeListener != null) {
                    mOnItemCheckChangeListener.onCheckChange(albumFile, !albumFile.checked);
                }
            }
        });

        return convertView;
    }

    public void setOnItemCheckChangeListener(OnItemCheckChangeListener listener) {
        mOnItemCheckChangeListener = listener;
    }

    public interface OnItemCheckChangeListener {
        void onCheckChange(AlbumFile albumFile, boolean checked);
    }
}
