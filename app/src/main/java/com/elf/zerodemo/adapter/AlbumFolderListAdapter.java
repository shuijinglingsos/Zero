package com.elf.zerodemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.elf.zerodemo.model.AlbumFolder;
import com.elf.zerodemo.widget.AlbumFolderListItem;

/**
 * 相册文件加列表适配器
 * Created by Lidong on 2018/1/5.
 */
public class AlbumFolderListAdapter extends ArrayAdapter<AlbumFolder> {

    private int mSelectedIndex = 0;

    public AlbumFolderListAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = new AlbumFolderListItem(getContext());
        }

        AlbumFolderListItem folderItem = (AlbumFolderListItem) convertView;
        folderItem.setData(getItem(position));
        folderItem.setSelected(mSelectedIndex == position);
        return convertView;
    }

    public void setSelectedIndex(int index) {
        mSelectedIndex = index;
    }
}
