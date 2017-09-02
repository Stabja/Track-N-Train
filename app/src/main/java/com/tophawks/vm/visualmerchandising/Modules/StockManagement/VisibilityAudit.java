package com.tophawks.vm.visualmerchandising.Modules.StockManagement;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.ReadWriteExcelFile;

import org.joda.time.LocalDate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VisibilityAudit extends AppCompatActivity {

    ImageView productIV;
    TextView storeNameTV;
    TextView storeIDTV;
    EditText auditTypeET;
    EditText ownerNameET;
    EditText productNameET;
    EditText quantityET;
    EditText productTypeET;
    EditText remarkET;
    String auditType,ownerName,productName,quantity,productType,remark;
    private static final int MY_PERMISSIONS_REQUEST = 123;
    private static final int PICK_IMAGE_REQUEST_CODE = 213;
    ProgressDialog mProgress;
    int s = 0, c = 0;
    Uri imageHold = null;
    private Uri outputFileUri;
    FirebaseUser user;
    LinearLayout generateReportLL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visibility_audit);

        productIV=(ImageView)findViewById(R.id.visibility_product_image_iv);
        storeNameTV=(TextView)findViewById(R.id.visibility_store_name_tv);
        storeIDTV=(TextView)findViewById(R.id.visibility_store_id_tv);
        auditTypeET=(EditText)findViewById(R.id.visibility_audit_type_et);
        ownerNameET=(EditText)findViewById(R.id.visibility_store_owner_et);
        productNameET=(EditText)findViewById(R.id.visibility_product_name_et);
        quantityET=(EditText)findViewById(R.id.visibility_product_quantity_et);
        productTypeET=(EditText)findViewById(R.id.visibility_product_type_et);
        remarkET=(EditText)findViewById(R.id.visibility_audit_remark_et);
        generateReportLL=(LinearLayout)findViewById(R.id.generate_audit_report_ll);
        storeNameTV.setText("Store Name: "+getIntent().getStringExtra("storeName"));
        storeIDTV.setText("Store ID: "+getIntent().getStringExtra("storeId"));
        generateReportLL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(VisibilityAudit.this);
                progressDialog.setMessage("Uploading Image and generating report!!");
                progressDialog.show();
                auditType=auditTypeET.getText().toString();
                ownerName=ownerNameET.getText().toString();
                productName=productNameET.getText().toString();
                quantity=quantityET.getText().toString();
                productType=productTypeET.getText().toString();
                remark=remarkET.getText().toString();
                if(!TextUtils.isEmpty(auditType)&&
                        !TextUtils.isEmpty(ownerName)&&
                        !TextUtils.isEmpty(productName)&&
                        !TextUtils.isEmpty(quantity)&&
                        !TextUtils.isEmpty(productType)&&
                        !TextUtils.isEmpty(remark)&&
                       imageHold!=null){
                    StorageReference mChildStorage = FirebaseStorage.getInstance().getReference().child("Audit_Images").child(getIntent().getStringExtra("storeId")).child(imageHold.getLastPathSegment());
                    mChildStorage.putFile(imageHold).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //GET THE DOWNLOAD URL FROM THE TASK SUCCESS
                            //noinspection VisibleForTests
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            ArrayList<ArrayList<String >> reportDataLOL=new ArrayList<>();
                            reportDataLOL.add(new ArrayList<String>());
                            reportDataLOL.get(0).add("Store ID");
                            reportDataLOL.get(0).add("Store Name");
                            reportDataLOL.get(0).add("Store Owner");
                            reportDataLOL.get(0).add("Audit Entity Name");
                            reportDataLOL.get(0).add("Entity Quantity");
                            reportDataLOL.get(0).add("Entity Type");
                            reportDataLOL.get(0).add("Entity Image");
                            reportDataLOL.get(0).add("Remark");
                            reportDataLOL.add(new ArrayList<String>());
                            reportDataLOL.get(1).add(getIntent().getStringExtra("storeId"));
                            reportDataLOL.get(1).add(getIntent().getStringExtra("storeName"));
                            reportDataLOL.get(1).add(ownerName);
                            reportDataLOL.get(1).add(productName);
                            reportDataLOL.get(1).add(quantity);
                            reportDataLOL.get(1).add(productType);
                            reportDataLOL.get(1).add(downloadUri.toString());
                            reportDataLOL.get(1).add(remark);
                            String xlsFilename="Audit report "+ LocalDate.now().toString()+".xls";
                            ReadWriteExcelFile readWriteExcelFile=new ReadWriteExcelFile(VisibilityAudit.this);
                            Uri fileUri=readWriteExcelFile.saveExcelFile(xlsFilename,reportDataLOL,"Audit report");
                            Intent openStockReport=new Intent(Intent.ACTION_VIEW,fileUri);
                            progressDialog.dismiss();
                            startActivity(openStockReport);
                        }
                    });


                }
                else
                    Toast.makeText(VisibilityAudit.this, "Fill all fields!!", Toast.LENGTH_SHORT).show();
            }
        });
        productIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequest();
            }
        });


    }
    private void permissionRequest() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
            imageChooser();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    //IMAGE PICKER WHEN CHOOSE IMAGE BUTTON IS CLICKED
    private void imageChooser() {

        File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Track'n'Train" + File.separator + "Audit Product Picture" + File.separator);
        root.mkdirs();
        final String fname = "storePic" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        //Camera
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final String localPackageName = res.activityInfo.loadLabel(packageManager).toString();
            if (localPackageName.toLowerCase().equals("camera")) {
                final Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                cameraIntents.add(intent);
            }
        }
        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                CropImage.activity(selectedImageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    imageHold = result.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageHold);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                        byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
                        File temp = File.createTempFile("store", "pic.jpg");
                        FileOutputStream fileOutputStream = new FileOutputStream(temp);
                        fileOutputStream.write(bytesBitmap);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        imageHold = Uri.fromFile(temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    productIV.setImageURI(imageHold);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST && grantResults.length > 0) {
            Log.i("grantresults", grantResults.toString());
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot use external storage!!", Toast.LENGTH_LONG).show();
                    return;
                }
                s = 1;
            }
            if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot use Camera!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                c = 1;
            }
            if (s == 1 && c == 1)
                imageChooser();
        }
        }


}
