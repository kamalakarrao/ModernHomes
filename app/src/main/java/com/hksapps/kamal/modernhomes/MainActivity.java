package com.hksapps.kamal.modernhomes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 123;
    private boolean signed_in_cancelled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseAuthenticationProcess();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Signing out", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                AuthUI.getInstance()
                        .signOut(MainActivity.this);
            }
        });
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
        if (id == R.id.action_settings) {
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
                    Toast.makeText(MainActivity.this, "Hello "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                }else {

                      // loadFirebaseLoginScreenUi();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                       Toast.makeText(MainActivity.this, "Loading FirebaseAuth", Toast.LENGTH_SHORT).show();
                    finish();
                }

                }
                // ...
            };
        }




}
