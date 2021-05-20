package com.yb.schoolmarket.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.yb.schoolmarket.R;
import com.yb.schoolmarket.bean.Worker;
import com.yb.schoolmarket.utils.Check;
import com.yb.schoolmarket.utils.Network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private Button register,login,get_code;
    private EditText username,nickname,pw1,pw2,code;
    private String Random_code;
    private TimeCount time;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initView() {
        register = findViewById(R.id.register_go);
        login = findViewById(R.id.register_login);
        get_code = findViewById(R.id.get_code);
        username = findViewById(R.id.register_username);
        nickname = findViewById(R.id.register_nickname);
        pw1 = findViewById(R.id.register_password1);
        pw2 = findViewById(R.id.register_password2);
        code = findViewById(R.id.register_code);
        time = new TimeCount(60000, 1000);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")){
                    dialog("请输入邮箱！！！");
                }else {
                    time = new TimeCount(60000, 1000);
                    CreateCode();
                    SendCode(username.getText().toString(),Random_code);
                    time.start();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = username.getText().toString();
                String nc = nickname.getText().toString();
                String pw_1 = pw1.getText().toString();
                String pw_2 = pw2.getText().toString();
                String cd = code.getText().toString();
                System.out.println(un+"=="+nc+"=="+pw_1+"=="+pw_2+"=="+cd);
                if(un.equals("")||nc.equals("")||pw_1.equals("")||pw_2.equals("")||nc.equals("")||cd.equals("")){
                    dialog("请将信息填写完整！");
                }
                else{
                    if(!pw_1.equals(pw_2)){
                        dialog("两次密码不同，请重试！");
                    }
                    if(!cd.equals(Random_code)){
                        dialog("请输入正确的验证码！");
                    }
                    else{
                        Worker worker = new Worker(un,pw_1,nc,"2131099735");
                        Network.Register(worker, new Network.RegisterListener() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.e("注册异常", "onFailure: "+e);
                                Looper.prepare();
                                dialog("注册失败，请稍后重试");
                                Looper.loop();
                            }
                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String string = response.body().string();
                                String code = new Check().getCode(string);
                                if(code.equals("200")){
                                    Looper.prepare();
                                    dialog("注册成功！");
                                    Looper.loop();
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                }
                                if(code.equals("400")){
                                    Looper.prepare();
                                    dialog("注册失败，用户名不可用！");
                                    Looper.loop();
                                }
                                if(code.equals("500")){
                                    Looper.prepare();
                                    dialog("注册失败，请稍后重试！");
                                    Looper.loop();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void dialog(String message) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(RegisterActivity.this);
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


    private void SendCode(String email,String code) {
        Network.SendCode(email, code, new Network.SendCodeListener() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("SendCode异常", "onFailure: "+e);
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.body().string().equals("true")){
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"发送成功",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    private void CreateCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
         this.Random_code = str.toString();
        System.out.println(Random_code);
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            get_code.setBackgroundColor(Color.parseColor("#B6B6D8"));
            get_code.setClickable(false);
            get_code.setText("("+millisUntilFinished / 1000 +")秒后重新发送");
        }

        @Override
        public void onFinish() {
            get_code.setText("重新获取验证码");
            get_code.setClickable(true);
            get_code.setBackgroundColor(Color.parseColor("#2d8cf0"));
        }
    }


}
