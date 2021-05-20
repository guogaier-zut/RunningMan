package com.yb.schoolmarket.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yb.schoolmarket.Activities.MainActivity;
import com.yb.schoolmarket.R;
import com.yb.schoolmarket.adapter.taskadapter;
import com.yb.schoolmarket.bean.HomeItem;
import com.yb.schoolmarket.utils.Network;
import com.yb.schoolmarket.utils.ToObjectList;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class TaskFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private List<HomeItem> list;
    private Context context;
    private taskadapter task_adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//
//        Bundle b = getArguments();
//        if(null != b){
//            id = b.getString("id");
//        }
        id= MainActivity.id;
        View view=inflater.inflate(R.layout.fragment_task,container,false);
        context=view.getContext();
        recyclerView=view.findViewById(R.id.task_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.task_refresh);
        list = new ArrayList<HomeItem>();
        initData();
        HanderPullDown();
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        task_adapter = new taskadapter(list,context);
        task_adapter.setCallToClient(new taskadapter.CallToClient() {
            @Override
            public void onCall(View view, String phone) {
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
        recyclerView.setAdapter(task_adapter);
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

                    List<HomeItem> data= new ArrayList<>();
                    @Override
                    public void run() {
                        Network.GetWorkerOrders(id, new Network.GetWorkerOrdersListener() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                System.out.println(e);
                            }
                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String string = response.body().string();
                                List<HomeItem> byId = ToObjectList.getById(string);
                                data.addAll(byId);
                                if (data.isEmpty()){
                                    Looper.prepare();
                                    Toast.makeText(context,"暂无订单",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                                if (data.size()==list.size()){
                                    Looper.prepare();
                                    Toast.makeText(context,"暂无新的订单",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                                else{
                                    Log.i(TAG, "run: list.size="+list.size());
                                    Log.i(TAG, "run: data.size="+data.size());
                                    list.clear();
                                    for (int i = data.size()-1;i>=0;i--){
                                        list.add(data.get(i));
                                    }
                                    Looper.prepare();
                                    Toast.makeText(context,"刷新成功",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        });
                    }
                }).start();
                task_adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void initData() {
        Log.i(TAG,"数据初始化");
        new Thread(networkTask).start();
    }
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            Network.GetWorkerOrders(id, new Network.GetWorkerOrdersListener() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e);
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String string = response.body().string();
                    List<HomeItem> byId = ToObjectList.getById(string);
                    list.addAll(byId);
                    System.out.println(list);
                }
            });
        }
    };
}
