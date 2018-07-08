package com.hksapps.kamal.modernhomes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


    public class RoomHolder extends    RecyclerView.ViewHolder {
        public TextView room_id, room_name;
        public ImageView room_img;



        public RoomHolder(View view) {
            super(view);
        //    title = (TextView) view.findViewById(R.id.title);
            room_id = (TextView) view.findViewById(R.id.room_id);
            room_name = (TextView) view.findViewById(R.id.room_name);
            room_img = (ImageView) view.findViewById(R.id.room_img);
        }
    }

