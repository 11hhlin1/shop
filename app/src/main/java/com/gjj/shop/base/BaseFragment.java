package com.gjj.shop.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gjj.applibrary.task.MainTaskExecutor;
import com.gjj.shop.widget.CustomProgressDialog;
import com.lzy.okhttputils.OkHttpUtils;


/**
 * chuck
 */
public abstract class BaseFragment extends Fragment {
    private CustomProgressDialog mLoadingDialog;
    /**
     * 清除所有fragment回退栈
     */
    public static final String FLAT_CLEAR_TOP_FRAGMENT = "clear_top_fragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayout(), container, false);
        return view;
    }

    public abstract int getContentViewLayout();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //根据 Tag 取消请求
        OkHttpUtils.getInstance().cancelTag(this);
    }

    /**
     * 展示加载中对话框
     */
    protected void showLoadingDialog(int tipResId, boolean cancelable) {
        CustomProgressDialog loadingDialog = mLoadingDialog;
        if (null == loadingDialog) {
            loadingDialog = new CustomProgressDialog(getActivity());
            mLoadingDialog = loadingDialog;
            loadingDialog.setTipText(tipResId);
            loadingDialog.setCancelable(cancelable);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //TODO
                    onBackPressed();
                }
            });
        }
        loadingDialog.show();
    }

    public void onBackPressed() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.onBackPressed();
        }
    }
    /**
     * 关闭对话框
     */
    protected void dismissLoadingDialog() {
        CustomProgressDialog loadingDialog = mLoadingDialog;
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    public void onRightBtnClick() {

    }

    public void onTitleBtnClick() {

    }

    public void handleArgs(Bundle bundle) {

    }


    public void runOnUiThread(Runnable action) {
        runOnUiThread(action, 0);
    }

    public void runOnUiThread(final Runnable action, long delay) {
        if (delay < 0f) {
            throw new RuntimeException("delay must > 0");
        } else {
            if (delay == 0f && Looper.myLooper() == Looper.getMainLooper()) {
                if (getActivity() == null) {
                    return;
                }
                action.run();
            } else {
                Runnable wrapper = new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() == null) {
                            return;
                        }
                        action.run();
                    }
                };
                if (delay > 0) {
                    MainTaskExecutor.scheduleTaskOnUiThread(delay, wrapper);
                } else {
                    MainTaskExecutor.runTaskOnUiThread(wrapper);
                }
            }
        }
    }

    public boolean goBack(boolean fromKeyboard) {
        return false;
    }
}
