package com.elf.zero.image;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.elf.zero.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择
 */
public class AlbumActivity extends BaseActivity {

    private final static String TAG = AlbumActivity.class.getSimpleName();

    private Handler mHandler = new Handler();
    private boolean mRunAnim = false;
    private View mViewFoldersShade;
    private Button mBtnFolder, mBtnPreview;
    private AbsListView mGvFile, mLvFolder;
    private AlbumFileListAdapter mAlbumFileListAdapter;
    private AlbumFolderListAdapter mAlbumFolderListAdapter;
    private List<AlbumFile> mSelectedFiles = new ArrayList<>();
    private int mSelectedMaxNum = 9;

    private AlbumDataSource mAlbumDataSource;
    private AlbumFolder mShowAlbumFolder;

    private MenuItem mSendMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zero_image_activity_album);
        mSelectedMaxNum = getIntent().getIntExtra("count", 9);
        init();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permission, 1);
            return;
        }
        showData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mSendMenu = menu.add(1, 1, 1, "发送");
        mSendMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == mSendMenu.getItemId()) {
            int count = mSelectedFiles.size();
            if (count > 0) {
                String[] selected = new String[count];
                for (int i = 0; i < count; i++) {
                    selected[i] = mSelectedFiles.get(i).path;
                }
                Intent intent = new Intent();
                intent.putExtra("data", selected);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showToast("请选择图片");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mViewFoldersShade.getVisibility() == View.VISIBLE) {
            onShowFolderList();
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        mViewFoldersShade = findViewById(R.id.view_folders_shade);
        mGvFile = (AbsListView) findViewById(R.id.gv_files);
        mLvFolder = (AbsListView) findViewById(R.id.lv_folders);
        mBtnFolder = (Button) findViewById(R.id.btn_folder);
        mBtnPreview = (Button) findViewById(R.id.btn_preview);

        mBtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowFolderList();
            }
        });
        mBtnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        mViewFoldersShade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowFolderList();
            }
        });
        mGvFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mAlbumFileListAdapter.getCount() > 0) {
//                    List<AlbumFile> paths = new ArrayList<>();
//                    for (int i = 0; i < paths.length; i++) {
//                        paths.add(mAlbumFileListAdapter.getItem(i));
//                    }
                    openGalleryDialog(mShowAlbumFolder.albumFiles, position);
                }
            }
        });
        mLvFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAlbumFolderListAdapter.setSelectedIndex(position);
                mAlbumFolderListAdapter.notifyDataSetChanged();
                showAlbumFiles(mAlbumFolderListAdapter.getItem(position));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onShowFolderList();
                    }
                }, 300);
            }
        });

        mAlbumFileListAdapter = new AlbumFileListAdapter(this);
        mAlbumFileListAdapter.setOnItemCheckChangeListener(new AlbumFileListAdapter.OnItemCheckChangeListener() {
            @Override
            public void onCheckChange(AlbumFile albumFile, boolean checked) {
                if (checked) {
                    if (mSelectedFiles.size() >= mSelectedMaxNum) {
                        showToast("最多选中 " + mSelectedMaxNum + " 项");
                    } else {
                        albumFile.checked = true;
                        mSelectedFiles.add(albumFile);
                        mAlbumFileListAdapter.notifyDataSetChanged();
                        showSelectCount();
                    }
                } else {
                    albumFile.checked = false;
                    mSelectedFiles.remove(albumFile);
                    mAlbumFileListAdapter.notifyDataSetChanged();
                    showSelectCount();
                }
            }
        });
        mGvFile.setAdapter(mAlbumFileListAdapter);
    }

    public void onShowFolderList() {
        if (mRunAnim) {
            return;
        }

        int height = getResources().getDimensionPixelSize(R.dimen.zero_image_folder_list_height);

        if (mViewFoldersShade.getVisibility() == View.VISIBLE) {   //隐藏

            ObjectAnimator bgAnim = ObjectAnimator.ofFloat(mViewFoldersShade, "alpha", 1f, 0f);
            ObjectAnimator listAnim = ObjectAnimator.ofFloat(mLvFolder, "translationY", 0, mLvFolder.getTop() + height);

            bgAnim.setDuration(400);
            listAnim.setDuration(400);

            listAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mRunAnim = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mRunAnim = false;
                    mViewFoldersShade.setVisibility(View.GONE);
                    mLvFolder.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            listAnim.start();
            bgAnim.start();
        } else {    //显示
            ObjectAnimator bgAnim = ObjectAnimator.ofFloat(mViewFoldersShade, "alpha", 0f, 1f);
            ObjectAnimator listAnim = ObjectAnimator.ofFloat(mLvFolder, "translationY", mLvFolder.getTop() + height, 0);

            bgAnim.setDuration(400);
            listAnim.setDuration(400);

            listAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mRunAnim = true;
                    mViewFoldersShade.setVisibility(View.VISIBLE);
                    mLvFolder.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mRunAnim = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            listAnim.start();
            bgAnim.start();
        }
    }

    public void send() {
        if (mSelectedFiles.size() == 0) {
            showToast("未选中项目");
            return;
        }

        List<AlbumFile> list = new ArrayList<>();
        list.addAll(mSelectedFiles);
        openGalleryDialog(list, 0);
    }

    /**
     * 显示数据
     */
    private void showData() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
            return;
        }

        mAlbumDataSource = new AlbumDataSource(this);
        mAlbumDataSource.readImageAndVideo();

        showAlbumFiles(mAlbumDataSource.getFolders().get(0));
        showAlbumFolders();
    }

    /**
     * 显示文件列表
     *
     * @param folder 文件夹
     */
    private void showAlbumFiles(AlbumFolder folder) {
        mShowAlbumFolder = folder;
        mBtnFolder.setText(folder.name);
        mAlbumFileListAdapter.clear();
        mAlbumFileListAdapter.addAll(folder.albumFiles);
        mGvFile.setAdapter(mAlbumFileListAdapter);
    }

    /**
     * 显示文件夹列表
     */
    private void showAlbumFolders() {
        mAlbumFolderListAdapter = new AlbumFolderListAdapter(this);
        mAlbumFolderListAdapter.addAll(mAlbumDataSource.getFolders());
        mLvFolder.setAdapter(mAlbumFolderListAdapter);
    }

    private void openGalleryDialog(List<AlbumFile> paths, int position) {
        GalleryActivity.open(this, paths, mSelectedFiles, mSelectedMaxNum, position, 0);
    }

    private void showSelectCount() {
        int count = mSelectedFiles.size();
        mBtnPreview.setText("预览" + "(" + count + ")");
        if (count > 0) {
            mSendMenu.setTitle("发送" + "(" + count + ")");
        } else {
            mSendMenu.setTitle("发送");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        showData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            mAlbumFileListAdapter.notifyDataSetChanged();
            showSelectCount();
        }
    }

    public static void open(Activity activity, int selectCount, int requestCode) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putExtra("count", selectCount);
        activity.startActivityForResult(intent, requestCode);
    }
}
