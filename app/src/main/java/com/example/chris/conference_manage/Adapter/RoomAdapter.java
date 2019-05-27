package com.example.chris.conference_manage.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.conference_manage.Activity.RoomDisActivity;
import com.example.chris.conference_manage.Class.Room;
import com.example.chris.conference_manage.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{

    private List<Room> mRoomList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View roomView;
        TextView id;
        TextView people;
        TextView projector;
        TextView computer;
        TextView microphone;

        public ViewHolder(View view) {
            super(view);
            roomView = view;
            id = (TextView)view.findViewById(R.id.room_id);
            people = (TextView)view.findViewById(R.id.people);
            projector = (TextView)view.findViewById(R.id.projector);
            computer = (TextView)view.findViewById(R.id.computer);
            microphone = (TextView)view.findViewById(R.id.microphone);
        }
    }

    public RoomAdapter(List<Room> roomList) {
        mRoomList = roomList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room,parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.roomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Room room = mRoomList.get(position);
                Intent intent = new Intent(view.getContext(),RoomDisActivity.class);
                intent.putExtra("position",room.getId());
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder ,int position) {
        Room room = mRoomList.get(position);
        holder.id.setText(room.getId());
        holder.people.setText(room.getPeople());
        holder.projector.setText(room.getProjector());
        holder.computer.setText(room.getComputer());
        holder.microphone.setText(room.getMicrophone());
    }

    @Override
    public int getItemCount(){
        return mRoomList.size();
    }
}
