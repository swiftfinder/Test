package com.snappwish.base_core.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

/**
 * Created by jinjin on 17/5/14.
 */

class DialogViewHelper {

    private View mContentView = null;
    private SparseArray<WeakReference<View>> mViews;
    private Context mContent;

    public DialogViewHelper(Context mContext, int mContentLayoutResId) {
        mViews = new SparseArray<>();
        mContent = mContext;
        this.mContentView = LayoutInflater.from(mContext).inflate(mContentLayoutResId, null);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public void setContentView(View mView) {
        this.mContentView = mView;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    public void setTextColor(int viewId, Integer textColorId) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setTextColor(textColorId);
        }
    }

    public void setImage(int viewId, Integer resourceId) {
        ImageView iv = getView(viewId);
        if (iv != null) {
            iv.setImageResource(resourceId);
        }
    }

    public void setImage(int viewId, String url) {
        ImageView iv = getView(viewId);
        if (iv != null) {
            Glide.with(mContent).load(url).into(iv);
        }
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        } else {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<View>(view));
            }
        }
        return (T) view;
    }
}
