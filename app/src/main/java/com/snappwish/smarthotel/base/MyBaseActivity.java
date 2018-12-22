package com.snappwish.smarthotel.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.snappwish.base_core.basemvp.BaseActivity;
import com.snappwish.base_core.dialog.AlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Subscription;

/**
 * Created by jinjin on 2018/12/22.
 * description:
 */

public abstract class MyBaseActivity extends BaseActivity {

//    public CompositeSubscription compositeSubscription = null;
//
//    public void addSubscription(Subscription subscription) {
//        if (compositeSubscription == null) {
//            compositeSubscription = new CompositeSubscription();
//        }
//        compositeSubscription.add(subscription);
//    }

    @Override
    protected AlertDialog getLoadingDialog() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this instanceof IEventBus) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (compositeSubscription != null) {
//            compositeSubscription.unsubscribe();
//        }
        if (this instanceof IEventBus) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }
}
