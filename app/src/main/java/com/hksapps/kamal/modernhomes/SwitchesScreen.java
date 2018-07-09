package com.hksapps.kamal.modernhomes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hksapps.kamal.modernhomes.models.Switch;
import com.squareup.picasso.Picasso;

public class SwitchesScreen extends AppCompatActivity {


    private String room_id;
    private RecyclerView SwitchRecyclerView;
    private FirebaseRecyclerAdapter<Switch, SwitchHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        room_id = getIntent().getStringExtra("room_id");

        SwitchRecyclerView = (RecyclerView) findViewById(R.id.switch_recyclerview);
//        mAdapter = new MoviesAdapter(movieList);
        SwitchRecyclerView.hasFixedSize();
        SwitchRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        SwitchRecyclerView.setLayoutManager(mLayoutManager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void LoadDataToRecyclerView() {


        if (FirebaseAuth.getInstance().getUid() != null && room_id != null && room_id.length() > 0) {
            // Toast.makeText(this, "UUID Exist!", Toast.LENGTH_SHORT).show();

            ;
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Devices").child(room_id).child("switches");
            Log.e("data", String.valueOf(query));

            FirebaseRecyclerOptions<Switch> options =
                    new FirebaseRecyclerOptions.Builder<Switch>()
                            .setQuery(query, Switch.class)
                            .build();

            adapter = new FirebaseRecyclerAdapter<Switch, SwitchHolder>(options) {
                @Override
                public SwitchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.switch_recycler_ui, parent, false);

//                    Toast.makeText(MainActivity.this, "Arrived 444", Toast.LENGTH_SHORT).show();

                    return new SwitchHolder(view);
                }

                @Override
                protected void onBindViewHolder(SwitchHolder holder, final int position, final Switch model) {
//                    Toast.makeText(MainActivity.this, "Arrived" +model.getRoom_id()+" "+model.getRoom_name(), Toast.LENGTH_SHORT).show();
                    //holder.room_id.setText(model.getRoom_id());

                    holder.switch_name.setText(model.getName());
                    try {
                        if (model.getSwitch_img().length() > 0 && model.getSwitch_img() != null) {

                            Picasso.get().load(model.getSwitch_img()).into(holder.switch_img);

                        }
                    } catch (Exception e) {

                    }


                    if (model.getStatus().equals("on")) {

                        holder.switch_status.setChecked(true);
                    } else if (model.getStatus().equals("off")) {

                        holder.switch_status.setChecked(false);

                    }

                    holder.switch_card_view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            return false;
                        }
                    });


                    holder.switch_card_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Devices").child(room_id).child("switches");

                            if (model.getStatus().equals("on")) {

                                ref.child("switch" + String.valueOf(position + 1)).child("status").setValue("off");

                            } else if (model.getStatus().equals("off")) {

                                ref.child("switch" + String.valueOf(position + 1)).child("status").setValue("on");
                            }
                        }
                    });


                    // Bind the Chat object to the ChatHolder
                    // ...
                }


            };
            adapter.startListening();
            SwitchRecyclerView.setAdapter(adapter);
            /*adapter.startListening();*/

//            adapter.notifyDataSetChanged();

        } else {
            Toast.makeText(this, "No Room Id", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadDataToRecyclerView();

    }

    @Override
    protected void onStop() {
        super.onStop();


        adapter.stopListening();
    }

}
