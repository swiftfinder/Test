package com.snappwish.base_core.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.snappwish.base_core.R;

/**
 * Created by jinjin on 17/5/14.
 */

public class AlertDialog extends Dialog {

    private AlertController mAlert;

    public AlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        mAlert = new AlertController(this, getWindow());
    }

    public void setText(int viewId, CharSequence text) {
        mAlert.setText(viewId, text);
    }

    public void setImage(int viewId, int imageResourceId){
        mAlert.setImage(viewId, imageResourceId);
    }

    public void setImage(int viewId, String url) {
        mAlert.setImage(viewId, url);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnClickListener(viewId, listener);
    }

    public void setVisibility(int viewId, int visibility) {
        mAlert.getView(viewId).setVisibility(visibility);
    }

    public <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);
    }

    public void setTextColor(int viewId, int colorResoueceId) {
        mAlert.settextColor(viewId, colorResoueceId);
    }

    public static class Builder {

        private final AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        public Builder setContentView(int contentViewId) {
            P.mView = null;
            P.mContentLayoutResId = contentViewId;
            return this;
        }

        public Builder setContentView(View view) {
            P.mView = view;
            P.mContentLayoutResId = 0;
            return this;
        }

        public Builder setText(int viewId, CharSequence text) {
            P.textArray.put(viewId, text);
            return this;
        }

        public Builder setImage(int viewId, int imageResourceId) {
            P.imageResource.put(viewId, imageResourceId);
            return this;
        }

        public Builder setImage(int viewId, String url) {
            P.imageUrl.put(viewId, url);
            return this;
        }

        public Builder setTextColor(int viewId, int colorResourceId) {
            P.textColorArray.put(viewId, colorResourceId);
            return this;
        }

        public Builder setOnClickListener(int viewId, View.OnClickListener listener) {
            P.clickArray.put(viewId, listener);
            return this;
        }

        // 其他设置参数
        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimations = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        public Builder addDefaultAnimation() {
            P.mAnimations = R.style.dialog_scale_anim;
            return this;
        }

        public Builder setAnimations(int styleId) {
            P.mAnimations = styleId;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        public AlertDialog create() {
            final AlertDialog dialog = new AlertDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
