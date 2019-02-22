package com.sensetime.qinhaihang_vendor.dynamiccircledemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sensetime.qinhaihang_vendor.qhhview.progressbar.circlebar.CircleProgressBar;
import com.sensetime.qinhaihang_vendor.qhhview.progressbar.listener.IProgressListener;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CircleProgressBar mCpb;
    private int mCount;
    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCpb = findViewById(R.id.cpb);
        mCpb.setProgressListener(new IProgressListener() {
            @Override
            public void onProgress(int progress) {
                Log.v("qhh", "progress = " + progress);
            }

            @Override
            public void onProgressFinish() {
                mTimerTask.cancel();
                mTimer.cancel();
            }
        });

    }

    public void start(View view) {

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mCount++;
                Log.v("qhh", "timer run count = " + mCount);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCpb.setPercent(mCount);
                    }
                });
            }
        };
        mTimer.schedule(mTimerTask,1000,1000);
    }
}
