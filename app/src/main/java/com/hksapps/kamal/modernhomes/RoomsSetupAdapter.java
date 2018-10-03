package com.hksapps.kamal.modernhomes;

import android.content.Intent;
import android.support.constraint.solver.GoalRow;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hksapps.kamal.modernhomes.models.Room;

import java.util.List;

public class RoomsSetupAdapter extends RecyclerView.Adapter<RoomsSetupAdapter.MyViewHolder> {

    private List<Room> roomList;
    SetupActivity setupActivity;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView room_id, room_name;
        public TextView room_img;
        CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            room_id = (TextView) view.findViewById(R.id.room_id);
            room_name = (TextView) view.findViewById(R.id.room_name);
            room_img = (TextView) view.findViewById(R.id.room_img);
            card_view = (CardView) view.findViewById(R.id.card_view);
            setupActivity = new SetupActivity();
        }
    }


    public RoomsSetupAdapter(List<Room> moviesList) {
        this.roomList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_ui, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Room model = roomList.get(position);
        holder.room_name.setText(model.getRoom_id());

//        holder.room_img.setText(model.getRoom_name().substring(0,1));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    Intent intent = new Intent(MainActivity.this, SwitchesScreen.class);
//                    intent.putExtra("room_id", model.getRoom_id());

                Log.e("room_id", model.getRoom_id());

                setupActivity.selectedRoom = model.getRoom_id();
                setupActivity.constraintlayout.setVisibility(View.VISIBLE);
                setupActivity.recyclerView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}