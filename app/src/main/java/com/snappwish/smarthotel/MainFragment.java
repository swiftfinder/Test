package com.snappwish.smarthotel;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.snappwish.smarthotel.base.MyBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jinjin on 2018/12/24.
 * description:
 */

public class MainFragment extends MyBaseFragment {

    @BindView(R.id.iv_temperature)
    ImageView ivTemperature;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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

    @OnClick(R.id.iv_logo)
    public void logoClick() {
        ((MainActivity) getActivity()).chooseFragment(Constant.FRAGMENT_WELCOME);
        ((MainActivity) getActivity()).startSpeak(getString(R.string.speak_welcome));
//        WakeupManager.getInstance(this.getContext()).destroyWakeup();
        ((MainActivity) getActivity()).setWelcome(true);
    }

}
