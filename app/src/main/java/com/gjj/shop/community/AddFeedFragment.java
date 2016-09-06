package com.gjj.shop.community;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gjj.applibrary.http.callback.CommonCallback;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.StringDialogCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.task.ForegroundTaskExecutor;
import com.gjj.applibrary.task.MainTaskExecutor;
import com.gjj.applibrary.util.ImageCompress;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.event.EventOfAddPhoto;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.widget.UnScrollableGridView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.request.BaseRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Chuck on 2016/7/29.
 */
public class AddFeedFragment extends BaseFragment implements AddPhotoAdapter.SelectPhotoListener {

    public static final int GET_PHOTO_CODE = 2;
    public static final int GET_CAMERA_CODE = 3;

    private String DCIM = Environment.getExternalStorageDirectory() + "/"
            + Environment.DIRECTORY_DCIM + "/Camera/";
    private File rootDir = new File(DCIM);

    @Bind(R.id.desc_tv)
    EditText mDesc;

    @Bind(R.id.albums_grid)
    UnScrollableGridView mGridView;

    private ArrayList<String> mList;
    private AddPhotoAdapter mAdapter;
    private String mPhotoUrl;
    private PopupWindow mPickUpPopWindow;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_add_feed;
    }

    @Override
    public void onRightBtnClick() {
        super.onRightBtnClick();
        if (TextUtils.isEmpty(mDesc.getText().toString())) {
            return;
        }
        if (Util.isListEmpty(mList)) {
            return;
        }
        ForegroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog(R.string.committing, false);
                final HashMap<String, String> params = new HashMap<>();
                params.put("details", mDesc.getText().toString());
                final List<File> fileList = new ArrayList<>();
                for (String path : mList) {
                    File file = compressImage(path);
                    L.d("@@@@@" + file.getAbsolutePath());
                    fileList.add(file);
                }
                OkHttpUtils.post(ApiConstants.COMMUNITY_PUBLISH)//
                        .tag(AddFeedFragment.this)//
                        .cacheMode(CacheMode.NO_CACHE)
                        .params(params)
                        .addFileParams("imageList", fileList)
                        .execute(new JsonCallback<String>(String.class) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        ToastUtil.shortToast(R.string.commit_success);
                                        onBackPressed();

                                    }
                                });
                            }
                        });


            }
        });

    }

    /**
     * 图片压缩
     *
     * @return
     */
    private File compressImage(String fileUrl) {
        ImageCompress compress = new ImageCompress();
        ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
        options.uri = fileUrl;
        StringBuilder filename = Util.getThreadSafeStringBuilder();
        filename.append(Util.getDirectory().getAbsoluteFile()).append(getPhotoName(System.currentTimeMillis() / 1000));
        options.destFile = new File(filename.toString());
//        options.fileKey = this.fileKey;
//        options.isUploadTest = mIsUploadTest;
        compress.compressImageFile(options);
        return options.destFile;
    }

    @Override
    public void initView() {
        mList = new ArrayList<>();
        mAdapter = new AddPhotoAdapter(getActivity(), mList);
        mGridView.setAdapter(mAdapter);
        mAdapter.setSelectPhotoListener(this);
    }

//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void addPhoto(EventOfAddPhoto event) {
//        if(event.mType == GET_CAMERA_CODE) {
//            if(!mList.contains(mPhotoUrl))
//                mList.add(mPhotoUrl);
//        } else {
//            if(!mList.contains(event.mPhotoUrl))
//            mList.add(event.mPhotoUrl);
//        }
//        mAdapter.notifyDataSetChanged();
//    }

    /**
     * 取消工程消息弹出框
     *
     * @return
     */
    private void dismissConstructNoticeWindow() {
        PopupWindow pickUpPopWindow = mPickUpPopWindow;
        if (null != pickUpPopWindow && pickUpPopWindow.isShowing()) {
            pickUpPopWindow.dismiss();
        }
    }

    public void doPickPhotoFromGallery() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        getActivity().startActivityForResult(intent, AddFeedFragment.GET_PHOTO_CODE);
    }

    public void doTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = getPhotoName(System.currentTimeMillis() / 1000);
        File file = new File(rootDir, fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        mPhotoUrl = file.getAbsolutePath();
        getActivity().startActivityForResult(intent, AddFeedFragment.GET_CAMERA_CODE);
    }

    /**
     * 照片保存名字
     *
     * @param rq
     * @return
     */
    private String getPhotoName(long rq) {
        return "IMG_" + rq + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() == null) {
            return;
        }
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        if (requestCode == AddFeedFragment.GET_PHOTO_CODE) {
            if (data != null) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    sendPicByUri(selectedImage);
                }
            }
        } else if (requestCode == AddFeedFragment.GET_CAMERA_CODE) {
            if (!mList.contains(mPhotoUrl))
                mList.add(mPhotoUrl);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;
            if (picturePath == null || picturePath.equals("null")) {
                return;
            }
            if (!mList.contains(picturePath))
                mList.add(picturePath);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showDialog() {
        View contentView;
        PopupWindow popupWindow = mPickUpPopWindow;
        if (popupWindow == null) {
            contentView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.choose_pic_pop, null);
            TextView takePhoto = (TextView) contentView.findViewById(R.id.take_photo);
            TextView takeGallery = (TextView) contentView.findViewById(R.id.take_gallery);
            TextView cancle = (TextView) contentView.findViewById(R.id.btn_cancel);
            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doTakePhoto();
                    dismissConstructNoticeWindow();
                }
            });
            takeGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doPickPhotoFromGallery();
                    dismissConstructNoticeWindow();
                }
            });
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissConstructNoticeWindow();
                }
            });
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissConstructNoticeWindow();
                }
            });
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false);
            mPickUpPopWindow = popupWindow;
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
            mPickUpPopWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

        } else {
            contentView = popupWindow.getContentView();
        }
        //判读window是否显示，消失了就执行动画
        if (!popupWindow.isShowing()) {
            Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.effect_bg_show);
            contentView.startAnimation(animation2);
        }

        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }
}
