package com.snappwish.base_core.basemvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jinjin on 17/11/6.
 * description:
 */

public abstract class BaseFragment extends Fragment implements IBaseView {

    protected Context mContext;
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(getContentView(), container, false);
        bind = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        initView();
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resumeView();
        } else {
            pauseView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeView();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseView();
    }

    protected void pauseView() {

    }

    protected void resumeView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyData();
        bind.unbind();
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void destroyData();

    @Override
    public void showLoading() {
        checkActivityAttached();
        ((BaseActivity) mContext).showLoading();
    }

    @Override
    public void hideLoading() {
        checkActivityAttached();
        ((BaseActivity) mContext).hideLoading();
    }

    @Override
    public void showToast(String msg) {
        checkActivityAttached();
        ((BaseActivity) mContext).showToast(msg);
    }

    @Override
    public void showError() {
        checkActivityAttached();
        ((BaseActivity) mContext).showError();
    }

    public void checkActivityAttached() {
        if (getActivity() == null) {
            throw new RuntimeException(getActivity().getClass().getSimpleName() + "is not attached!");
        }
    }


}
