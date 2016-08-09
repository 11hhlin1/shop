package com.gjj.shop.community;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.StringDialogCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.event.EventOfAddPhoto;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.widget.UnScrollableGridView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private  String DCIM = Environment.getExternalStorageDirectory() + "/"
            + Environment.DIRECTORY_DCIM + "/Camera/";
    private  File rootDir = new File(DCIM);

    @Bind(R.id.desc_tv)
    EditText mDesc;

    @Bind(R.id.albums_grid)
    UnScrollableGridView mGridView;

    private ArrayList<String> mList;
    private AddPhotoAdapter mAdapter;
    private String mPhotoUrl;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_add_feed;
    }

    @Override
    public void onRightBtnClick() {
        super.onRightBtnClick();
        if(TextUtils.isEmpty(mDesc.getText().toString())){
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("desc", mDesc.getText().toString());
        List<File> fileList = new ArrayList<>();
        for (String path: mList){
           fileList.add(new File(path));
        }

//        final JSONObject jsonObject = new JSONObject(params);
        OkHttpUtils.post(ApiConstants.COMMUNITY_PUBLISH)//
                .tag(this)//
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .addFileParams("imageList", fileList)
                .execute(new StringDialogCallback(getActivity()) {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        ToastUtil.shortToast(R.string.commit_success);
                    }
                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        if(response != null) L.d("@@@@@>>", response.code());
                    }

                });
//                .postJson(jsonObject.toString())//
//                .execute(new StringDialogCallback {
//                    @Override
//                    public void onResponse(boolean isFromCache, UserInfo rspInfo, Request request, @Nullable Response response) {
//                    }
//                    @Override
//                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
//                        if(response != null) L.d("@@@@@>>", response.code());
//                    }
//                });code
    }

    @Override
    public void initView() {
        mList = new ArrayList<>();
        mAdapter = new AddPhotoAdapter(getActivity(), mList);
        mGridView.setAdapter(mAdapter);
        mAdapter.setSelectPhotoListener(this);
//        EventBus.getDefault().register(this);
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
        if(getActivity() == null) {
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
            if(!mList.contains(mPhotoUrl))
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
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
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
            if(!mList.contains(picturePath))
                mList.add(picturePath);
            mAdapter.notifyDataSetChanged();
        }

    }
}
