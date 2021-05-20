package com.yb.schoolmarket.Activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.yb.schoolmarket.R;
import com.yb.schoolmarket.adapter.myadapter;
import com.yb.schoolmarket.adapter.taskadapter;
import com.yb.schoolmarket.bean.HomeItem;
import com.yb.schoolmarket.fragment.MeFragment;
import com.yb.schoolmarket.fragment.HomeFragment;
import com.yb.schoolmarket.fragment.NoNetWorkFragment;
import com.yb.schoolmarket.fragment.TaskFragment;
import com.yb.schoolmarket.utils.Check;
import com.yb.schoolmarket.utils.Network;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView home,me,task;
    private HomeFragment homeFragment;
    private DrawerLayout drawerLayout;
    static public NoNetWorkFragment no_networkFragment;
    private TaskFragment taskFragment;
    private MeFragment dashFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RecyclerView recyclerView;
    private myadapter adapter;
    private List<HomeItem> mData;
    private boolean network;
    public TextView time,username;
    public static ImageView avatar;
    private Button logout;

    Date date = new Date();
    public static String nickname;
    public static String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout);
        final DrawerLayout drawerLayout=findViewById(R.id.drawerLayout);
        //给按钮添加一个监听器
        findViewById(R.id.top_view_left_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开侧滑菜单
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
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


    public void initView(){
        /**
         * 自动点击屏幕
         *    失败了，还要自己点一下才显示recyclerview
         *    不知道为什么
         */
//        relativeLayout = findViewById(R.id.content_layout);
//        boolean b = relativeLayout.performClick();
//        System.out.println("点击事件"+b);
        /**
         *设置侧边栏信息
         */
        //设置时间
        time = (TextView) findViewById(R.id.time);
        avatar = findViewById(R.id.user_icon);
        username = findViewById(R.id.user_name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(date);
        time.setText(dateNowStr);
        //设置用户信息
        Intent intent = null;
        intent =  getIntent();
        nickname = intent.getStringExtra("nickname");
        id = intent.getStringExtra("id");
        int avatar = R.drawable.avatar;
        System.out.println(avatar);
        this.avatar.setImageResource(R.drawable.avatar);
        username.setText(nickname);
        /**
         * 初始化页面的功能模块
         */
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        network = new Network().checkNetworkAvailable(this);
//        adapter = new myadapter(mData,this);
//        adapter = new taskadapter(mData,this);
        //如果有网络，初始化页面
        if(network){
            Log.e("网络","设备有网络");
            home = findViewById(R.id.tab_home_img);
            me = findViewById(R.id.tab_me_img);
            task = findViewById(R.id.tab_task_img);
            home.setBackgroundResource(R.drawable.home_selected);
            home.setOnClickListener(this);
            me.setOnClickListener(this);
            task.setOnClickListener(this);
            homeFragment = new HomeFragment();
            Bundle sendBundle = new Bundle();
            sendBundle.putString("id",id);
            homeFragment.setArguments(sendBundle);
            fragmentTransaction.replace(R.id.content_layout,homeFragment);
            fragmentTransaction.commit();
        }
        //如果没有网络
        else{
            Log.e("网络","设备无网络");
            home = findViewById(R.id.tab_home_img);
            me = findViewById(R.id.tab_me_img);
            task = findViewById(R.id.tab_task_img);
            home.setBackgroundResource(R.drawable.home_selected);
            home.setOnClickListener(this);
            /**
             * tabbar不再设置点击事件
             */
//            me.setOnClickListener(this);
//            task.setOnClickListener(this);
            no_networkFragment=new NoNetWorkFragment();
            fragmentTransaction.replace(R.id.content_layout,no_networkFragment);
            fragmentTransaction.commit();
        }

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setIcon(R.drawable.dialog);
                normalDialog.setTitle("提示");
                normalDialog.setMessage("\n" + "确定要退出吗？ "+ "\n");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean b = new Check().deleteFile();
                                if(b){
                                    finish();
                                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                }else{
                                    Toast.makeText(MainActivity.this,"退出失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this,"取消退出",Toast.LENGTH_SHORT).show();
                            }
                        });
                // 显示
                normalDialog.show();


            }
        });
    }

    @Override
    public void onClick(View v) {
//
//        boolean b1 = homeFragment!=null;
//        boolean b2 = no_networkFragment!=null;
//        boolean b3 = taskFragment!=null;
//        boolean b4 = dashFragment!=null;
//
//        System.out.println("homefragment:"+b1);
//        System.out.println("no_networkFragment:"+b2);
//        System.out.println("taskFragment:"+b3);
//        System.out.println("dashFragment:"+b4);
        int id = v.getId();
        fragmentTransaction=fragmentManager.beginTransaction();
        switch (id){
            case R.id.tab_home_img:
                System.out.println("主页被点击");
                home.setBackgroundResource(R.drawable.home_selected);
                me.setBackgroundResource(R.drawable.me);
                task.setBackgroundResource(R.drawable.task);
                hideAllFragment();
                showHomeFragment();
                break;
            case R.id.tab_task_img:
                System.out.println("订单页被点击");
                task.setBackgroundResource(R.drawable.task_selected);
                me.setBackgroundResource(R.drawable.me);
                home.setBackgroundResource(R.drawable.home);
                hideAllFragment();
                showTaskFragment();
                break;
            case R.id.tab_me_img:
                System.out.println("我的被点击");
                me.setBackgroundResource(R.drawable.me_selected);
                home.setBackgroundResource(R.drawable.home);
                task.setBackgroundResource(R.drawable.task);
                hideAllFragment();
                showMeFragment();
                break;
        }
        fragmentTransaction.commit();
    }

   public void hideAllFragment(){
       if(no_networkFragment!=null){
           fragmentTransaction.hide(no_networkFragment);
       }
       if(homeFragment!=null){
           fragmentTransaction.hide(homeFragment);
       }
       if(taskFragment!=null){
           fragmentTransaction.hide(taskFragment);
       }
       if(dashFragment!=null){
           fragmentTransaction.hide(dashFragment);
       }

   }
   public void showHomeFragment(){
       if(!network){
           fragmentTransaction.show(no_networkFragment);
       }else{
           if(homeFragment == null){
               homeFragment = new HomeFragment();
               fragmentTransaction.add(R.id.content_layout,homeFragment);
           }
           else {
               fragmentTransaction.show(homeFragment);
           }
       }
   }
   public void showTaskFragment(){
       if(taskFragment==null){
           System.out.println("new了个taskFragment");
           taskFragment = new TaskFragment();
           fragmentTransaction.add(R.id.content_layout,taskFragment);
       }else{
           System.out.println("显示了taskFragment");
           fragmentTransaction.show(taskFragment);
       }
   }
   public void showMeFragment(){
       if(dashFragment==null){
           dashFragment = new MeFragment();
           fragmentTransaction.add(R.id.content_layout,dashFragment);
       }else{
           fragmentTransaction.show(dashFragment);
       }
   }

}
