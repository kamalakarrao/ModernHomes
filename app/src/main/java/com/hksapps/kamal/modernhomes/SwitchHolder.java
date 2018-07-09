package com.hksapps.kamal.modernhomes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SwitchHolder extends RecyclerView.ViewHolder {

   public TextView switch_name;
   public Switch switch_status;
   public CardView switch_card_view;
   public ImageView switch_img;
    public SwitchHolder(View itemView) {
        super(itemView);

        switch_name = (TextView) itemView.findViewById(R.id.switch_name);
        switch_status  = (Switch) itemView.findViewById(R.id.switch_toggle);
        switch_card_view = (CardView) itemView.findViewById(R.id.switch_card_view);
        switch_img = (ImageView) itemView.findViewById(R.id.switch_img);
    }


}
