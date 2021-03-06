package com.hksapps.kamal.modernhomes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hksapps.kamal.modernhomes.models.Room;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 123;
    private boolean signed_in_cancelled = false;
    private RecyclerView RoomRecyclerView;

    FirebaseRecyclerAdapter<Room, RoomHolder> adapter;
    LinearLayout ll_empty;
    ProgressDialog dialog;
    private TextView text_notice;
    private  Button add_room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



         ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
         add_room = (Button) findViewById(R.id.add_room);

        text_notice = (TextView) findViewById(R.id.text_notice);

         dialog = new ProgressDialog(this);




        FirebaseAuthenticationProcess();

        RoomRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        mAdapter = new MoviesAdapter(movieList);
        RoomRecyclerView.hasFixedSize();
        RoomRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RoomRecyclerView.setLayoutManager(mLayoutManager);
       //n RoomRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ScannerActivity.class);
                startActivity(intent);
            }
        });


        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,ScannerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LoadDataToRecyclerView(){

        if(FirebaseAuth.getInstance().getUid()!=null){

            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("rooms");

            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        RoomRecyclerView.setVisibility(View.VISIBLE);
                        ll_empty.setVisibility(View.GONE);

                    }else{
                        RoomRecyclerView.setVisibility(View.GONE);
                        ll_empty.setVisibility(View.VISIBLE);
                        add_room.setVisibility(View.VISIBLE);
                        text_notice.setText("You haven't added any rooms yet");


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                   // Toast.makeText(MainActivity.this, "Cancelled ", Toast.LENGTH_SHORT).show();

                }
            });

            //
            // Toast.makeText(this, "UUID Exist!", Toast.LENGTH_SHORT).show();

            Log.e("uuid",FirebaseAuth.getInstance().getUid());
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users").child(FirebaseAuth.getInstance().getUid()).child("rooms");
            Log.e("data", String.valueOf(query));

            FirebaseRecyclerOptions<Room> options =
                    new FirebaseRecyclerOptions.Builder<Room>()
                            .setQuery(query, Room.class)
                            .build();

            adapter = new FirebaseRecyclerAdapter<Room, RoomHolder>(options) {
                @Override
                public RoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recycler_ui, parent, false);

//                    Toast.makeText(MainActivity.this, "Arrived 444", Toast.LENGTH_SHORT).show();

                    return new RoomHolder(view);
                }

                @Override
                protected void onBindViewHolder(RoomHolder holder, int position, final Room model) {
//                    Toast.makeText(MainActivity.this, "Arrived" +model.getRoom_id()+" "+model.getRoom_name(), Toast.LENGTH_SHORT).show();
                    //holder.room_id.setText(model.getRoom_id());
                    holder.room_name.setText(model.getRoom_name());

                    holder.room_img.setText(model.getRoom_name().substring(0,1));

                    /*if(model.getRoom_img().length()>5)
                        Picasso.get().load(model.getRoom_img()).into(holder.room_img);
                    else
                        holder.room_img.setImageResource(R.drawable.light);*/

                    holder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            AlertDialog diaBox = AskOption(model.getRoom_id());
                            diaBox.show();

                            return false;
                        }
                    });

                    holder.card_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          //  Toast.makeText(MainActivity.this, model.getRoom_id(), Toast.LENGTH_SHORT).show();

                            if(isNetworkConnected()) {
                                RoomRecyclerView.setVisibility(View.VISIBLE);
                                ll_empty.setVisibility(View.GONE);

                                Intent intent = new Intent(MainActivity.this, SwitchesScreen.class);
                                intent.putExtra("room_id", model.getRoom_id());
                                startActivity(intent);

                                // Toast.makeText(this, "No UUID", Toast.LENGTH_SHORT).show();
                            }else {

                                RoomRecyclerView.setVisibility(View.GONE);
                                ll_empty.setVisibility(View.VISIBLE);
                                text_notice.setText("Check your Internet Connection");
                                add_room.setVisibility(View.GONE);

                            }



                        }
                    });




                    // Bind the Chat object to the ChatHolder
                    // ...
                }



            };
            adapter.startListening();
            RoomRecyclerView.setAdapter(adapter);
            /*adapter.startListening();*/

//            adapter.notifyDataSetChanged();

        }
        if(isNetworkConnected()) {
            RoomRecyclerView.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);



           // Toast.makeText(this, "No UUID", Toast.LENGTH_SHORT).show();
        }else {

            RoomRecyclerView.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
            text_notice.setText("Check your Internet Connection");
            add_room.setVisibility(View.GONE);

        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {



            AuthUI.getInstance()
                    .signOut(MainActivity.this);
            return true;
        }
        if (id == R.id.configure_device) {

startActivity(new Intent(MainActivity.this,WifiActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



 /*   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
               // startActivity(SignedInActivity.createIntent(this, response));
               // finish();
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            }


            else {
                // Sign in failed
            //    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();

                if (response == null) {
                    // User pressed back button

                    // showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                  //  showSnackbar(R.string.no_internet_connection);

                    return;
                }

                //showSnackbar(R.string.unknown_error);
              //  Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }
 */

  /*  @Override
    public void onBackPressed() {

        Toast.makeText(this, "Exiting", Toast.LENGTH_SHORT).show();
        finish();
        System.exit(0);
        super.onBackPressed();

    }*/
/*  @Override
  protected void onResume() {
      super.onResume();
      mAuth.addAuthStateListener(mAuthListener);
  }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        LoadDataToRecyclerView();

    }
    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
       // adapter.stopListening();
    }



    private AlertDialog AskOption(final String room_id)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete this room")
                .setIcon(R.drawable.baseline_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("rooms");
                        ref.child(room_id).removeValue();

                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }



    private void FirebaseAuthenticationProcess(){

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              //  Toast.makeText(MainActivity.this, "Inside FirebaseAuth", Toast.LENGTH_SHORT).show();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    dialog.setMessage("Please wait");
                    dialog.show();
                    //Load LoginScreen
                   // finish();
                  // Intent i = new Intent(MainActivity.this,MainActivity.class);
                   //startActivity(i);
                    DatabaseReference database =  FirebaseDatabase.getInstance().getReference();
                   database.child("Users").child(user.getUid()).child("user_id").setValue(user.getUid());
                   database.child("Users").child(user.getUid()).child("email_id").setValue(user.getEmail());

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                  //  Toast.makeText(MainActivity.this, "Hello "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                }else {

                      // loadFirebaseLoginScreenUi();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                     //  Toast.makeText(MainActivity.this, "Loading FirebaseAuth", Toast.LENGTH_SHORT).show();
                    finish();
                }

                }
                // ...
            };
        }




}
