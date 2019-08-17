package com.yhms.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
    /** 加载的结果图标显示**/
    private ImageView iv_load_result;
    /** 加载的文字展示**/
    private TextView tv_load;
    /** 加载中的图片**/
    private ProgressBar pb_loading;

    public LoadingDialog(Context context) {
        super(context, R.style.Theme_HalfScreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commom_loading_layout);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        //设置参数必须在show之后，不然没有效果
        WindowManager.LayoutParams params = getWindow().getAttributes();
        getWindow().setAttributes(params);
        iv_load_result = findViewById(R.id.iv_load_result);
        tv_load = findViewById(R.id.tv_load);
        pb_loading = findViewById(R.id.pb_loading);
    }

    public void success() {// 加载成功
        pb_loading.setVisibility(View.GONE);
        iv_load_result.setVisibility(View.VISIBLE);
        tv_load.setText("加载成功");
        iv_load_result.setImageResource(R.drawable.load_suc_icon);
    }

    public void fail() {// 加载失败
        pb_loading.setVisibility(View.GONE);
        iv_load_result.setVisibility(View.VISIBLE);
        tv_load.setText("加载失败");
        iv_load_result.setImageResource(R.drawable.load_fail_icon);
    }
}