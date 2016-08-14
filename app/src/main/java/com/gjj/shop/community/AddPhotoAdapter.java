package com.gjj.shop.community;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.gjj.shop.R;
import com.gjj.shop.widget.SquareCenterImageView;

import java.util.ArrayList;

/**
 * Created by Chuck on 2016/7/29.
 */
public class AddPhotoAdapter extends BaseAdapter {
    public static final int MAX_SELECT = 6;
    private Context mContext;
    private ArrayList<String> mList;
    protected LayoutInflater mInflater;
    private static final int VIEW_TYPE_GO_CAMERA = 0;
    private static final int VIEW_TYPE_SELECT_CHILD = 1;
    private SelectPhotoListener mSelectListener;
    public AddPhotoAdapter(Context context, ArrayList<String> list) {
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        if (null == mList) {
            return 1;
        }
        if (mList.size() == MAX_SELECT) {
            return MAX_SELECT;
        }
        return mList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (null == mList) {
            return null;
        }
        if (position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {
        int size = 0;
        if (null != mList) {
            size = mList.size();
        }
        if (position == size && size < MAX_SELECT) {
            return VIEW_TYPE_GO_CAMERA;
        } else {
            return VIEW_TYPE_SELECT_CHILD;
        }
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_GO_CAMERA) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.view_add_photo, null);
                convertView.setOnClickListener(mPhotoAddClick);
            }
            return convertView;
        } else {
            final String path = mList.get(position);
            final ViewHolder viewHolder;
            if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
                convertView = mInflater.inflate(R.layout.albums_photo_btn, null);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (SquareCenterImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Glide.with(mContext)
                    .load(path)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.mImageView);
            convertView.setTag(R.layout.albums_photo_btn, path);

            return convertView;
        }
    }
    private View.OnClickListener mPhotoAddClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mSelectListener.showDialog();
        }
    };
    public void setSelectPhotoListener(SelectPhotoListener listener) {
        this.mSelectListener = listener;
    }

    public static class ViewHolder {
        public SquareCenterImageView mImageView;
    }

    public interface SelectPhotoListener {
        void showDialog();
//        void doTakePhoto();
//        void doPickPhotoFromGallery();
    }
}
