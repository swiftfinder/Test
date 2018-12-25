package com.snappwish.smarthotel;

import android.os.Bundle;

import com.snappwish.smarthotel.base.MyBaseFragment;

/**
 * @author lishibo
 * @date 2018/12/25
 * email : andy_li@swift365.com.cn
 */
public class UnsubscribeFragment extends MyBaseFragment {
    public static UnsubscribeFragment newInstance() {

        Bundle args = new Bundle();

        UnsubscribeFragment fragment = new UnsubscribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_unsubscribe;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void destroyData() {

    }
}
