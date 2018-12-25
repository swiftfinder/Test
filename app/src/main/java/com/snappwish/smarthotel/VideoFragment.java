package com.snappwish.smarthotel;

import com.snappwish.smarthotel.base.MyBaseFragment;

/**
 * Created by jinjin on 2018/12/25.
 * description:
 */

public class VideoFragment extends MyBaseFragment {

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_video;
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
