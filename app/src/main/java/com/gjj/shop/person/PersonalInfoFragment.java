package com.gjj.shop.person;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjj.applibrary.app.AppLib;
import com.gjj.applibrary.glide.GlideCircleTransform;
import com.gjj.applibrary.http.callback.JsonCallback;
import com.gjj.applibrary.http.callback.StringDialogCallback;
import com.gjj.applibrary.log.L;
import com.gjj.applibrary.task.ForegroundTaskExecutor;
import com.gjj.applibrary.util.ImageCompress;
import com.gjj.applibrary.util.ToastUtil;
import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.address.AddressFragment;
import com.gjj.shop.app.BaseApplication;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.base.PageSwitcher;
import com.gjj.shop.community.AddFeedFragment;
import com.gjj.shop.event.UpdateUserInfo;
import com.gjj.shop.login.LoginActivity;
import com.gjj.shop.model.UserInfo;
import com.gjj.shop.net.ApiConstants;
import com.gjj.shop.net.UrlUtil;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/14.
 */
public class PersonalInfoFragment extends BaseFragment {

    @Bind(R.id.avatar_item)
    RelativeLayout avatarItem;
    @Bind(R.id.name_item)
    RelativeLayout nameItem;
    @Bind(R.id.address_item)
    RelativeLayout addressItem;
    @Bind(R.id.change_psw_item)
    RelativeLayout changePswItem;
    @Bind(R.id.logout_btn)
    Button logoutBtn;
    @Bind(R.id.name_tv)
    TextView nameTV;
    @Bind(R.id.phone_tv)
    TextView phoneTV;
    @Bind(R.id.avatar)
    ImageView avatarIv;

    private PopupWindow mPickUpPopWindow;
    private  String DCIM = Environment.getExternalStorageDirectory() + "/"
            + Environment.DIRECTORY_DCIM + "/Camera/";
    private  File rootDir = new File(DCIM);
    private String mPhotoUrl;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_person_info;
    }

    @Override
    public void initView() {
        UserInfo userInfo = BaseApplication.getUserMgr().getUser();
        if(userInfo != null) {
            Glide.with(getActivity())
                    .load(UrlUtil.getHttpUrl(userInfo.getAvatar()))
                    .centerCrop()
                    .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                    .bitmapTransform(new GlideCircleTransform(getActivity()))
                    .into(avatarIv);
            nameTV.setText(userInfo.nickname);
            phoneTV.setText(userInfo.getPhone());
        }
     EventBus.getDefault().register(this);
    }

    @OnClick({R.id.avatar_item, R.id.name_item, R.id.address_item, R.id.change_psw_item,R.id.logout_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar_item:
                showPickupWindow();
                break;
            case R.id.name_item:
                PageSwitcher.switchToTopNavPage(getActivity(),ChangeNameFragment.class,null,getString(R.string.change_name),getString(R.string.save));
                break;
            case R.id.address_item:
                PageSwitcher.switchToTopNavPage(getActivity(),AddressFragment.class, null, getString(R.string.address), "");
                break;
            case R.id.change_psw_item:
                PageSwitcher.switchToTopNavPage(getActivity(),ChangePswFragment.class,null,getString(R.string.change_psw),null);
                break;
            case R.id.logout_btn:
                OkHttpUtils.post(ApiConstants.LOGOUT)
                        .tag(this)//
                        .cacheMode(CacheMode.NO_CACHE)
                        .execute(new JsonCallback<String>(String.class) {
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        L.d("@@@@@" + "logout fail");
                                        ToastUtil.shortToast(R.string.fail);
                                    }
                                });
                            }

                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        L.d("@@@@@" + "logout success");
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        BaseApplication.getUserMgr().logOut();
                                    }
                                });
                            }
                        });
                break;
        }
    }
    /**
     * 显示选择框
     */
    @SuppressWarnings("unused")
    private void showPickupWindow() {
        // dismissConstructNoticeWindow();
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
        // mPopWindow.showAsDropDown(view, 0, 0);
    }
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
//            if(!mList.contains(mPhotoUrl))
//                mList.add(mPhotoUrl);
//            mAdapter.notifyDataSetChanged();
            setAvatar();
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
            mPhotoUrl = picturePath;
            setAvatar();
//            if(!mList.contains(picturePath))
//                mList.add(picturePath);
//            mAdapter.notifyDataSetChanged();
        }

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
        filename.append(Util.getDirectory().getAbsoluteFile()).append(getPhotoName(System.currentTimeMillis() / 1000)) ;
        options.destFile = new File(filename.toString());
//        options.fileKey = this.fileKey;
//        options.isUploadTest = mIsUploadTest;
        compress.compressImageFile(options);
        return options.destFile;
    }

    void setAvatar() {
//        Glide.with(getActivity())
//                .load(mPhotoUrl)
//                .centerCrop()
//                .placeholder(R.mipmap.all_img_dot_pr)
//                .error(R.mipmap.all_img_dot_pr)
//                .bitmapTransform(new GlideCircleTransform(getActivity()))
//                .into(avatarIv);
        showLoadingDialog(R.string.committing,true);
        ForegroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                final File file = compressImage(mPhotoUrl);
//                HashMap<String, String> params = new HashMap<>();
//                params.put("nickname", "");
                List<File> fileList = new ArrayList<>();
                fileList.add(file);

                OkHttpUtils.post(ApiConstants.UPDATE_USER_INFO)//
                        .tag(this)//
                        .cacheMode(CacheMode.NO_CACHE)
//                        .params(params)
                        .addFileParams("avatar", fileList)
                        .execute(new JsonCallback<UserInfo>(UserInfo.class) {
                            @Override
                            public void onError(Call call, final Response response, Exception e) {
                                super.onError(call, response, e);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        if(response != null)
                                            L.d("@@@@@>>" + response.code());
                                        ToastUtil.shortToast(R.string.fail);
                                    }
                                });
                            }

                            @Override
                            public void onSuccess(final UserInfo userInfo, Call call, Response response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        ToastUtil.shortToast(R.string.commit_success);
                                        BaseApplication.getUserMgr().saveUserInfo(userInfo);
                                        EventBus.getDefault().post(new UpdateUserInfo());
                                    }
                                });
                            }

                        });

            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setUserInfo(UpdateUserInfo event) {
        UserInfo userInfo = BaseApplication.getUserMgr().getUser();
        Glide.with(getActivity())
                .load(UrlUtil.getHttpUrl(userInfo.avatar))
                .centerCrop()
                .error(new ColorDrawable(AppLib.getResources().getColor(android.R.color.transparent)))
                .bitmapTransform(new GlideCircleTransform(getActivity()))
                .into(avatarIv);
        nameTV.setText(userInfo.nickname);
    }
}
