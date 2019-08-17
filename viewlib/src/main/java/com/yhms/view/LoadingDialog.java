package com.yhms.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
    /** 加载的结果图标显示**/
    private ImageView ivLoadResult;
    /** 加载的文字展示**/
    private TextView tvLoadTxt;
    /** 加载中的图片**/
    private ProgressBar pbLoading;
    private final int LOAD_SUCC = 0x001;
    private final int LOAD_FAIL = 0x002;
    private final int LOAD_TEXT = 0x003;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_SUCC:
                    dismiss();
                    break;
                case LOAD_FAIL:
                    dismiss();
                    break;
                case LOAD_TEXT:
                    if (tvLoadTxt == null && msg.obj != null) {
                        tvLoadTxt.setText(msg.obj.toString());
                    }
                    break;
                default:
                    break;
            }
        }
    };

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
        ivLoadResult = findViewById(R.id.iv_load_result);
        tvLoadTxt = findViewById(R.id.tv_load_txt);
        pbLoading = findViewById(R.id.pb_loading);
    }

    public void setLoadText(String text) {
        if (tvLoadTxt == null || TextUtils.isEmpty(text)) {
            return;
        }
        Message message = new Message();
        message.what = LOAD_TEXT;
        message.obj = text;
        mHandler.sendMessage(message);
    }

    public void success() {// 加载成功
        pbLoading.setVisibility(View.GONE);
        ivLoadResult.setVisibility(View.VISIBLE);
        tvLoadTxt.setText("加载成功");
        ivLoadResult.setImageResource(R.drawable.load_suc_icon);
        mHandler.sendEmptyMessageDelayed(LOAD_SUCC, 1000);
    }

    public void fail() {// 加载失败
        pbLoading.setVisibility(View.GONE);
        ivLoadResult.setVisibility(View.VISIBLE);
        tvLoadTxt.setText("加载失败");
        ivLoadResult.setImageResource(R.drawable.load_fail_icon);
        mHandler.sendEmptyMessageDelayed(LOAD_FAIL, 1000);
    }
}