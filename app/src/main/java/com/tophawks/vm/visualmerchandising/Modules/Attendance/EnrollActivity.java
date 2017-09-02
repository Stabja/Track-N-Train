package com.tophawks.vm.visualmerchandising.Modules.Attendance;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tophawks.vm.visualmerchandising.R;

import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class EnrollActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 50;

    private SharedPreferences sPref;
    private Editor editor;
    private Handler handler;
    public static Kairos myKairos;
    public KairosListener listener;

    private String subjectId = "myimage";
    private String galleryId;
    private String selector = "FULL";
    private String multipleFaces = "false";
    private String minHeadScale = "0.25";
    private String threshold = "0.75";
    private String maxNumResults = "25";

    private TextView galleryName;
    private Button start;
    private Intent cameraIntent;

    private int captureCount = 0;
    public static final String GALLERY_NAME = "gallery_name";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.still);
        setContentView(R.layout.activity_enroll);

        //getSupportActionBar().hide();

        sPref = getSharedPreferences("AttendancePref", MODE_PRIVATE);
        editor = sPref.edit();

        /*if(sPref.getBoolean(KEY_IS_LOGGED_IN, false) == true){            //If image is already enrolled, open recognize activity directly
            Intent intent = new Intent(this, RecognizeActivity.class);
            startActivityForResult(intent, 2000);
        }*/

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        galleryName = (TextView) findViewById(R.id.galleryid);
        galleryName.setText(user.getUid());
        start = (Button) findViewById(R.id.start);

        handler = new Handler();
        initialiseKairos();
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("KAIROS_DEMO", response);
            }
            @Override
            public void onFail(String response) {
                Log.d("KAIROS_DEMO", response);
            }
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryId = user.getUid();
                editor.putString(GALLERY_NAME, galleryId);
                editor.commit();
                requestCamera();
            }
        });
    }

    private void requestCamera(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 5);
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
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

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(captureCount >= 3){
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.commit();
            Toast.makeText(getApplicationContext(), "All Set and Done", Toast.LENGTH_SHORT);
            finish();
        }
        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null){
            captureCount++;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                myKairos.enroll(photo, subjectId, galleryId, selector, multipleFaces, minHeadScale, listener);
                Toast.makeText(getApplicationContext(), "Image Enrolled in " + galleryId, Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestCamera();
                    }
                }, 1000);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void initialiseKairos(){
        myKairos = new Kairos();
        String app_id = "ff849a7b";
        String api_key = "a0115dedb47393e59918822ec0989cd0";
        myKairos.setAuthentication(this, app_id, api_key);
    }

}
