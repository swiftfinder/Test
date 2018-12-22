package com.snappwish.base_core.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jinjin on 17/5/14.
 */

class AlertController {

    private AlertDialog mDialog;
    private Window mWindow;
    private DialogViewHelper helper;

    public AlertController(AlertDialog alertDialog, Window window) {
        this.mDialog = alertDialog;
        this.mWindow = window;
    }

    public void setHelper(DialogViewHelper helper) {
        this.helper = helper;
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public void setText(int viewId, CharSequence text) {
        helper.setText(viewId, text);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        helper.setOnClickListener(viewId, listener);
    }

    public <T extends View> T getView(int viewId) {
        return helper.getView(viewId);
    }

    public void setImage(int viewId, int imageResourceId) {
        helper.setImage(viewId, imageResourceId);
    }

    public void setImage(int viewId, String url) {
        helper.setImage(viewId, url);
    }

    public void settextColor(int viewId, int colorResoueceId) {
        helper.setTextColor(viewId, colorResoueceId);
    }

    public static class AlertParams {

        public Context mContext;
        public int mThemeResId;
        public boolean mCancelable = true;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        // 布局
        public View mView;
        // 布局id
        public int mContentLayoutResId;
        // 存放设置文本
        SparseArray<CharSequence> textArray = new SparseArray<>();
        // 存放设置监听
        public SparseArray<View.OnClickListener> clickArray = new SparseArray<>();
        // 宽高
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 动画
        public int mAnimations = 0;
        // 默认中间
        public int mGravity = Gravity.CENTER;
        // 存放imageResource
        public SparseArray<Integer> imageResource = new SparseArray<>();
        // 存放imageUrl
        public SparseArray<String> imageUrl = new SparseArray<>();
        // 存放文字颜色
        public SparseArray<Integer> textColorArray = new SparseArray<>();

        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         *
         * @param mAlert
         */
        public void apply(AlertController mAlert) {
            // 设置布局
            DialogViewHelper helper = null;
            if (mContentLayoutResId != 0) {
                helper = new DialogViewHelper(mContext, mContentLayoutResId);
            } else if (mView != null) {
                helper = new DialogViewHelper();
                helper.setContentView(mView);
            } else {
                throw new IllegalArgumentException("you need to use setContentView() before everything you want to do!");
            }
            mAlert.setHelper(helper);

            // 将布局设置给dialog
            mAlert.getDialog().setContentView(helper.getContentView());

            // 设置text
            int textSize = textArray.size();
            if (textSize > 0) {
                for (int i = 0; i < textSize; i++) {
                    helper.setText(textArray.keyAt(i), textArray.valueAt(i));
                }
            }

            int textColorSize = textColorArray.size();
            if (textColorSize > 0) {
                for (int i = 0; i < textColorSize; i++) {
                    helper.setTextColor(textColorArray.keyAt(i), textColorArray.valueAt(i));
                }
            }

            // 设置imageResource
            int imageResourceSize = imageResource.size();
            if (imageResourceSize > 0) {
                for (int i = 0; i < imageResourceSize; i++) {
                    helper.setImage(imageResource.keyAt(i), imageResource.valueAt(i));
                }
            }

            // 设置imageUrl
            int imageUrlSize = imageUrl.size();
            if (imageUrlSize > 0) {
                for (int i = 0; i < imageUrlSize; i++) {
                    helper.setImage(imageUrl.keyAt(i), imageUrl.valueAt(i));
                }
            }

            // 设置点击
            int clickSize = clickArray.size();
            if (clickSize > 0) {
                for (int i = 0; i < clickSize; i++) {
                    helper.setOnClickListener(clickArray.keyAt(i), clickArray.valueAt(i));
                }
            }

            // 设置其他监听

            // 设置自定义效果(全屏,从底部弹出,默认动画...)
            Window window = mAlert.getWindow();
            window.setGravity(mGravity);
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
