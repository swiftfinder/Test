package com.snappwish.base_core.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jinjin on 17/5/14.
 */

public abstract class AbsNavigationBar <P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;
    private View mNavigationView;

    public AbsNavigationBar(P mParams) {
        this.mParams = mParams;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    public View getNavigationView() {
        return mNavigationView;
    }

    public <T extends View> T findViewById(int viewId) {
        return (T) mNavigationView.findViewById(viewId);
    }

    /**
     * 创建并绑定view
     */
    private void createAndBindView() {
        // 创建View

        if (mParams.mParent == null) {
            ViewGroup viewGroup =
                    (ViewGroup) ((Activity) (mParams.mContext)).getWindow().getDecorView();
            mParams.mParent = (ViewGroup) viewGroup.getChildAt(0);
        }

        if (mParams.mParent == null) {
            return;
        }

        mNavigationView = LayoutInflater.from(mParams.mContext)
                .inflate(bindLayoutId(), mParams.mParent, false);
        // 添加
        mParams.mParent.addView(mNavigationView, 0);
        applyView();
    }

    public static abstract class Builder{

        AbsNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            P = new AbsNavigationParams(context, parent);
        }

        public abstract AbsNavigationBar builder();

        public static class AbsNavigationParams{

            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
