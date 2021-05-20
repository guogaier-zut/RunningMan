package com.yb.schoolmarket.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yb.schoolmarket.R;
import com.yb.schoolmarket.adapter.historyadapter;

import com.yb.schoolmarket.bean.HomeItem;

import com.yb.schoolmarket.utils.Network;
import com.yb.schoolmarket.utils.ToObjectList;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class HistoryOrdersActivity extends AppCompatActivity {
    private List<HomeItem> mlist;
    private RecyclerView mRecycleView;
    private historyadapter mAdapter;//适配器
    private LinearLayoutManager mLinearLayoutManager;//布局管理器
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initData();
    }

    private void initData() {
        Network.getHistoryOrders(MainActivity.id, new Network.SendCodeListener() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                mlist = new ArrayList<HomeItem>();
                List<HomeItem> byId = ToObjectList.getById(response.body().string());
                mlist = byId;
                Log.i("Inner List",mlist.toString());
                mRecycleView = findViewById(R.id.history_recycler);
                mLinearLayoutManager = new LinearLayoutManager(HistoryOrdersActivity.this, LinearLayoutManager.VERTICAL, false);
                mRecycleView.setLayoutManager(mLinearLayoutManager);
                Log.i("Outter List",mlist.toString());
                mAdapter = new historyadapter(mlist, HistoryOrdersActivity.this);
                mRecycleView.setAdapter(mAdapter);
            }
        });
    }
}
