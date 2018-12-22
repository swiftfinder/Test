package com.snappwish.base_core.basemvp;

/**
 * Created by jinjin on 17/10/31.
 * description: presenter层基类，定义通用方法
 */

public class BasePresenter<V extends IBaseView> {

    private V view;

    /**
     * 绑定view
     * @param view
     */
    public void attachView(V view) {
        this.view = view;
    }

    /**
     * 解除view
     */
    public void detachView() {
        this.view = null;
    }

    /**
     * 判断view是否存在
     * @return
     */
    public boolean isViewAttached() {
        return this.view != null;
    }

    /**
     * 获取view
     * @return
     */
    public V getView() {
        return view;
    }

}
