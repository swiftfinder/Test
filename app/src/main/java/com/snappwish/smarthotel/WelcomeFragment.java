package com.snappwish.smarthotel;

import android.widget.TextView;

import com.snappwish.smarthotel.base.MyBaseFragment;
import com.snappwish.smarthotel.util.DateUtils;

import butterknife.BindView;

/**
 * Created by jinjin on 2018/12/24.
 * description:
 */

public class WelcomeFragment extends MyBaseFragment {

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_welcome)
    TextView tvWelcome;
    @BindView(R.id.tv_welcome_bed)
    TextView tvWelcomeBed;
    @BindView(R.id.tv_welcome_time_start)
    TextView tvWelcomeTimeStart;
    @BindView(R.id.tv_welcome_time_end)
    TextView tvWelcomeTimeEnd;

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
        String time = DateUtils.nowTimeDetail3();
        String day = DateUtils.nowTimeDetail2();

        tvTime.setText(time);
        tvDay.setText(day);
    }

    @Override
    protected void destroyData() {

    }
}
