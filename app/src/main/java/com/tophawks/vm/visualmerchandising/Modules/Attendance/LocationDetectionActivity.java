package com.tophawks.vm.visualmerchandising.Modules.Attendance;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tophawks.vm.visualmerchandising.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class LocationDetectionActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, ResultCallback, LocationListener {

    private TextView xc1;
    private TextView yc1;
    private TextView xc2;
    private TextView yc2;
    private TextView status;

    private Button buttonScan;
    private Button showLoc;
    private Button startUpd;

    private IntentIntegrator qrScan;
    private Location mLastLocation;

    private Handler handler;
    private Handler counterHandler;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest locationRequest;
    private ProgressDialog progressDialog;

    private static final String TAG = LocationDetectionActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.still);
        setContentView(R.layout.activity_location_detection);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Location Verification");

        handler = new Handler();
        counterHandler = new Handler();
        progressDialog = new ProgressDialog(this);
        preferences = getSharedPreferences("AttendancePref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        xc1 = (TextView) findViewById(R.id.xcoord1);
        yc1 = (TextView) findViewById(R.id.ycoord1);
        xc2 = (TextView) findViewById(R.id.xcoord2);
        yc2 = (TextView) findViewById(R.id.ycoord2);
        status = (TextView) findViewById(R.id.status);

        qrScan = new IntentIntegrator(this);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        /*startUpd = (Button) findViewById(R.id.startupd);
        startUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePeriodicLocationUpdates();
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
        /*Intent intent = new Intent();
        intent.putExtra("LocationVerified", false);
        setResult(RESULT_OK, intent);
        finish();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent();
        intent.putExtra("LocationVerified", false);
        setResult(RESULT_OK, intent);*/
        finish();
        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(this);
        //Toast.makeText(this, mGoogleApiClient.isConnected() + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
        boolean ans = checkForMatch(xc1.getText().toString(), yc1.getText().toString(), xc2.getText().toString(), yc2.getText().toString());
        if(ans == true){
            progressDialog.dismiss();
            counterHandler.removeCallbacksAndMessages(null);
            status.setText("Location Matched");
            status.setTextColor(Color.GREEN);
            editor.putBoolean("isLocDone", true);
            editor.commit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(status.getText().equals("Location Matched")) {
                        /*Intent intent = new Intent();
                        intent.putExtra("LocationVerified", true);
                        setResult(RESULT_OK, intent);*/
                        Toast.makeText(getApplicationContext(), "Location Result Set", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
                    }
                }
            }, 1000);
        }
        else{
            status.setText("Location Doesnot Match");
            status.setTextColor(Color.RED);
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            xc2.setText(String.valueOf(latitude));
            yc2.setText(String.valueOf(longitude));
            //Toast.makeText(this, latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkForMatch(String xx1, String yy1, String xx2, String yy2){                 //This is the logic for matching location
        double x1 = Double.parseDouble(xx1);
        double y1 = Double.parseDouble(yy1);
        double x2 = Double.parseDouble(xx2);
        double y2 = Double.parseDouble(yy2);
        //Toast.makeText(this, Math.abs(x2-x1) + "," + Math.abs(y2-y1), Toast.LENGTH_SHORT).show();
        if(Math.abs(x2-x1) <= 0.000240 || Math.abs(y2-y1) <= 0.000240){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    protected void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
            startUpd.setText("Stop Updates");
            Log.d(TAG, "Periodic location updates started!");
        } else {
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
            startUpd.setText("Start Updates");
            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(this, 100);
                } catch (IntentSender.SendIntentException e) {
                    //failed to show
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String coord = base64Decode(result.getContents());
                    JSONObject obj = new JSONObject(coord);
                    xc1.setText(obj.getString("x"));
                    yc1.setText(obj.getString("y"));
                    //Location scanned successfully, now er can start GPS scan.
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setMessage("Verifying Location");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            startLocationUpdates();
                        }
                    }, 1000);
                    //StartCounting 60 seconds, if after 60 seconds, location is not found, stop the updates and tell the user to change the location;
                    counterHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            stopLocationUpdates();
                            Toast.makeText(getApplicationContext(), "Location not matched. Please change your location or move to open air and try again.", Toast.LENGTH_SHORT).show();
                        }
                    }, 60000);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e){
                    e.printStackTrace();
                    Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String base64Decode(String res) {
        byte[] data = Base64.decode(res, Base64.DEFAULT);
        String text = new String(data, StandardCharsets.UTF_8);
        return text;
    }

}