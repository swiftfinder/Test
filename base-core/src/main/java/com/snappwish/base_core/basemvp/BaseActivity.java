package com.snappwish.base_core.basemvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.snappwish.base_core.dialog.AlertDialog;
import com.snappwish.base_core.statusbar.StatusBarFontHelper;

import butterknife.ButterKnife;


/**
 * Created by jinjin on 17/10/31.
 * description:
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        if (hideScreen()) {
//            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
//        StatusBarFontHelper.setStatusBarMode(this, true);
        ButterKnife.bind(this);
        loadingDialog = getLoadingDialog();
        initTitle();
        initView();
        initData();
    }

    protected boolean hideScreen() {
        return true;
    }

    @Override
    protected void onDestroy() {
        destroyData();
        super.onDestroy();
    }

    protected abstract AlertDialog getLoadingDialog();

    protected abstract int getContentView();

    protected abstract void initTitle();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void destroyData();

    @Override
    public void showLoading() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (isTaskRoot()) {
                moveTaskToBack(false);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);

            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
