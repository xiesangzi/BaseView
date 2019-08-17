package com.yhms.baseview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yhms.view.LoadingDialog;
import com.yhms.view.immersionbar.ImmersionBar;

public class MainActivity extends AppCompatActivity {
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .transparentNavigationBar()
                .transparentBar()
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true).init();
        dialog = new LoadingDialog(this);
        dialog.show();
//        dialog.success();
//        dialog.fail();
//        dialog.setLoadText("342343243");
//        dialog.show();
    }
}
