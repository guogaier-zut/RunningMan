package com.yb.schoolmarket.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yb.schoolmarket.R;
import com.yb.schoolmarket.utils.Network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;

public class ForgetActivity extends AppCompatActivity {
    private Button sendcode,enter,login;
    private EditText username,password,code;
    private String Random_code;
    private TimeCount time;
    private void CreateCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        this.Random_code = str.toString();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initview();
    }

    private void initview() {
        time = new TimeCount(60000, 1000);
        sendcode = findViewById(R.id.forget_get_code);
        enter = findViewById(R.id.forget_enter);
        login = findViewById(R.id.forget_login);
        username = findViewById(R.id.forget_username);
        code = findViewById(R.id.forget_code);
        password = findViewById(R.id.forget_password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetActivity.this,LoginActivity.class));
                finish();
            }
        });
        sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network.CheckWorker(username.getText().toString(), new Network.SendCodeListener() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.body().string().equals("true")) {
                            ForgetActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    time.start();
                                }
                            });
                            CreateCode();
                            Network.SendCode(username.getText().toString(), Random_code, new Network.SendCodeListener() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                }
                            });

                        }
                        else{
                            Looper.prepare();
                            dialog("用户名不存在");
                            Looper.loop();
                        }
                    }
                });

            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cd = code.getText().toString();
                final String pw = password.getText().toString();
                if(cd.equals(Random_code)) {
                    if(!pw.equals(""))
                    {

                        Network.Update_Password(username.getText().toString(), pw, new Network.SendCodeListener() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                if (response.body().string().equals("true")){
                                    ForgetActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final AlertDialog.Builder normalDialog =
                                                    new AlertDialog.Builder(ForgetActivity.this);
                                            normalDialog.setIcon(R.drawable.dialog);
                                            normalDialog.setTitle("提示");
                                            normalDialog.setMessage("\n" + "您的密码为"+pw+ "\n");
                                            normalDialog.setPositiveButton("去登录",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            startActivity(new Intent(ForgetActivity.this,LoginActivity.class));
                                                            finish();
                                                        }
                                                    });
                                            // 显示
                                            normalDialog.show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else{
                        ForgetActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog("请输入密码");
                            }
                        });
                    }
                }
                else{
                    dialog("验证码错误");
                }
            }
        });
    }

    private void dialog(String message) {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(ForgetActivity.this);
            normalDialog.setIcon(R.drawable.dialog);
            normalDialog.setTitle("提示");
            normalDialog.setMessage("\n" + message + "\n");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            // 显示
            normalDialog.show();
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendcode.setBackgroundColor(Color.parseColor("#B6B6D8"));
            sendcode.setClickable(false);
            sendcode.setText("("+millisUntilFinished / 1000 +")秒后重新发送");
        }

        @Override
        public void onFinish() {
            sendcode.setText("重新获取验证码");
            sendcode.setClickable(true);
            sendcode.setBackgroundColor(Color.parseColor("#2d8cf0"));
        }
    }

}
