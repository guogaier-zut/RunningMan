package com.yb.schoolmarket.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lib.settingview.LSettingItem;
import com.leon.lib.settingview.LSettingItem.OnLSettingItemClick;
import com.yb.schoolmarket.Activities.HistoryOrdersActivity;
import com.yb.schoolmarket.Activities.MainActivity;
import com.yb.schoolmarket.R;
import com.yb.schoolmarket.utils.Network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MeFragment extends BaseFragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_me,container,false);
        ImageView avatar = view.findViewById(R.id.profile_image);
        TextView textView = view.findViewById(R.id.me_nickname);
        LSettingItem item_money  = view.findViewById(R.id.item_money);
        LSettingItem item_history = view.findViewById(R.id.item_history);
        LSettingItem item_about  = view.findViewById(R.id.item_about);
        avatar.setImageResource(R.drawable.avatar);
        textView.setText(MainActivity.nickname);
        item_money.setmOnLSettingItemClick(new OnLSettingItemClick() {
            @Override
            public void click() {
                Network.getmoney(MainActivity.id, new Network.FinishOrderListener() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println(e);
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Looper.prepare();
                        Toast.makeText(view.getContext(),"您已经赚了"+response.body().string()+"元！！！",Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                });
            }
        });

        item_history.setmOnLSettingItemClick(new OnLSettingItemClick() {
            @Override
            public void click() {
                startActivity(new Intent(view.getContext(), HistoryOrdersActivity.class));
            }
        });
        item_about.setmOnLSettingItemClick(new OnLSettingItemClick() {
            @Override
            public void click() {
//                Looper.prepare();
                Toast.makeText(view.getContext(),"当前版本信息: version 1.0.0",Toast.LENGTH_LONG).show();
//                Looper.loop();
            }
        });

        return view;
    }


}
