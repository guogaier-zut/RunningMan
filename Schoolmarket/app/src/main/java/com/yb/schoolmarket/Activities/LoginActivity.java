package com.yb.schoolmarket.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
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

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button login,forget,register;
    private EditText username,password;
    private Worker worker;
    Network network = new Network();
    private String nickname,avatar,id;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 判断登录状态
         * 登录直接跳转，否则输入账密
         */
        if(islogin()){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            Log.i(TAG,nickname);
            Log.i(TAG,id);
            Log.i(TAG,avatar);
            intent.putExtra("nickname",nickname);
            intent.putExtra("id",id);
            intent.putExtra("avatar",avatar);
            startActivity(intent);
        }else{
        setContentView(R.layout.activity_login);
        initView();
        }
    }
    private boolean islogin(){
        nickname = new Check().getCache().get("worker_nickname");
        avatar = new Check().getCache().get("worker_avatar");
        id = new Check().getCache().get("worker_id");
        if(nickname!=null)
            return true;
        else
            return false;
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
        login  = (Button) findViewById(R.id.login);
        forget =(Button) findViewById(R.id.forget);
        register = (Button) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String up = password.getText().toString();
                Log.i("username",uname);
                Log.i("password",up);
                if(uname.equals("")||up.equals("")){
                    dialog("请将信息填写完整");
                }else{
                    final Worker worker = new Worker(uname,up);
                    /**
                     * 用户信息验证
                     */
                    network.Login(worker, new Network.LoginListener() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            System.out.println(e);
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String info = response.body().string();
                            String code = new Check().getCode(info);
                            if(code.equals("200")){
                                /**
                                 * 保存用户信息到本地
                                 */
                                saveUserInfoToLocal(info);
                                /**
                                 * 页面跳转
                                 */
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                String nickname = new Check().getCache().get("worker_nickname");
                                intent.putExtra("nickname",nickname);
                                String id = new Check().getCache().get("worker_id");
                                intent.putExtra("id",id);
                                startActivity(intent);
                                finish();
                            }
                            if(code.equals("400")){
                                //
                                Looper.prepare();
                                dialog("密码错误");
                                Looper.loop();
                            }
                            if(code.equals("500")){
                                Looper.prepare();
                                //账户不存在
                                dialog("账户不存在");
                                Looper.loop();
                            }
                        }
                    });
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }

    private void saveUserInfoToLocal(String json) {
        new Check().saveToLocal(this,json);
    }

    private void dialog(String msg) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(LoginActivity.this);
        normalDialog.setIcon(R.drawable.dialog);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("\n" + msg + "\n");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }
    public interface HttpCallBackListener {
        void onFinish(String respose);
        void onError(Exception e);
    }
}
