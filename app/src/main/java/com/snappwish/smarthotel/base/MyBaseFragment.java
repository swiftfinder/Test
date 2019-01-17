package com.snappwish.smarthotel.base;

import com.snappwish.base_core.basemvp.BaseFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jinjin on 2018/6/14.
 * description:
 */

public abstract class MyBaseFragment extends BaseFragment {

    @Override
    public void onStart() {
        super.onStart();
        if (this instanceof IEventBus) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

}
