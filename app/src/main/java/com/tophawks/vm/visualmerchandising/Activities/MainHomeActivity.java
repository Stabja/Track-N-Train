package com.tophawks.vm.visualmerchandising.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tophawks.vm.visualmerchandising.Modules.Attendance.AttendanceHomeActivity;
import com.tophawks.vm.visualmerchandising.Modules.VisualMerchandising.AddProduct;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.Training.MainActivity;

public class MainHomeActivity extends AppCompatActivity {

    private Button attendanceButton;
    private Button salesButton;
    private Button stockButton;
    private Button vmButton;
    private Button trainingButton;

    private int permissionRecord;
    private String [] permissions = {
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.CHANGE_NETWORK_STATE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.MODIFY_PHONE_STATE,
            android.Manifest.permission.CAPTURE_AUDIO_OUTPUT,
            android.Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.still);
        ActivityCompat.requestPermissions(this, permissions, 100);
        setContentView(R.layout.activity_main_home);

        attendanceButton = (Button) findViewById(R.id.attendancebutton);
        salesButton = (Button) findViewById(R.id.salesbutton);
        stockButton = (Button) findViewById(R.id.stockbutton);
        vmButton = (Button) findViewById(R.id.vmbutton);
        trainingButton = (Button) findViewById(R.id.train);

        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainHomeActivity.this, AttendanceHomeActivity.class);
                startActivity(intent);
            }
        });

        salesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.tophawks.vm.visualmerchandising.SalesMgmt.MainActivity.class);
                startActivity(intent);
            }
        });

        stockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StockManagementHomePage.class);
                startActivity(intent);
            }
        });

        vmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProduct.class);
                startActivity(intent);
            }
        });

        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10){
            permissionRecord = grantResults[0] = PackageManager.PERMISSION_GRANTED;
            int length = grantResults.length;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
    }

}