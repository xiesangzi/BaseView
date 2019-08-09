package com.yhms.baseview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yhms.view.LoadingLayout;

public class MainActivity extends AppCompatActivity {
    LoadingLayout vLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vLoading = LoadingLayout.wrap(this);
        vLoading.setOnRefreshListener(refreshLayout -> {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    vLoading.finishRefreshNoData();
                }
            }.execute();

        });

        TextView btn = findViewById(R.id.tv_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(111111111);
                vLoading.finishRefreshError();
            }
        });
        vLoading.refresh();
        vLoading.setOnRetryListener(view -> vLoading.refresh());
    }
}
