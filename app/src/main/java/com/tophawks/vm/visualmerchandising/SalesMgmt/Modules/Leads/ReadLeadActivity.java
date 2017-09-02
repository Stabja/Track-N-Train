package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Leads;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Lead;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadLeadActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference dRef;
    private StorageReference sRef;

    @BindView(R.id.dis_image) ImageView photo;
    @BindView(R.id.editText) TextView tv1;
    @BindView(R.id.editText2) TextView tv2;
    @BindView(R.id.editText3) TextView tv3;
    @BindView(R.id.editText4) TextView tv4;
    @BindView(R.id.editText5) TextView tv5;
    @BindView(R.id.editText6) TextView tv6;
    @BindView(R.id.editText7) TextView tv7;
    @BindView(R.id.editText8) TextView tv8;
    @BindView(R.id.editText9) TextView tv9;
    @BindView(R.id.editText10) TextView tv10;
    @BindView(R.id.editText11) TextView tv11;
    @BindView(R.id.editText12) TextView tv12;

    private String leadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.sales_activity_read_lead);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dRef = FirebaseDatabase.getInstance().getReference("Sales");

        Intent intent = getIntent();
        leadId = intent.getStringExtra("leadId");
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadValues();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_lead_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_edit){
            Intent intent = new Intent(this, UpdateLeadActivity.class);
            intent.putExtra("leadId", leadId);
            startActivity(intent);
        }
        /*if(id == R.id.menu_clone){
            Intent intent = new Intent(this, CreateLeadActivity.class);
            intent.putExtra("leadId", leadId);
            startActivity(intent);
        }*/
        if(id == R.id.menu_refresh){
            loadValues();
        }
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    private void showdeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Code to delete the current item and finish the Activity
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void loadValues(){
        DatabaseReference leadsRef = dRef.child("leads");
        leadsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lead temp = dataSnapshot.child(leadId).getValue(Lead.class);

                String photoUrl = temp.getPhotoUrl();
                Glide.with(ReadLeadActivity.this).load(photoUrl).into(photo);
                tv1.setText(temp.getFirstName());
                tv2.setText(temp.getLastName());
                tv3.setText(temp.getCompany());
                tv4.setText(temp.getEmail());
                tv5.setText(temp.getPhone());
                tv6.setText(temp.getWebsite());
                tv7.setText(temp.getLeadSource());
                tv8.setText(temp.getLeadStatus());
                tv9.setText(temp.getIndustry());
                tv10.setText(temp.getEmployees());
                tv11.setText(temp.getRevenue());
                tv12.setText(temp.getRating());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cannot Load Values", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
