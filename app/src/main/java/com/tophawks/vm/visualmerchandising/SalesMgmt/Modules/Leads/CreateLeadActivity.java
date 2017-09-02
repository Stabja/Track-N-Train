package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Leads;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Account;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Lead;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateLeadActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dRef;
    private StorageReference sRef;

    @BindView(R.id.dis_image) ImageView photo;
    @BindView(R.id.editText) EditText et1;
    @BindView(R.id.editText2) EditText et2;
    @BindView(R.id.editText13) TextView tv13;
    @BindView(R.id.editText3) EditText et3;
    @BindView(R.id.editText4) EditText et4;
    @BindView(R.id.editText5) EditText et5;
    @BindView(R.id.editText6) EditText et6;
    @BindView(R.id.editText7) TextView tv7;
    @BindView(R.id.editText8) TextView tv8;
    @BindView(R.id.editText9) TextView tv9;
    @BindView(R.id.editText10) EditText et10;
    @BindView(R.id.editText11) EditText et11;
    @BindView(R.id.editText12) TextView tv12;
    @BindView(R.id.create_lead) Button create;

    private ArrayList<String> accountsList = new ArrayList<>();

    private Uri imageFile;
    private String imagefileDownloadUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.still);
        setContentView(R.layout.sales_activity_create_lead);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dRef = FirebaseDatabase.getInstance().getReference("Sales");
        sRef = FirebaseStorage.getInstance().getReference();

        fetchAccounts();

        tv13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountPickerDialog();
            }
        });

        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeadSourceDialog();
            }
        });

        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeadStatusDialog();
            }
        });

        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIndustryDialog();
            }
        });

        et11.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Locale locale = new Locale("en", "US");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                    et11.setText(fmt.format(Long.parseLong(et11.getText().toString())));
                }
            }
        });

        tv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToServer();
            }
        });

    }

    private void fetchAccounts(){
        final DatabaseReference accountsRef = dRef.child("accounts");
        accountsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accountsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Account temp = snapshot.getValue(Account.class);
                    String name = temp.getFirstName() + " " + temp.getLastName();
                    accountsList.add(name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAccountPickerDialog(){
        final CharSequence[] accountArray = accountsList.toArray(new CharSequence[accountsList.size()]);
        AlertDialog.Builder accountDialog = new AlertDialog.Builder(this);
        accountDialog.setTitle("Accounts");
        accountDialog.setSingleChoiceItems(accountArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv13.setText(accountArray[which].toString());
                dialog.dismiss();
            }
        });
        accountDialog.create();
        accountDialog.show();
    }

    private void showImageChooser(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(pickIntent, "select Image");
        startActivityForResult(chooserIntent, 900);
    }

    private void showLeadSourceDialog(){
        final CharSequence[] leadArray = getResources().getStringArray(R.array.lead_source);
        final AlertDialog.Builder leadDialog = new AlertDialog.Builder(this);
        leadDialog.setTitle("Lead Sources");
        leadDialog.setSingleChoiceItems(leadArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv7.setText(leadArray[which].toString());
                dialog.dismiss();
            }
        });
        leadDialog.create();
        leadDialog.show();
    }

    private void showLeadStatusDialog(){
        final CharSequence[] leadArray = getResources().getStringArray(R.array.lead_status);
        final AlertDialog.Builder leadDialog = new AlertDialog.Builder(this);
        leadDialog.setTitle("Lead Status");
        leadDialog.setSingleChoiceItems(leadArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv8.setText(leadArray[which].toString());
                dialog.dismiss();
            }
        });
        leadDialog.create();
        leadDialog.show();
    }

    private void showIndustryDialog(){
        final CharSequence[] leadArray = getResources().getStringArray(R.array.industry);
        final AlertDialog.Builder leadDialog = new AlertDialog.Builder(this);
        leadDialog.setTitle("Industry");
        leadDialog.setSingleChoiceItems(leadArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv9.setText(leadArray[which].toString());
                dialog.dismiss();
            }
        });
        leadDialog.create();
        leadDialog.show();
    }

    private void showRatingDialog(){
        final CharSequence[] leadArray = getResources().getStringArray(R.array.rating);
        final AlertDialog.Builder leadDialog = new AlertDialog.Builder(this);
        leadDialog.setTitle("Rating");
        leadDialog.setSingleChoiceItems(leadArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv12.setText(leadArray[which].toString());
                dialog.dismiss();
            }
        });
        leadDialog.create();
        leadDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 900 && resultCode == RESULT_OK && data != null){
            imageFile = data.getData();
            Glide.with(this).load(imageFile).into(photo);
            StorageReference riversRef = sRef.child("Contact_Images/" + imageFile.getLastPathSegment());
            riversRef.putFile(imageFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                    imagefileDownloadUrl = downloadUri.toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_done){
            postDataToServer();
        }
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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

    private boolean chkEmt(EditText editText){
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private boolean chkEmtTV(TextView textView){
        return TextUtils.isEmpty(textView.getText().toString());
    }

    private void postDataToServer(){
        if(!chkEmt(et1) && !chkEmt(et2) && !chkEmtTV(tv13) && !chkEmt(et3) && !chkEmt(et4) && !chkEmt(et5) && !chkEmt(et6) && !chkEmtTV(tv7)
                        && !chkEmtTV(tv8) && !chkEmtTV(tv9) && !chkEmt(et10) && !chkEmt(et11) && !chkEmtTV(tv12)){

            DatabaseReference leadsRef = dRef.child("leads");
            String key = leadsRef.push().getKey();
            String a = imagefileDownloadUrl;
            String b = et1.getText().toString();
            String c = et2.getText().toString();
            String d = tv13.getText().toString();
            String e = et3.getText().toString();
            String f = et4.getText().toString();
            String g = et5.getText().toString();
            String h = et6.getText().toString();
            String i = tv7.getText().toString();
            String j = tv8.getText().toString();
            String k = tv9.getText().toString();
            String l = et10.getText().toString();
            String m = et11.getText().toString();
            String n = tv12.getText().toString();

            Lead temp = new Lead(key, a, b, c, d, e, f, g, h, i, j, k, l, m, n);
            leadsRef.child(key).setValue(temp);
            Toast.makeText(this, "Lead Created", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.still, R.anim.slide_out_down);
        } else {
            Toast.makeText(this, "Please fill all values.", Toast.LENGTH_SHORT).show();
        }
    }

}
