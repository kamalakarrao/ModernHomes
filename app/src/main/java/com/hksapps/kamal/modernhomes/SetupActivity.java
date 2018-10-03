package com.hksapps.kamal.modernhomes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetupActivity extends AppCompatActivity {
    EditText edit_pwd,edit_name;
    TextView tv_wrongname,tv_invalidpwd;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        button = (Button) findViewById(R.id.button);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
        tv_invalidpwd = (TextView) findViewById(R.id.tv_invalidpwd);
        tv_wrongname= (TextView) findViewById(R.id.tv_wrongname);
        tv_wrongname.setVisibility(View.INVISIBLE);
        tv_invalidpwd.setVisibility(View.INVISIBLE);

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

    private void updateData(String name, String password) {
//        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("Devices").child(room_id).child("switches");
    }
}
