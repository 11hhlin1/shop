package com.gjj.shop.photo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;


import com.gjj.shop.R;
import com.gjj.shop.base.BaseMainActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoViewActivity extends Activity implements ViewPager.OnPageChangeListener{

    @Bind(R.id.viewPager)
    HackyViewPager mViewPager;
    
    @Bind(R.id.pageinfo)
    TextView mPageInfoTV;
    
    @OnClick(R.id.root)
    void exit() { 
        finish();
    }
    /**
     * 图片内容
     */
    private ArrayList<PhotoData> mPhotoDataList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initViews();
    }
    
    private void initViews() {
        ButterKnife.bind(this);
        Intent i = getIntent();
        i.setExtrasClassLoader(PhotoData.class.getClassLoader());
        mPhotoDataList = i.getParcelableArrayListExtra("photoDataList");
        int index = i.getIntExtra("index", 0);
        mViewPager.setAdapter(new PhotoPagerAdapter(this, mPhotoDataList));
        mViewPager.setCurrentItem(index);
        mViewPager.setOnPageChangeListener(this);
        mPageInfoTV.setText(getString(R.string.index_of_length, (index + 1), mPhotoDataList.size()));
    }
    

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int pos) {
        mPageInfoTV.setText(getString(R.string.index_of_length, (pos + 1), mPhotoDataList.size()));
        Intent intent = getIntent();
        if (intent != null) {
            intent.putExtra("index", pos);
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//        overridePendingTransition(R.anim.stay, R.anim.fade_out);
//    }
    
}
