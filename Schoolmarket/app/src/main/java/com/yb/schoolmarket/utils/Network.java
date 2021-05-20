package com.yb.schoolmarket.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yb.schoolmarket.bean.HomeItem;
import com.yb.schoolmarket.bean.Legwork;
import com.yb.schoolmarket.bean.Order;
import com.yb.schoolmarket.bean.Worker;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Network {


    private final static String PC_IP = "10.133.95.74";


    public interface SendCodeListener extends Callback{}
    public interface LoginListener extends Callback{}
    public interface RegisterListener extends Callback{}
    public interface SaveOrderListener extends Callback{}
    public interface GetWorkerOrdersListener extends Callback{}
    public interface FinishOrderListener extends Callback{}

    // 检测网络
    public boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //请求订单
    public static List<HomeItem> getData() {
        List<HomeItem> list = new ArrayList<>();
        URL url = null;
        try {
            url = new URL("http://" + PC_IP + ":8888/api/getAllOrderByStatus_0");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String data = "";
            while ((line = reader.readLine()) != null) {
                data += line;
            }
            JSONObject jsonObject = JSON.parseObject(data);
            Object res = jsonObject.get("res");
            if(res!=null) {
                List<Legwork> legworks = JSONArray.parseArray(res.toString(), Legwork.class);
//                System.out.println(legworks);
                for (int i = 0; i < legworks.size(); i++) {
                    if (legworks.get(i).getType().equals("代领快递")) {
                        HomeItem homeItem = new HomeItem(legworks.get(i).getId(), 0, legworks.get(i).getInfo(), legworks.get(i).getAddress(), legworks.get(i).getName(), legworks.get(i).getPhone(), legworks.get(i).getMoney(),legworks.get(i).getTime());
                        list.add(homeItem);
                    }
                    if (legworks.get(i).getType().equals("超市代买")) {
                        HomeItem homeItem = new HomeItem(legworks.get(i).getId(), 1, legworks.get(i).getInfo(), legworks.get(i).getAddress(), legworks.get(i).getName(), legworks.get(i).getPhone(), legworks.get(i).getMoney(),legworks.get(i).getTime());
                        list.add(homeItem);
                    }
                    if (legworks.get(i).getType().equals("食堂带饭")) {
                        HomeItem homeItem = new HomeItem(legworks.get(i).getId(), 2, legworks.get(i).getInfo(), legworks.get(i).getAddress(), legworks.get(i).getName(), legworks.get(i).getPhone(), legworks.get(i).getMoney(),legworks.get(i).getTime());
                        list.add(homeItem);
                    }
                }
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static void Update_Password(String username,String password,final SendCodeListener listener){
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        final Request request = new Request.Builder()
                .url("http://"+PC_IP+":8888/api/worker_update")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);

    }
    public static void SendCode(String email,String code,final SendCodeListener listener){
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("code", code)
                .build();
        final Request request = new Request.Builder()
                .url("http://"+PC_IP+":8888/api/sendcode")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);

    }

    public static void CheckWorker(String username,final SendCodeListener listener){
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .build();
        final Request request = new Request.Builder()
                .url("http://"+PC_IP+":8888/api/worker_isexist")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
    }

    public static void getHistoryOrders(String id,final SendCodeListener listener){
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("workerid", id)
                .build();
        final Request request = new Request.Builder()
                .url("http://"+PC_IP+":8888/api/worker/history_orders")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
    }
//    public interface HttpCallBackListener extends Callback {
//        void onFinish(String respose);
//        void onError(Exception e);
//
//    }


    public static void Register(Worker worker,final RegisterListener listener){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username", worker.getUsername())
                .add("password", worker.getPassword())
                .add("nickname",worker.getNickname())
                .add("avatar",worker.getAvatar())
                .build();
        final Request request = new Request.Builder()
                .url("http://" + PC_IP + ":8888/api/register")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
    }

    public static void Login(Worker worker,final LoginListener listener){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("username", worker.getUsername())
                .add("password", worker.getPassword())
                .build();
        final Request request = new Request.Builder()
                .url("http://" + PC_IP + ":8888/api/login")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
        //异步方式
    }


    public static void SaveOrder(Order order ,final SaveOrderListener listener){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("workerid",order.getWorkerid())
                .add("orderid",String.valueOf(order.getHomeItem().getId()))
                .add("status","0")
                .build();
        final Request request = new Request.Builder()
                .url("http://" + PC_IP + ":8888/api/saveLegworkOrder")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
    }


    public static void GetWorkerOrders(String id,final GetWorkerOrdersListener listener){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("workerid",id)
                .build();
        final Request request = new Request.Builder()
                .url("http://" + PC_IP + ":8888/api/getWorkerOrdersById")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
    }

    public static void finishOrder(String workerid,String orderid,final FinishOrderListener listener){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("workerid",workerid)
                .add("orderid",orderid)
                .build();
        final Request request = new Request.Builder()
                .url("http://" + PC_IP + ":8888/api/finishOrder")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
    }

    public static void getOrderGoods(String orderid,final FinishOrderListener listener){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("orderid",orderid)
                .build();
        final Request request = new Request.Builder()
                .url("http://" + PC_IP + ":8888/api/getLegWorkGoods")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
    }


    public static void getmoney(String id,final FinishOrderListener listener){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("workerid",id)
                .build();
        final Request request = new Request.Builder()
                .url("http://" + PC_IP + ":8888/api/monkey_packet")//请求的url
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(listener);
    }


}
