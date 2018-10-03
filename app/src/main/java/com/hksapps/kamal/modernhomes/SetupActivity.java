package com.hksapps.kamal.modernhomes;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hksapps.kamal.modernhomes.models.Room;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends AppCompatActivity {
    EditText edit_pwd,edit_name;
    TextView tv_wrongname,tv_invalidpwd;
    public static RecyclerView recyclerView;
    public static ConstraintLayout constraintlayout;
    ProgressBar progressBar;
    Button button;
    String selectedRoom;
    private List<Room> roomList = new ArrayList<>();
    private RoomsSetupAdapter mAdapter;
//    FirebaseRecyclerAdapter<Room, RoomsSetupAdapter> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new RoomsSetupAdapter(roomList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        constraintlayout = (ConstraintLayout) findViewById(R.id.constraintlayout);
        button = (Button) findViewById(R.id.button);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
        tv_invalidpwd = (TextView) findViewById(R.id.tv_invalidpwd);
        tv_wrongname= (TextView) findViewById(R.id.tv_wrongname);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_wrongname.setVisibility(View.INVISIBLE);
        tv_invalidpwd.setVisibility(View.INVISIBLE);


        loadData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_wrongname.setVisibility(View.INVISIBLE);
                tv_invalidpwd.setVisibility(View.INVISIBLE);
                if (edit_name.getText().toString()!=null && edit_name.getText().toString().length()>0){
                    if (edit_pwd.getText().toString()!=null && edit_pwd.getText().toString().length()>7){
                        String name = edit_name.getText().toString();
                        String password = edit_pwd.getText().toString();
                        updateData(name,password);
                        Toast.makeText(SetupActivity.this, "All right !", Toast.LENGTH_SHORT).show();

                    }else {
                        tv_invalidpwd.setVisibility(View.VISIBLE);
                    }
                }else {
                    tv_wrongname.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadData() {
        final List<String> rooms  = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("rooms");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot contact : contactChildren) {
                    Log.d("contact:: ", contact.getKey());
                    rooms.add(contact.getKey());

                }
                if (rooms.size()>0){
                    for(int i=0;i<rooms.size();i++){
                        Room room = new Room(rooms.get(i));
                        roomList.add(room);
                    }
                    mAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(SetupActivity.this, "Nothing to Show !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateData(String name, String password) {
//        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("Devices").child(room_id).child("switches");

    }

}
