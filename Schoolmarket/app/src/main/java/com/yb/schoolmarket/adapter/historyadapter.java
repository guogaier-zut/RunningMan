package com.yb.schoolmarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yb.schoolmarket.Activities.MainActivity;
import com.yb.schoolmarket.R;
import com.yb.schoolmarket.bean.HomeItem;

import java.util.List;

public class historyadapter extends RecyclerView.Adapter<historyadapter.MyViewHolder> {

    private List<HomeItem> list;
    Context context;

    public historyadapter(List<HomeItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private int[] images = {R.drawable.type_1, R.drawable.type_2, R.drawable.type_3};

    @NonNull
    @Override
    public historyadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.historyorder_item, parent, false);
        historyadapter.MyViewHolder myViewHolder = new historyadapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int type = list.get(position).getType();
        holder.imageView.setBackgroundResource(images[type]);
        holder.textView.setText("下单时间:"+list.get(position).getTime());
        holder.textView1.setText(list.get(position).getInfo());
        holder.textView2.setText("客户姓名:" + list.get(position).getName());
        holder.textView3.setText("联系电话:" + list.get(position).getPhone());
        holder.textView4.setText("派送地址:" + list.get(position).getAddress());
        holder.textView5.setText("服务费:" + Double.toString(list.get(position).getMoney()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;//icon
        TextView textView1;//客户需求
        TextView textView2;//客户
        TextView textView3;//电话
        TextView textView4;//地址
        TextView textView5;//订单价格
        TextView textView;//下单时间

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.history_image);
            textView = itemView.findViewById(R.id.history_time);
            textView1 = itemView.findViewById(R.id.history_info);
            textView2 = itemView.findViewById(R.id.history_user);
            textView3 = itemView.findViewById(R.id.history_phone);
            textView4 = itemView.findViewById(R.id.history_address);
            textView5 = itemView.findViewById(R.id.history_price);
        }
    }
}
