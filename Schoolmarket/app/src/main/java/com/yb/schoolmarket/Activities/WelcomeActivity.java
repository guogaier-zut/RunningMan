package com.yb.schoolmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.yb.schoolmarket.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private View welcomeImg = null;

    private Button button;
    private int recLen = 5;//跳过倒计时提示5秒
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_welcome);
        initView();
        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒
        /**
         * 正常情况下不点击跳过
         */
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                //从闪屏界面跳转到首界面
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);//延迟5S后发送handler信息

    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    private void initView() {
        button = findViewById(R.id.skip);
        /**
         * 跳过
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
            }
        });

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    button.setText("跳过("+recLen+")");
                    if (recLen < 0) {
                        timer.cancel();
                        button.setVisibility(View.GONE);//倒计时到0隐藏字体
                    }
                }
            });
        }
    };



}