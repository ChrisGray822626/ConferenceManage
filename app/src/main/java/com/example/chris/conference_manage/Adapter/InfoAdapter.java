package com.example.chris.conference_manage.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.conference_manage.Class.Info;
import com.example.chris.conference_manage.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private List<Info> mInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView Info;
        TextView Data;

        public ViewHolder(View view){
            super(view);
            Info = (TextView) view.findViewById(R.id.info);
            Data = (TextView) view.findViewById(R.id.data);
        }
    }

    public InfoAdapter(List<Info> infoList){
        mInfoList = infoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Info info = mInfoList.get(position);
        holder.Info.setText(info.getInfo());
        holder.Data.setText(info.getData());
    }

    @Override
    public int getItemCount(){
        return mInfoList.size();
    }
}
