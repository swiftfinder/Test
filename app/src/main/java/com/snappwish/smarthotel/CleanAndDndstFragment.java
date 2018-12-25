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
 * Created by jinjin on 2018/12/25.
 * description:
 */

public class CleanAndDndstFragment extends MyBaseFragment {

    @BindView(R.id.iv_temperature)
    ImageView ivTemperature;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.iv_clean)
    ImageView ivClean;
    @BindView(R.id.iv_dndst)
    ImageView ivDndst;

    public static CleanAndDndstFragment newInstance(int status) {
        CleanAndDndstFragment fragment = new CleanAndDndstFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_clean_dndst;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        int status = getArguments().getInt("status");
        switch (status) {
            case 0:
                ivClean.setImageResource(R.drawable.image_clean_close);
                ivDndst.setImageResource(R.drawable.image_dndst_close);
                break;
            case 1:
                ivClean.setImageResource(R.drawable.image_clean_open);
                ivDndst.setImageResource(R.drawable.image_dndst_close);
                break;
            case 2:
                ivClean.setImageResource(R.drawable.image_clean_close);
                ivDndst.setImageResource(R.drawable.image_dndst_open);
                break;
        }
    }

    @Override
    protected void destroyData() {

    }

}
