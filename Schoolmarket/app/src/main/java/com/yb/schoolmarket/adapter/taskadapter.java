package com.yb.schoolmarket.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.net.Uri;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yb.schoolmarket.Activities.MainActivity;
import com.yb.schoolmarket.R;
import com.yb.schoolmarket.bean.HomeItem;
import com.yb.schoolmarket.utils.Network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class taskadapter  extends RecyclerView.Adapter<taskadapter.MyViewHolder> {
    private List<HomeItem> list;
    Context context;
    MainActivity mainactivity = new MainActivity();
    private int[] images = {R.drawable.type_1, R.drawable.type_2, R.drawable.type_3};
    public taskadapter(List<HomeItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public taskadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        taskadapter.MyViewHolder myViewHolder = new taskadapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull taskadapter.MyViewHolder holder, int position) {
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

    public class MyViewHolder  extends RecyclerView.ViewHolder {
        ImageView imageView;//icon
        TextView textView1;//客户需求
        TextView textView2;//客户
        TextView textView3;//电话
        TextView textView4;//地址
        TextView textView5;//订单价格
        TextView textView;//下单时间

        Button button1, button2,button3;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("================获取组件===================");

            imageView = itemView.findViewById(R.id.task_image);
            textView = itemView.findViewById(R.id.task_time);
            textView1 = itemView.findViewById(R.id.task_info);
            textView2 = itemView.findViewById(R.id.task_user);
            textView3 = itemView.findViewById(R.id.task_phone);
            textView4 = itemView.findViewById(R.id.task_address);
            textView5 = itemView.findViewById(R.id.task_price);
            button1 = itemView.findViewById(R.id.task_finish);
            button2 = itemView.findViewById(R.id.task_connect);
            button3 = itemView.findViewById(R.id.task_deliver);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,textView1.getText(),Toast.LENGTH_SHORT).show();
//                    list.remove(getAdapterPosition());
                    Dialog_receive();
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,textView2.getText(),Toast.LENGTH_SHORT).show();
                    Dialog_connect();
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button3.setEnabled(false);
                    button3.setText("派送中");
                    button3.setBackgroundColor(Color.rgb(190,190,190));
                    Network.getOrderGoods(String.valueOf(list.get(getAdapterPosition()).getId()), new Network.FinishOrderListener() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            System.out.println(e);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if(response.body().string().equals("true")){
                                Looper.prepare();
                                Toast.makeText(context,"请尽快派送！",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    });
                }
            });
        }

        private void Dialog_connect() {
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(context);
            normalDialog.setIcon(R.drawable.dialog);
            normalDialog.setTitle("客户信息");
            normalDialog.setMessage("\n" + textView2.getText() + "\n" + textView3.getText());
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(callToClient!=null){
                                callToClient.onCall(itemView,list.get(getLayoutPosition()).getPhone());
                            }
                        }
                    });
            normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //...To-do
//                            Toast.makeText(context,"未接单",Toast.LENGTH_SHORT).show();
                        }
                    });
            // 显示
            normalDialog.show();
        }


        private void Dialog_receive() {
            /* @setIcon 设置对话框图标
             * @setTitle 设置对话框标题
             * @setMessage 设置对话框消息提示
             * setXXX方法返回Dialog对象，因此可以链式设置属性
             */
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(context);
            normalDialog.setIcon(R.drawable.dialog);
            normalDialog.setTitle("提示");
            normalDialog.setMessage("确定已完成此订单吗？\n" + textView1.getText());
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FinishOrder(getAdapterPosition());
                        }
                    });
            normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //...To-do
                            Toast.makeText(context, "请尽快完成本订单!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
            // 显示
            normalDialog.show();
        }




        /**
         * 接单的操作
         * 耗时操作放在子线程中
         */

        /**
         * 联系客户
         * 调用电话应用
         */
        private void Call() {
//            Toast.makeText(context,textView2.getText(),Toast.LENGTH_LONG).show();

            if (ContextCompat.checkSelfPermission(mainactivity, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 那就向系统申请权限...
                ActivityCompat.requestPermissions(mainactivity,new String[]{Manifest.permission.CALL_PHONE},1);
            }
//	用户已经授权过申请
            else {
                // 发起拨号
                String phone_number = textView3.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
                context.startActivity(intent);
            }
        }

    }

    private void FinishOrder(final int postion) {
        String workerid = MainActivity.id;
        String orderid = String.valueOf(list.get(postion).getId());
        System.out.println(workerid+"===="+orderid);
        Network.finishOrder(workerid, orderid, new Network.FinishOrderListener() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("true")){
//                    list.remove(postion);
                    Looper.prepare();
                    Toast.makeText(context,"已完成！！",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
        list.remove(postion);
    }

    public interface CallToClient{

        public void onCall(View view ,String phone);
    }

    private taskadapter.CallToClient callToClient;

    /**
     * 设置监听
     * @param callToClient
     */
    public void setCallToClient(taskadapter.CallToClient callToClient) {
        this.callToClient = callToClient;
    }
}
