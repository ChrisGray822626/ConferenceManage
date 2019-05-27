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

public class OrderStaffAdapter extends RecyclerView.Adapter<OrderStaffAdapter.ViewHolder>{

    private List<Order> mOrderList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View orderView;
        TextView room;
        TextView topic;
        TextView date;

        public ViewHolder(View view) {
            super(view);
            orderView = view;
            room = (TextView) view.findViewById(R.id.order_room);
            topic = (TextView) view.findViewById(R.id.order_topic);
            date = (TextView) view.findViewById(R.id.order_date);
        }
    }

    public OrderStaffAdapter(List<Order> orderList){
        mOrderList = orderList;
    }

    @Override
    public OrderStaffAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_staff,parent, false);
        final OrderStaffAdapter.ViewHolder holder = new OrderStaffAdapter.ViewHolder(view);
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
    public void onBindViewHolder(OrderStaffAdapter.ViewHolder holder , int position) {
        Order order = mOrderList.get(position);
        holder.room.setText(order.getRoom());
        holder.topic.setText(order.getTopic());
        holder.date.setText(order.getStart_time().substring(0,10));
    }

    @Override
    public int getItemCount(){
        return mOrderList.size();
    }
}
