package com.lany.multistateview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lany.view.StateLayout;

public class MainActivity extends AppCompatActivity {
    private StateLayout mStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showContent();
            }
        });
        findViewById(R.id.error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showError();
            }
        });
        findViewById(R.id.empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showEmpty();
            }
        });
        findViewById(R.id.network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showNetwork();
            }
        });
        findViewById(R.id.loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateLayout.showLoading();
            }
        });


        mStateLayout = (StateLayout) findViewById(R.id.stateLayout);
        mStateLayout.setOnStateListener(new StateLayout.OnStateListener() {
            @Override
            public void onStateChanged(@StateLayout.State int state) {
                Log.i("MainActivity", "onStateChanged: state==" + state);
            }
        });
        mStateLayout.setOnRetryListener(new StateLayout.OnRetryListener() {
            @Override
            public void onRetry(@StateLayout.State int state) {
                mStateLayout.showLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStateLayout.showContent();
                        Toast.makeText(getApplicationContext(), "load data finish", Toast.LENGTH_SHORT).show();
                    }
                },3000);


            }
        });

        ListView list = (ListView) findViewById(R.id.list);

        String[] data = new String[100];
        for (int i = 0; i < 100; i++) {
            data[i] = "Item " + i;
        }
        list.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, data));
    }
}

