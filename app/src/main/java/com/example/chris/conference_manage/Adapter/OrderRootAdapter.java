package com.example.chris.conference_manage.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.conference_manage.Activity.OrderDisActivity;
import com.example.chris.conference_manage.Class.Order;
import com.example.chris.conference_manage.R;

import java.util.List;

public class OrderRootAdapter extends RecyclerView.Adapter<OrderRootAdapter.ViewHolder>{

    private List<Order> mOrderList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View orderView;
        TextView id;
        TextView room;
        TextView borrower;
        TextView date;

        public ViewHolder(View view) {
            super(view);
            orderView = view;
            id = (TextView) view.findViewById(R.id.order_id);
            room = (TextView) view.findViewById(R.id.room);
            borrower = (TextView) view.findViewById(R.id.borrower);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

    public OrderRootAdapter(List<Order> orderList){
        mOrderList = orderList;
    }

    @Override
    public OrderRootAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_root,parent, false);
        final OrderRootAdapter.ViewHolder holder = new OrderRootAdapter.ViewHolder(view);
        holder.orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Order order = mOrderList.get(position);
                Intent intent = new Intent(view.getContext(), OrderDisActivity.class);
                intent.putExtra("position",order.getId());
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderRootAdapter.ViewHolder holder , int position) {
        Order order = mOrderList.get(position);
        holder.id.setText(order.getId());
        holder.room.setText(order.getRoom());
        holder.borrower.setText(order.getBorrower());
        if(order.getStart_time().equals("开始时间"))
            holder.date.setText("会议日期");
        else
            holder.date.setText(order.getStart_time().substring(0,10));
    }

    @Override
    public int getItemCount(){
        return mOrderList.size();
    }
}
