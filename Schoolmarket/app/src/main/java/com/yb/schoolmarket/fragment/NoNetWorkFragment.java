package com.yb.schoolmarket.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.yb.schoolmarket.Activities.MainActivity;
import com.yb.schoolmarket.R;
import com.yb.schoolmarket.bean.HomeItem;
import com.yb.schoolmarket.utils.Network;

import java.util.List;

public class NoNetWorkFragment extends BaseFragment{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment;
    private Context context;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_no_network,container,false);
        context = view.getContext();
        button = view.findViewById(R.id.button_retry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean b = new Network().checkNetworkAvailable(context);
                System.out.println(b);
                if (b){
                    Toast.makeText(context,"来网了，拼命加载中",Toast.LENGTH_LONG).show();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(MainActivity.no_networkFragment);
                    if(homeFragment==null){
                    homeFragment = new HomeFragment();
                    }
//                    fragmentTransaction.show(homeFragment);
                    fragmentTransaction.replace(R.id.content_layout,homeFragment);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(context,"无网络，呜呜呜",Toast.LENGTH_LONG).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


}
