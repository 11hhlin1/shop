package com.gjj.shop.shopping;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gjj.applibrary.util.Util;
import com.gjj.shop.R;
import com.gjj.shop.base.BaseFragment;
import com.gjj.shop.widget.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Chuck on 2016/7/21.
 */
public class ShoppingFragment extends BaseFragment implements ShoppingAdapter.SelectListener{
    @Bind(R.id.tv_title)
    TextView mTitleTV;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.edit_shop)
    TextView mEditTv;

    @Bind(R.id.state_btn)
    TextView mStateTv;

    private ConfirmDialog mConfirmDialog;
    private List<String> mShopIDList;

    @OnClick(R.id.edit_shop)
    void editShop() {
        if(isEdit) {
            mEditTv.setText(getString(R.string.edit));
            mStateTv.setText(getString(R.string.balance));
            isEdit = false;
        } else {
            isEdit = true;
            mEditTv.setText(getString(R.string.done));
            mStateTv.setText(getString(R.string.delete));
        }
    }

    @OnClick(R.id.state_btn)
    void handleState() {
        if(isEdit) {
            dismissConfirmDialog();
            ConfirmDialog confirmDialog = new ConfirmDialog(getActivity(),R.style.white_bg_dialog);
            mConfirmDialog = confirmDialog;
            confirmDialog.setConfirmClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            confirmDialog.setCanceledOnTouchOutside(true);
            confirmDialog.show();
            confirmDialog.setContent(R.string.delete_shop_tip);
        } else {
            //TODO 结算
        }
    }

    @Bind(R.id.all_sel_box)
    CheckBox mAllSel;

    private ShoppingAdapter mAdapter;
    private boolean isEdit = false;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_shopping;
    }

    @Override
    public void initView() {
        mTitleTV.setText(R.string.shopping);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mShopIDList = new ArrayList<>();
        // 设置布局管理器
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ShoppingAdapter(getActivity(), new ArrayList<ShopAdapterInfo>());
        mAdapter.setmSelectListener(this);
        mRecyclerView.setAdapter(mAdapter);


        mAllSel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    List<ShopAdapterInfo> list = mAdapter.getDataList();
                    if(!Util.isListEmpty(list)) {
                        for (ShopAdapterInfo info : list) {
                            info.isSel = true;
                            mShopIDList.add(info.id);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    mShopIDList.clear();
                }
            }
        });
    }

    /**
     * dismiss确认对话框
     */
    private void dismissConfirmDialog() {
        ConfirmDialog confirmDialog = mConfirmDialog;
        if (null != confirmDialog && confirmDialog.isShowing()) {
            confirmDialog.dismiss();
            mConfirmDialog = null;
        }
    }

    @Override
    public void select(String id) {
        mShopIDList.add(id);
    }

    @Override
    public void unSel(String id) {
        mShopIDList.remove(id);
    }
}
