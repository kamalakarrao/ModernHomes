package com.hksapps.kamal.modernhomes;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScannerActivity extends AppCompatActivity {
    SurfaceView cameraprev;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Button btn_configure;
    LinearLayout ll_scannerlayout,ll_deviceactivation;
    TextView tv_device_id;
    Spinner spinner_roomtype;
    final int RequestCameraPermissionId = 1001;
    private int i=0;
    ArrayList<String> categories;
    private String[] sampleString;
    private ArrayAdapter dataAdapter;
    private MaterialBetterSpinner materialDesignSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categories= new ArrayList<>();
        categories.add("Select");
        ll_deviceactivation = (LinearLayout) findViewById(R.id.ll_deviceactivation);
        ll_scannerlayout = (LinearLayout) findViewById(R.id.ll_scannerlayout);
        tv_device_id = (TextView) findViewById(R.id.tv_device_id);
        btn_configure = (Button) findViewById(R.id.btn_configure);
//        spinner_roomtype= (Spinner) findViewById(R.id.spinner_roomtype);
//         dataAdapter = new ArrayAdapter(ScannerActivity.this, android.R.layout.simple_spinner_item, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
////        spinner_roomtype.setAdapter(dataAdapter);
//        dataAdapter.notifyDataSetChanged();
        ll_scannerlayout.setVisibility(View.VISIBLE);
        ll_deviceactivation.setVisibility(View.GONE);



        cameraprev = (SurfaceView) findViewById(R.id.cameraprev);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();
        cameraprev.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(ScannerActivity.this,new String[]{android.Manifest.permission.CAMERA},RequestCameraPermissionId);
                    return;
                }
                try {
                    cameraSource.start(cameraprev.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes= detections.getDetectedItems();
                if (qrcodes.size() != 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            final String qrcode = qrcodes.valueAt(0).displayValue;

                            cameraSource.stop();

                            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.child("Devices").child(qrcode).exists()){
                                        if (!dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).child("rooms").child(qrcode).exists()) {


                                            Toast.makeText(ScannerActivity.this, "Device Exist", Toast.LENGTH_SHORT).show();
                                            tv_device_id.setText(qrcode);
                                            ll_scannerlayout.setVisibility(View.GONE);
                                            ll_deviceactivation.setVisibility(View.VISIBLE);

                                            String[] categoriess = getSpinnerData();
                                            if (categories != null) {

                                                List<String> spinnerArray = categories;
                                                categories.remove(0);
                                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ScannerActivity.this,
                                                        android.R.layout.simple_dropdown_item_1line, categories);
                                                materialDesignSpinner = (MaterialBetterSpinner)
                                                        findViewById(R.id.android_material_design_spinner);
                                                materialDesignSpinner.setAdapter(arrayAdapter);

                                            }
                                        }else {
                                            Toast.makeText(ScannerActivity.this, "This Room is Already Configured with Your Account", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }else {
                                        Toast.makeText(ScannerActivity.this, "Device Doesn't Exist", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(ScannerActivity.this, qrcodes.valueAt(0).displayValue, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        btn_configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (materialDesignSpinner.getText()!=null&&materialDesignSpinner.getText().length()>0){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("rooms").child(tv_device_id.getText().toString());
                    ref.child("room_id").setValue(tv_device_id.getText().toString());
                    ref.child("room_name").setValue(materialDesignSpinner.getText().toString());
                    ref.child("room_img").setValue("none");

                    finish();
                }else {
                    Toast.makeText(ScannerActivity.this, "Please Select Room Type !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String[] getSpinnerData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("defaults").child("room_types");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("data",dataSnapshot.getChildren().toString());
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int length = (int) dataSnapshot.getChildrenCount();
                 sampleString = new String[length];
                while(i < length) {
                    sampleString[i] = iterator.next().getValue().toString();
                    Log.d(Integer.toString(i), sampleString[i]);
                    categories.add(sampleString[i]);
                    i++;
                }
                Log.e("spinner  ",sampleString.toString());


//                dataSnapshot.getChildren()
//                String[] s = (String[]) dataSnapshot.getValue();
//                Log.e("array",s.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return sampleString;
    }

}
