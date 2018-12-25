package com.snappwish.smarthotel;

import com.snappwish.smarthotel.base.MyBaseFragment;

/**
 * Created by jinjin on 2018/12/25.
 * description:
 */

public class MealFragment extends MyBaseFragment {

    public static MealFragment newInstance() {
        MealFragment fragment = new MealFragment();
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_meal;
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
