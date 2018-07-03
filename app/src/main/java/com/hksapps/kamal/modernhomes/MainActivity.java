package com.hksapps.kamal.modernhomes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 1;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
               // startActivity(SignedInActivity.createIntent(this, response));
               // finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    finish();

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
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){

            if(resultCode == RESULT_OK){

                Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();



            }else if(resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Sign in Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
*/

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
    private void loadFirebaseLoginScreenUi(){

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN_IN);
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
                    Toast.makeText(MainActivity.this, "Hello "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                }else {

                    loadFirebaseLoginScreenUi();
                    Toast.makeText(MainActivity.this, "Loading FirebaseAuth", Toast.LENGTH_SHORT).show();

                }

                }
                // ...
            };
        }




}
