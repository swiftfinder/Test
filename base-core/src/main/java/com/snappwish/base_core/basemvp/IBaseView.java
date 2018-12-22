package com.snappwish.base_core.basemvp;

import android.content.Context;

/**
 * Created by jinjin on 17/10/31.
 * description: view层基类，定义常用UI更新方法
 */

public interface IBaseView {

    /**
     * 显示正在加载view
     */
    void showLoading();

    /**
     * 隐藏正在加载view
     */
    void hideLoading();

    /**
     * 显示toast
     * @param msg
     */
    void showToast(String msg);

    /**
     * 显示请求错误
     */
    void showError();

    /**
     * 获取当前上下文
     * @return
     */
    Context getContext();

}
