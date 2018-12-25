package com.snappwish.smarthotel;

import com.snappwish.smarthotel.base.MyBaseFragment;

/**
 * @author lishibo
 * @date 2018/12/25
 * email : andy_li@swift365.com.cn
 */
public class WeatherFragment extends MyBaseFragment {
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_weather;
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
