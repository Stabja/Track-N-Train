package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Feeds;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tophawks.vm.visualmerchandising.SalesMgmt.MainActivity;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Account;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Feed;

import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dRef;
    private StorageReference sRef;

    @BindView(R.id.editText) TextView selectAccount;
    @BindView(R.id.editText2) EditText titleText;
    @BindView(R.id.editText3) EditText statusText;
    @BindView(R.id.post_status) Button create;

    private ArrayList<Account> accountsList = new ArrayList<>();
    private ArrayList<String> accountNames = new ArrayList<>();
    private int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.still);
        setContentView(R.layout.sales_activity_create_feed);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dRef = FirebaseDatabase.getInstance().getReference("Sales");
        sRef = FirebaseStorage.getInstance().getReference();

        fetchAccounts();

        selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccountDialog();
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
        DatabaseReference accountsRef = dRef.child("accounts");
        accountsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accountsList.clear();
                accountNames.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Account account = snapshot.getValue(Account.class);
                    accountsList.add(account);
                    accountNames.add(account.getFirstName() + " " + account.getLastName());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void selectAccountDialog(){
        final CharSequence[] accountArray = accountNames.toArray(new CharSequence[accountNames.size()]);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select an Account");
        dialog.setSingleChoiceItems(accountArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex = which;
                selectAccount.setText(accountArray[which]);
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
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

    private void postDataToServer(){
        if(!chkEmt(titleText) && !chkEmt(statusText)){

            final DatabaseReference feedsRef = dRef.child("feeds");
            final String key = feedsRef.push().getKey();                  //Creating a new Node
            final String userId = accountsList.get(selectedIndex).getId();
            final String title = titleText.getText().toString();
            final String status = statusText.getText().toString();
            final String timeStamp = DateTime.now().getSecondOfMinute() + "/" +
                    DateTime.now().getMinuteOfHour() + "/" +
                    DateTime.now().getHourOfDay() + "/" +
                    DateTime.now().getDayOfMonth() + "/" +
                    DateTime.now().getMonthOfYear() + "/" +
                    DateTime.now().getYear();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            sRef.child("Profile_Images/" + user.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri taskSnapshot) {
                    Uri downloadUri = taskSnapshot;
                    String photoUrl = downloadUri.toString();
                    Feed temp = new Feed(key, userId, title, status, photoUrl, timeStamp, 0, 0);
                    feedsRef.child(key).setValue(temp);          //Adding in Firebase
                    addDataInLocalList(temp);                    //Adding in Local List
                    Toast.makeText(CreateFeedActivity.this, "Feed Posted", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.still, R.anim.slide_out_down);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            Toast.makeText(this, "Please fill all values.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDataInLocalList(Feed temp){
        MainActivity.feedsList.add(temp);
        MainActivity.feedsAdapter.notifyItemInserted(MainActivity.feedsList.size());
        MainActivity.feedsAdapter.notifyDataSetChanged();
    }

}
