package com.yb.schoolmarket.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yb.schoolmarket.Activities.MainActivity;
import com.yb.schoolmarket.R;
import com.yb.schoolmarket.adapter.myadapter;
import com.yb.schoolmarket.bean.HomeItem;
import com.yb.schoolmarket.utils.Network;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class HomeFragment extends BaseFragment{
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RecyclerView recyclerView;
    private List<HomeItem> list;
    private Context context;
    private myadapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static String id;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x11) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("CMCC", "权限被允许");
            } else {
                Log.i("CMCC", "权限被拒绝");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        Bundle b = getArguments();
        if(null != b){
            id = b.getString("id");
        }
        id = MainActivity.id;
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        context=view.getContext();
        recyclerView=view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        list = new ArrayList<HomeItem>();
        initData();
        HanderPullDown();
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        manager.setReverseLayout(true);
        adapter = new myadapter(list,context);
        adapter.setCallToClient(new myadapter.CallToClient() {
            @Override
            public void onCall(View view, String phone) {
//                Toast.makeText(context,phone,Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(context, Manifest.
                        permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // 调用ActivityCompat.requestPermissions() 方法，向用户申请授权
                    ActivityCompat.requestPermissions((Activity) context, new String[]{ Manifest.permission.CALL_PHONE}, 1);
                    call(phone);
                } else {
                    // 已经授权，直接执行 call() 方法
                    call(phone);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
        return view;

    }

    private void call(String phone) {
        // 为了防止程序崩溃我们将所有操作放在了异常捕获代码块当中
        try {
            // 构建一个隐式 intent ，intent 的 action 为 ACTION_CALL
            // ACTION_CALL 是系统内置的一个打电话的动作
            Intent intent = new Intent(Intent.ACTION_CALL);

            // 指定协议为 tel ，号码为 10086
            intent.setData(Uri.parse("tel:"+phone));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    private void HanderPullDown() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();//增加部分
                        List<HomeItem> data = new Network().getData();
                        if (data.isEmpty()){
                            Toast.makeText(context,"暂无订单",Toast.LENGTH_SHORT).show();
                        }
                        if (data.size()==list.size()){
                            Toast.makeText(context,"暂无新的订单",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.i(TAG, "run: list.size="+list.size());
                            Log.i(TAG, "run: data.size="+data.size());
                        list.clear();
                            /**
                             * 假数据
                             */
//                        HomeItem homeItem = new HomeItem(1,"demo","demo","demo","测试数据",12);
//                        list.add(homeItem);
                            for (int i = data.size()-1;i>=0;i--){
                                list.add(data.get(i));
                            }
                            Toast.makeText(context,"刷新成功",Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();
                    }
                }).start();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void initData() {
        Log.i(TAG,"数据初始化");
        //todo
        /**
         * 假数据
         */
//        for(int i = 0;i<10;i++) {
//            HomeItem homeItem = new HomeItem(i, " "+i," "+i,i);
//            list.add(homeItem);
//        }
        /**
         * 真实数据，在子线程中请求
         */
        new Thread(networkTask).start();
    }
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            List<HomeItem> data = Network.getData();
            if(data.size()>0){
            for (int i = data.size()-1;i>=0;i--){
                list.add(data.get(i));
            }
            Log.i("list--size",String.valueOf(list.size()));
            }
        }
    };


}