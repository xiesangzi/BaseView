package com.yhms.view.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhms.view.R;

public class EditTextWithLabel extends LinearLayout {

    private LayoutInflater mInflater;

    private int formDirection = 1;
    private boolean formEnabled = true;
    private Drawable formBackground;
    private boolean formRequire = false;

    private String formLabel = "";
    private int formLabelColor;
    private int formLabelSize;

    private String formEditHint;
    private int formEditColor;
    private int formEditSize;
    private int formEditHeight;
    private TextView formLabelView;
    private EditText formEditView;
    private ImageView formIconRightView;

    private Drawable selectedDrawable;
    private Drawable unSelectedDrawable;
    private int formIconRight = -1;
    private int defaultHeight = 0;
    private OnClickIconRightListener onClickIconRightListener;

    public EditTextWithLabel(Context context) {
        this(context, null);
    }

    public EditTextWithLabel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextWithLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        handleTypedArray(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public void setOnClickIconRightListener(OnClickIconRightListener onClickIconRightListener) {
        this.onClickIconRightListener = onClickIconRightListener;
    }

    private void handleTypedArray(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FormView, defStyleAttr, 0);
        try {
            defaultHeight = dp2px(40);
            formDirection = typedArray.getInt(R.styleable.FormView_form_direction, formDirection);
            formEnabled = typedArray.getBoolean(R.styleable.FormView_form_enabled, formEnabled);
            formBackground = typedArray.getDrawable(R.styleable.FormView_form_background);
            formRequire = typedArray.getBoolean(R.styleable.FormView_form_require, formRequire);


            formLabel = typedArray.getString(R.styleable.FormView_form_label);
            formLabelColor = typedArray.getColor(R.styleable.FormView_form_label_color, context.getResources().getColor(R.color.h2));
            formLabelSize = typedArray.getDimensionPixelSize(R.styleable.FormView_form_label_size, dp2px(14));
            formEditHeight = typedArray.getDimensionPixelSize(R.styleable.FormView_form_edit_height, defaultHeight);

            formEditHint = typedArray.getString(R.styleable.FormView_form_edit_hint);
            formEditColor = typedArray.getColor(R.styleable.FormView_form_edit_color, context.getResources().getColor(R.color.h1));
            formEditSize = typedArray.getDimensionPixelSize(R.styleable.FormView_form_edit_size, dp2px(20));

            selectedDrawable = getResources().getDrawable(R.drawable.tv_shape_bottom2);
            unSelectedDrawable = getResources().getDrawable(R.drawable.tv_shape_bottom);
            formIconRight = typedArray.getResourceId(R.styleable.FormView_form_icon_right, formIconRight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        typedArray.recycle();
    }

    private void initView(Context context, AttributeSet attrs) {
        View layout;
        if (formDirection == 1) {
            layout = mInflater.inflate(R.layout.form_input_vertical, this, true);
        } else {
            layout = mInflater.inflate(R.layout.form_input_horizontal, this, true);
        }
        layout.setBackground(formBackground);
        formLabelView = layout.findViewById(R.id.form_label);
        formEditView = layout.findViewById(R.id.form_edit);
        formIconRightView = layout.findViewById(R.id.form_icon_right);
        if (formIconRight <= 0) {
            formIconRightView.setVisibility(GONE);
        } else {
            formIconRightView.setImageResource(formIconRight);
        }

        formIconRightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickIconRightListener != null) {
                    onClickIconRightListener.onClick(view);
                }
            }
        });

        if (formRequire) {
            SpannableStringBuilder builder;
            if (formEnabled) {
                builder = new SpannableStringBuilder("*" + formLabel);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                builder.setSpan(redSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                builder = new SpannableStringBuilder(formLabel);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(formLabelColor);
                builder.setSpan(redSpan, 0, formLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            formLabelView.setText(builder);
        } else {
            formLabelView.setText(formLabel);
        }

        formLabelView.setTextColor(formLabelColor);
        formLabelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, formLabelSize);

        formEditView.setEnabled(formEnabled);
        formEditView.setHint(formEditHint);
        formEditView.setTextColor(formEditColor);
        formEditView.setTextSize(TypedValue.COMPLEX_UNIT_PX, formEditSize);
        formEditView.setMinimumHeight(formEditHeight);
        if (formEditHeight > defaultHeight * 1.5) {
            formEditView.setGravity(Gravity.TOP);
        }
        formEditView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    String label = formLabelView.getText().toString();
                    SpannableStringBuilder builder = new SpannableStringBuilder(label);
                    if (label.startsWith("*")) {
                        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(Color.RED);
                        builder.setSpan(redSpan1, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(0xff39A4F8);
                        builder.setSpan(redSpan2, 1, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(0xff39A4F8);
                        builder.setSpan(redSpan2, 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    formLabelView.setText(builder);
                    formLabelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (formLabelSize * 9) / 10);
                    formEditView.setBackground(selectedDrawable);
                } else {
                    String label = formLabelView.getText().toString();
                    SpannableStringBuilder builder = new SpannableStringBuilder(label);
                    if (label.startsWith("*")) {
                        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(Color.RED);
                        builder.setSpan(redSpan1, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(formLabelColor);
                        builder.setSpan(redSpan2, 1, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(formLabelColor);
                        builder.setSpan(redSpan2, 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    formLabelView.setText(builder);
                    formLabelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, formLabelSize);
                    formEditView.setBackground(unSelectedDrawable);
                }
            }
        });
    }

    public interface OnClickIconRightListener {
        void onClick(View view);
    }

    private int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    public int spToPx(float spValue) {
        float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public String getText() {
        return formEditView.getText().toString();
    }

    public void setText(String str) {
        if (!TextUtils.isEmpty(str)) {
            formEditView.setText(str);
        }
    }

    public TextView getFormLabelView() {
        return formLabelView;
    }

    public EditText getFormEditView() {
        return formEditView;
    }

    public ImageView getFormIconRightView() {
        return formIconRightView;
    }

    public void setFormDirection(int formDirection) {
        this.formDirection = formDirection;
        postInvalidate();
    }

    public void setFormEnabled(boolean formEnabled) {
        this.formEnabled = formEnabled;
        postInvalidate();
    }

    public void setFormBackground(Drawable formBackground) {
        this.formBackground = formBackground;
        postInvalidate();
    }

    public void setFormRequire(boolean formRequire) {
        this.formRequire = formRequire;
        postInvalidate();
    }

    public void setFormLabel(String formLabel) {
        this.formLabel = formLabel;
        postInvalidate();
    }

    public void setFormLabelColor(int formLabelColor) {
        this.formLabelColor = formLabelColor;
        postInvalidate();
    }

    public void setFormLabelSize(int formLabelSize) {
        this.formLabelSize = formLabelSize;
        postInvalidate();
    }

    public void setFormEditHint(String formEditHint) {
        this.formEditHint = formEditHint;
        postInvalidate();
    }

    public void setFormEditColor(int formEditColor) {
        this.formEditColor = formEditColor;
        postInvalidate();
    }

    public void setFormEditSize(int formEditSize) {
        this.formEditSize = formEditSize;
        postInvalidate();
    }

    public void setFormEditHeight(int formEditHeight) {
        this.formEditHeight = formEditHeight;
        postInvalidate();
    }
}