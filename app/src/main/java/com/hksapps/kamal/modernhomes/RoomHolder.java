package com.hksapps.kamal.modernhomes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


    public class RoomHolder extends    RecyclerView.ViewHolder {
        public TextView room_id, room_name;
        public TextView room_img;
        public LinearLayout room_layout;
        CardView card_view;



        public RoomHolder(View view) {
            super(view);
        //    title = (TextView) view.findViewById(R.id.title);
            room_id = (TextView) view.findViewById(R.id.room_id);
            room_name = (TextView) view.findViewById(R.id.room_name);
            room_img = (TextView) view.findViewById(R.id.room_img);
//            room_layout = (LinearLayout) view.findViewById(R.id.room_layout);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }

