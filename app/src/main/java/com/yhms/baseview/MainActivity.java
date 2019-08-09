package com.yhms.baseview;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yhms.view.LoadingLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadingLayout vLoading = LoadingLayout.wrap(this);
        vLoading.setOnRefreshListener(refreshLayout -> {
            try {
                vLoading.finishRefresh();
            } catch (Exception e) {
                vLoading.finishRefreshError();
            }
        });
        vLoading.autoRefresh();
        vLoading.setOnRetryListener(view -> vLoading.autoRefresh());
    }
}
