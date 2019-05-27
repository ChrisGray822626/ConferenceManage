package com.example.chris.conference_manage.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.conference_manage.Activity.StaffDisActivity;
import com.example.chris.conference_manage.Class.Staff;
import com.example.chris.conference_manage.R;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private List<Staff> mStaffList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View staffView;
        TextView id;
        TextView name;
        TextView telephone;
        TextView rank;

        public ViewHolder(View view) {
            super(view);
            staffView = view;
            id = (TextView) view.findViewById(R.id.staff_id);
            name = (TextView) view.findViewById(R.id.name);
            telephone = (TextView) view.findViewById(R.id.telephone);
            rank = (TextView) view.findViewById(R.id.rank);
        }
    }

    public StaffAdapter (List<Staff> staffList){
        mStaffList = staffList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff,parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.staffView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Staff staff = mStaffList.get(position);
                Intent intent = new Intent(view.getContext(), StaffDisActivity.class);
                intent.putExtra("position",staff.getId() + "");
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder , int position) {
        Staff staff = mStaffList.get(position);
        holder.id.setText(staff.getId());
        holder.name.setText(staff.getName());
        holder.telephone.setText(staff.getTelephone());
        holder.rank.setText(staff.getRank());
    }

    @Override
    public int getItemCount(){
        return mStaffList.size();
    }
}
