package com.snappwish.smarthotel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snappwish.smarthotel.base.MyBaseFragment;
import com.snappwish.smarthotel.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jinjin on 2018/12/24.
 * description:
 */

public class MainFragment extends MyBaseFragment {

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.iv_temperature)
    ImageView ivTemperature;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;

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
        String time = DateUtils.nowTimeDetail3();
        String day = DateUtils.nowTimeDetail2();

        tvTime.setText(time);
        tvDay.setText(day);
    }

    @Override
    protected void destroyData() {

    }
}
