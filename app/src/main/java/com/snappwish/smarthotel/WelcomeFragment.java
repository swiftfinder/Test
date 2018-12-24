package com.snappwish.smarthotel;

import com.snappwish.smarthotel.base.MyBaseFragment;

/**
 * Created by jinjin on 2018/12/24.
 * description:
 */

public class WelcomeFragment extends MyBaseFragment {

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_welcome;
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
