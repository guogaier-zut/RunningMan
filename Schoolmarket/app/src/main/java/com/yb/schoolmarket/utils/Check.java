package com.yb.schoolmarket.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yb.schoolmarket.bean.Worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

public class Check {

    public  String getCode(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        Object code = jsonObject.get("code");
        return code.toString();
    }
    public  Worker getInfo(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        Object worker2 = jsonObject.get("worker");
        JSONObject jsonObject1 = JSON.parseObject(worker2.toString());
        String un = jsonObject1.get("username").toString();
        String pw = jsonObject1.get("password").toString();
        String nn = jsonObject1.get("nickname").toString();
        int id =Integer.parseInt( jsonObject1.get("id").toString());
        String at = jsonObject1.get("avatar").toString();
        Worker worker = new Worker(id,un,pw,nn,at);
        return worker;
    }

//    public static void main(String[] args) {
//        String json = "{\"code\":200,\"message\":\"登陆成功\",\"worker\":{\"id\":1,\"username\":\"demo1\",\"password\":\"demo\",\"nickname\":\"demo\",\"avatar\":\"demo\"}}";
//        System.out.println(new Check().getInfo(json));
//    }

    public String saveToLocal(Context context,String json){

        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String fileName = "userinfo.txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory()+"/msm/path/";
                File dir = new File(path);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(json.getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Looper.prepare();
            Log.w("保存本地失败",e.toString());
            Toast.makeText(context,"保存本地失败",Toast.LENGTH_LONG).show();
            Looper.loop();
        }
        return null;
    }

    public HashMap<String,String> getCache(){
        HashMap<String,String> hashMap = new HashMap();
        String path = Environment.getExternalStorageDirectory()+"/msm/path/userinfo.txt";
//        String path = Environment.DIRECTORY_DCIM+"/msm/path/userinfo.txt";
        System.out.println(path);
        File dir = new File(path);
        if(dir.exists()){
            try {
                File file = new File(path);
                int length=(int)file.length();
                byte[] buff=new byte[length];
                FileInputStream fis=new FileInputStream(file);
                fis.read(buff);
                fis.close();
                String result=new String(buff,"UTF-8");
                System.out.println(result);
                Worker info = new Check().getInfo(result);
                if (new Check().getCode(result).equals("200")){
                    hashMap.put("status","true");
                    hashMap.put("worker_nickname",info.getNickname());
                    hashMap.put("worker_avatar",info.getAvatar());
                    hashMap.put("worker_id",String.valueOf(info.getId()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hashMap;
    }

    public boolean deleteFile(){
        String path = Environment.getExternalStorageDirectory()+"/msm/path/userinfo.txt";
        File file = new File(path);
        boolean delete = file.delete();
        return delete;
    }
}
