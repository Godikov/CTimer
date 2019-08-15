package com.humu.ct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tchcn.timer.CTimer;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private final int COUNT_60 = 0;
    private final int COUNT_30 = 1;

    private TextView tv_60;
    private TextView tv_30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_60 = (TextView) findViewById(R.id.tv_60);
        tv_30 = (TextView) findViewById(R.id.tv_30);

    }

    public void count60(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                CTimer.start(COUNT_60, 60, new CTimer.CTimerListener() {
                    @Override
                    public void onCountDownStart(int tag) {
                        tv_60.setText("开始倒计时");
                        Log.d(TAG, "====onCountDownStart====" + tag);
                    }

                    @Override
                    public void onCount(int tag, int timeNow) {
                        tv_60.setText(timeNow + "");
                        Log.d(TAG, "====onCount====" + tag + " " + timeNow);
                    }

                    @Override
                    public void onCountPause(int tag, int timeNow) {
                        Log.d(TAG, "====onCountPause====" + tag + " " + timeNow);
                    }

                    @Override
                    public void onCountRestart(int tag) {
                        Log.d(TAG, "====restartCount====" + tag);
                    }

                    @Override
                    public void onCountOver(int tag) {
                        tv_60.setText("倒计时结束");
                        Log.d(TAG, "====onCountOver====" + tag);
                    }

                    @Override
                    public void onStop(int tag) {
                        tv_60.setText("倒计时手动结束");
                        Log.d(TAG, "====onCountOver====" + tag);
                    }
                });

            }
        }).start();

/*
        CTimer.start(COUNT_60, 60, new CTimer.CTimerListener() {
            @Override
            public void onCountDownStart(int tag) {
                tv_60.setText("开始倒计时");
                Log.d(TAG,"====onCountDownStart===="+tag);
            }

            @Override
            public void onCount(int tag, int timeNow) {
                tv_60.setText(timeNow+"");
                Log.d(TAG,"====onCount===="+tag+" "+timeNow);
            }

            @Override
            public void onCountPause(int tag, int timeNow) {
                Log.d(TAG,"====onCountPause===="+tag+" "+timeNow);
            }

            @Override
            public void onCountRestart(int tag) {
                Log.d(TAG,"====restartCount===="+tag);
            }

            @Override
            public void onCountOver(int tag) {
                tv_60.setText("倒计时结束");
                Log.d(TAG,"====onCountOver===="+tag);
            }

            @Override
            public void onStop(int tag) {
                tv_60.setText("倒计时手动结束");
                Log.d(TAG,"====onCountOver===="+tag);
            }
        });
*/
    }

    public void pause60(View view) {
        CTimer.pause(COUNT_60);
    }

    public void resume60(View view) {
        CTimer.resume(COUNT_60);
    }

    public void stop60(View view) {
        CTimer.stop(COUNT_60);
    }

    public void count30(View view) {
        CTimer.start(COUNT_30, 30, new CTimer.CTimerListener() {
            @Override
            public void onCountDownStart(int tag) {
                tv_30.setText("开始倒计时");
                Log.d(TAG, "====onCountDownStart====" + tag);
            }

            @Override
            public void onCount(int tag, int timeNow) {
                tv_30.setText(timeNow + "");
                Log.d(TAG, "====onCount====" + tag + " " + timeNow);
            }

            @Override
            public void onCountPause(int tag, int timeNow) {
                Log.d(TAG, "====onCountPause====" + tag + " " + timeNow);
            }

            @Override
            public void onCountRestart(int tag) {
                Log.d(TAG, "====restartCount====" + tag);
            }

            @Override
            public void onCountOver(int tag) {
                tv_30.setText("倒计时结束");
                Log.d(TAG, "====onCountOver====" + tag);
            }

            @Override
            public void onStop(int tag) {
                tv_30.setText("倒计时手动结束");
                Log.d(TAG, "====onCountOver====" + tag);
            }
        });
    }

    public void pause30(View view) {
        CTimer.pause(COUNT_30);
    }

    public void resume30(View view) {
        CTimer.resume(COUNT_30);
    }

    public void stop30(View view) {
        CTimer.stop(COUNT_30);
    }

    public void stopAll(View view) {
        CTimer.stopAll();
    }
}
