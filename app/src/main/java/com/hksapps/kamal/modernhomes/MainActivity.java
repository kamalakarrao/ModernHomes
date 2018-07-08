package com.hksapps.kamal.modernhomes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hksapps.kamal.modernhomes.models.Room;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 123;
    private boolean signed_in_cancelled = false;
    private RecyclerView RoomRecyclerView;
    FirebaseRecyclerAdapter<Room, RoomHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

            }
        });
    }

    private void LoadDataToRecyclerView(){


        if(FirebaseAuth.getInstance().getUid()!=null){
            Toast.makeText(this, "UUID Exist!", Toast.LENGTH_SHORT).show();

            Log.e("uuid",FirebaseAuth.getInstance().getUid());
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users").child(FirebaseAuth.getInstance().getUid()).child("rooms");
            Log.e("data", String.valueOf(query));
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Log.e("dagta",dataSnapshot.getKey()+"  "+dataSnapshot.getValue());
//                    try {
//                        JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
//                         new Room(jsonObject.getString("room_id"),jsonObject.getString("room_name"));;
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

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

                    Picasso.get().load(model.getRoom_img()).into(holder.room_img);

//                    holder.room_layout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Toast.makeText(MainActivity.this, model.getRoom_id(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    holder.card_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, model.getRoom_id(), Toast.LENGTH_SHORT).show();
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

        }else {
            Toast.makeText(this, "No UUID", Toast.LENGTH_SHORT).show();
        }
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
    }






    private void FirebaseAuthenticationProcess(){

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              //  Toast.makeText(MainActivity.this, "Inside FirebaseAuth", Toast.LENGTH_SHORT).show();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Load LoginScreen
                   // finish();
                  // Intent i = new Intent(MainActivity.this,MainActivity.class);
                   //startActivity(i);
                    DatabaseReference database =  FirebaseDatabase.getInstance().getReference();
                   database.child("Users").child(user.getUid()).child("user_id").setValue(user.getUid());
                   database.child("Users").child(user.getUid()).child("email_id").setValue(user.getEmail());

                    Toast.makeText(MainActivity.this, "Hello "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
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
