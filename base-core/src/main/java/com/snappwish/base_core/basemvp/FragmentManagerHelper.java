package com.snappwish.base_core.basemvp;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by jinjin on 17/6/8.
 */

public class FragmentManagerHelper {

    private FragmentManager mFragmentManager;
    private int mContainerViewId;

    public FragmentManagerHelper(@Nullable FragmentManager fragmentManager, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = containerViewId;
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(mContainerViewId, fragment);
        fragmentTransaction.commit();
    }

    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // 隐藏所有fragment
        List<Fragment> fragments = mFragmentManager.getFragments();
        for (Fragment tempFragment : fragments) {
            fragmentTransaction.hide(tempFragment);
        }

        // 判断容器中是否存在当前fragment
        if (fragments.contains(fragment)) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(mContainerViewId, fragment);
        }

        fragmentTransaction.commit();
    }
}
