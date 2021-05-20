package com.yb.schoolmarket.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yb.schoolmarket.bean.HomeItem;
import com.yb.schoolmarket.bean.Legwork;

import java.util.ArrayList;
import java.util.List;

public class ToObjectList {
    public static List<HomeItem> getById(String data) {
        List<HomeItem> list = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(data);
        Object res = jsonObject.get("list");
        if (res != null) {
            List<Legwork> legworks = JSONArray.parseArray(res.toString(), Legwork.class);
//            System.out.println(legworks);
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
        return list;
    }
}
