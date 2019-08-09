/*
 * Copyright 2016 czy1121
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yhms.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;


public class LoadingLayout extends FrameLayout {
    private int mEmptyImage;
    private CharSequence mEmptyText;

    private int mErrorImage;
    private CharSequence mErrorText, mRetryText;

    private OnInflateListener mOnEmptyInflateListener;
    private OnInflateListener mOnErrorInflateListener;

    private int mTextColor, mTextSize;
    private int mButtonTextColor, mButtonTextSize;
    private Drawable mButtonBackground;
    private int mEmptyResId = NO_ID, mLoadingResId = NO_ID, mErrorResId = NO_ID;
    private int mContentId = NO_ID;

    private Map<Integer, View> mLayouts = new HashMap<>();

    private LayoutInflater mInflater;

    private OnRefreshListener onRefreshListener;
    private OnRetryListener onRetryListener;

    public interface OnRefreshListener {
        void onRefresh(@NonNull View view);
    }

    public interface OnRetryListener {
        void onRetry(@NonNull View view);
    }

    public interface OnInflateListener {
        void onInflate(View inflated);
    }

    public static LoadingLayout wrap(Activity activity) {
        return wrap(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
    }

    public static LoadingLayout wrap(Fragment fragment) {
        return wrap(fragment.getView());
    }

    public static LoadingLayout wrap(View view) {
        if (view == null) {
            throw new RuntimeException("content view can not be null");
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (view == null) {
            throw new RuntimeException("parent view can not be null");
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int index = parent.indexOfChild(view);
        parent.removeView(view);

        LoadingLayout layout = new LoadingLayout(view.getContext());
        parent.addView(layout, index, lp);
        layout.addView(view);
        layout.setContentView(view);
        return layout;
    }


    public LoadingLayout(Context context) {
        this(context, null, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayout_Style);
        try {
            mEmptyImage = a.getResourceId(R.styleable.LoadingLayout_llEmptyImage, R.drawable.icon_view_empty);
            mEmptyText = a.getString(R.styleable.LoadingLayout_llEmptyText);

            mErrorImage = a.getResourceId(R.styleable.LoadingLayout_llErrorImage, R.drawable.icon_view_error);
            mErrorText = a.getString(R.styleable.LoadingLayout_llErrorText);
            mRetryText = a.getString(R.styleable.LoadingLayout_llRetryText);

            mTextColor = a.getColor(R.styleable.LoadingLayout_llTextColor, 0xff999999);
            mTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llTextSize, dp2px(16));

            mButtonTextColor = a.getColor(R.styleable.LoadingLayout_llButtonTextColor, 0xff999999);
            mButtonTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llButtonTextSize, dp2px(16));
            mButtonBackground = a.getDrawable(R.styleable.LoadingLayout_llButtonBackground);

            mEmptyResId = a.getResourceId(R.styleable.LoadingLayout_llEmptyResId, R.layout._loading_layout_empty);
            mLoadingResId = a.getResourceId(R.styleable.LoadingLayout_llLoadingResId, R.layout._loading_layout_loading);
            mErrorResId = a.getResourceId(R.styleable.LoadingLayout_llErrorResId, R.layout._loading_layout_error);
        } catch (Exception e) {
            e.printStackTrace();
        }
        a.recycle();
    }

    private int dp2px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
        View view = getChildAt(0);
        setContentView(view);
    }

    private void setContentView(View view) {
        mContentId = view.getId();
        mLayouts.put(mContentId, view);
    }

    public LoadingLayout setLoadingView(@LayoutRes int id) {
        if (mLoadingResId != id) {
            remove(mLoadingResId);
            mLoadingResId = id;
        }
        return this;
    }

    public LoadingLayout setEmptyView(@LayoutRes int id) {
        if (mEmptyResId != id) {
            remove(mEmptyResId);
            mEmptyResId = id;
        }
        return this;
    }

    public LoadingLayout setOnEmptyInflateListener(OnInflateListener listener) {
        mOnEmptyInflateListener = listener;
        if (mOnEmptyInflateListener != null && mLayouts.containsKey(mEmptyResId)) {
            listener.onInflate(mLayouts.get(mEmptyResId));
        }
        return this;
    }

    public LoadingLayout setOnErrorInflateListener(OnInflateListener listener) {
        mOnErrorInflateListener = listener;
        if (mOnErrorInflateListener != null && mLayouts.containsKey(mErrorResId)) {
            listener.onInflate(mLayouts.get(mErrorResId));
        }
        return this;
    }

    public LoadingLayout setEmptyImage(@DrawableRes int resId) {
        mEmptyImage = resId;
        setImage(mEmptyResId, R.id.empty_image, mEmptyImage);
        return this;
    }

    public LoadingLayout setEmptyText(String value) {
        mEmptyText = value;
        setText(mEmptyResId, R.id.empty_text, mEmptyText);
        return this;
    }

    public LoadingLayout setErrorImage(@DrawableRes int resId) {
        mErrorImage = resId;
        setImage(mErrorResId, R.id.error_image, mErrorImage);
        return this;
    }

    public LoadingLayout setErrorText(String value) {
        mErrorText = value;
        setText(mErrorResId, R.id.error_text, mErrorText);
        return this;
    }

    public LoadingLayout setRetryText(String text) {
        mRetryText = text;
        setText(mErrorResId, R.id.retry_button, mRetryText);
        return this;
    }


    public LoadingLayout setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
        return this;
    }

    public LoadingLayout setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        return this;
    }

    public void refresh() {
        Runnable runnable = () -> {
            changeVIew(mLoadingResId);
            if (onRefreshListener != null && mLayouts.containsKey(mLoadingResId)) {
                onRefreshListener.onRefresh(mLayouts.get(mLoadingResId));
            }
        };
        runnable.run();
    }

    public void finishRefreshNoData(String mEmptyText) {
        setEmptyText(mEmptyText);
        finish(mEmptyResId);
    }

    public void finishRefreshNoData() {
        finish(mEmptyResId);
    }

    public void finishRefreshError(String mErrorText) {
        setErrorText(mErrorText);
        finish(mErrorResId);
    }

    public void finishRefreshError() {
        finish(mErrorResId);
    }

    public void finishRefresh() {
        finish(mContentId);
    }

    private void finish(int layoutId) {
        Runnable runnable = () -> {
            changeVIew(layoutId);
        };
        this.postDelayed(runnable, 30);
    }

    private void changeVIew(int layoutId) {
        for (View view : mLayouts.values()) {
            view.setVisibility(GONE);
        }
        View view = getLayout(layoutId);
        view.setVisibility(VISIBLE);
        if (layoutId == mLoadingResId) {
            getLayout(mContentId).setVisibility(VISIBLE);
            view.setOnTouchListener((v, event) -> true);
        } else {
            view.setOnTouchListener(null);
        }
    }

    private void remove(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            View vg = mLayouts.remove(layoutId);
            removeView(vg);
        }
    }

    private View getLayout(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts.get(layoutId);
        }
        View layout = mInflater.inflate(layoutId, this, false);
        layout.setVisibility(GONE);
        addView(layout);
        mLayouts.put(layoutId, layout);
        if (layoutId == mEmptyResId) {
            ImageView img = layout.findViewById(R.id.empty_image);
            if (img != null) {
                img.setImageResource(mEmptyImage);
            }
            TextView view = layout.findViewById(R.id.empty_text);
            if (view != null) {
                view.setText(mEmptyText);
                view.setTextColor(mTextColor);
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener.onInflate(layout);
            }
        } else if (layoutId == mErrorResId) {
            ImageView img = layout.findViewById(R.id.error_image);
            if (img != null) {
                img.setImageResource(mErrorImage);
            }
            TextView txt = layout.findViewById(R.id.error_text);
            if (txt != null) {
                txt.setText(mErrorText);
                txt.setTextColor(mTextColor);
                txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
            TextView btn = layout.findViewById(R.id.retry_button);
            if (btn != null) {
                btn.setText(mRetryText);
                btn.setTextColor(mButtonTextColor);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize);
                btn.setBackground(mButtonBackground);
            }
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener.onInflate(layout);
            }
        }
        if (layoutId != mLoadingResId && layoutId != mContentId) {
            layout.setOnClickListener(view -> {
                if (onRetryListener != null) {
                    onRetryListener.onRetry(view);
                }
            });
        }
        return layout;
    }

    private void setText(int layoutId, int ctrlId, CharSequence value) {
        if (mLayouts.containsKey(layoutId)) {
            TextView view = mLayouts.get(layoutId).findViewById(ctrlId);
            if (view != null) {
                view.setText(value);
            }
        }
    }

    private void setImage(int layoutId, int ctrlId, int resId) {
        if (mLayouts.containsKey(layoutId)) {
            ImageView view = mLayouts.get(layoutId).findViewById(ctrlId);
            if (view != null) {
                view.setImageResource(resId);
            }
        }
    }
}
