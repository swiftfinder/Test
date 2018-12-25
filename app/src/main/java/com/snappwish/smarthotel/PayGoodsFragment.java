package com.snappwish.smarthotel;

import com.snappwish.smarthotel.base.MyBaseFragment;

/**
 * Created by jinjin on 2018/12/25.
 * description:
 */

public class PayGoodsFragment extends MyBaseFragment {

    public static PayGoodsFragment newInstance() {
        PayGoodsFragment fragment = new PayGoodsFragment();
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_pay_goods;
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
