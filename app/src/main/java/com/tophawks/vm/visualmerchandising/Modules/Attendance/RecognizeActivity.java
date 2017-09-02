package com.tophawks.vm.visualmerchandising.Modules.Attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tophawks.vm.visualmerchandising.R;

import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RecognizeActivity extends AppCompatActivity{

    private ImageView imageView;
    private TextView statusText;
    private Button loadCamera;
    private Button refreshGallery;

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final String IMAGES_ENROLLED = "images_enrolled";

    private Kairos myKairos;
    private KairosListener listener;
    private SharedPreferences preference;
    private Editor editor;

    private String subjectId = "myimage";
    private String galleryId;
    private String selector = "FULL";
    private String multipleFaces = "false";
    private String minHeadScale = "0.25";
    private String threshold = "0.75";
    private String maxNumResults = "25";

    private ProgressDialog progressDialog;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.still);
        setContentView(R.layout.activity_recognize);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Facial Recognition");

        imageView = (ImageView) findViewById(R.id.imageView1);
        statusText = (TextView) findViewById(R.id.status);
        loadCamera = (Button) findViewById(R.id.take_picture);
        refreshGallery = (Button) findViewById(R.id.reset_gallery);

        preference = getSharedPreferences("AttendancePref", MODE_PRIVATE);
        editor = preference.edit();
        galleryId = preference.getString(EnrollActivity.GALLERY_NAME, EnrollActivity.GALLERY_NAME);
        handler = new Handler();

        initialiseKairos();
        progressDialog = new ProgressDialog(this);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray array = root.getJSONArray("images");
                    JSONObject jo = array.getJSONObject(0);
                    JSONObject transaction = jo.getJSONObject("transaction");

                    String status = transaction.getString("status");
                    if(status.equals("success")){
                        statusText.setTextColor(Color.GREEN);
                        statusText.setText("Its a MATCH");
                        editor.putBoolean("isFaceDone", true);
                        editor.commit();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /*Intent intent = new Intent();
                                intent.putExtra("FaceVerified", true);
                                setResult(RESULT_OK, intent);*/
                                Toast.makeText(getApplicationContext(), "Face Result Set", Toast.LENGTH_SHORT).show();
                                finish();
                                overridePendingTransition(R.anim.still, R.anim.slide_out_down);
                            }
                        }, 1000);
                    }
                    else if(status.equals("failure")){
                        statusText.setTextColor(Color.RED);
                        statusText.setText(transaction.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray array = root.getJSONArray("Errors");
                    JSONObject jo = array.getJSONObject(0);
                    String message = jo.getString("Message");
                    statusText.setTextColor(Color.RED);
                    statusText.setText(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("KAIROS_DEMO", response);
            }
            @Override
            public void onFail(String response) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Cannot fetch JSON", Toast.LENGTH_SHORT).show();
                Log.d("KAIROS_DEMO", response);
            }
        };

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        loadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        refreshGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myKairos.deleteSubject(subjectId, galleryId, listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void recognizeImage(){
        if(imageView.getDrawable() == null){
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }
        else{
            Bitmap sample = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            try {
                progressDialog.setMessage("Recognizing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                myKairos.recognize(sample, galleryId, selector, threshold, minHeadScale, maxNumResults, listener);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
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
        Intent intent = new Intent();
        intent.putExtra("FaceVerified", false);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "OnConfigurationChanged Called", Toast.LENGTH_SHORT).show();
    }

    private void initialiseKairos(){
        myKairos = new Kairos();
        String app_id = "ff849a7b";
        String api_key = "a0115dedb47393e59918822ec0989cd0";
        myKairos.setAuthentication(this, app_id, api_key);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            recognizeImage();
        }
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            recognizeImage();
        }
    }

}


