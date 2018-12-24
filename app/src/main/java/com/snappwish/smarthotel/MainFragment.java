package com.snappwish.smarthotel;

import android.os.Bundle;

import com.snappwish.smarthotel.base.MyBaseFragment;

/**
 * Created by jinjin on 2018/12/24.
 * description:
 */

public class MainFragment extends MyBaseFragment {

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("flag", item);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_main;
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
