package com.tophawks.vm.visualmerchandising.SalesMgmt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.tophawks.vm.visualmerchandising.Activities.LoginActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.AccountsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.ContactsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.DealsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.EventsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.FeedsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.LeadsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.TasksAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.fragments.AccountsFragment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.fragments.ContactsFragment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.fragments.DealsFragment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.fragments.EventsFragment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.fragments.FeedsFragment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.fragments.HomeFragment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.fragments.LeadsFragment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.fragments.TasksFragment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Account;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Contact;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Deal;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Event;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Feed;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Lead;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Task;
import com.tophawks.vm.visualmerchandising.R;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    private CircleImageView profileImageView;
    private TextView userName;
    private TextView userEmail;

    private FragmentManager manager;

    private AccountsFragment accountsFragment;
    private ContactsFragment contactsFragment;
    private LeadsFragment leadsFragment;
    private DealsFragment dealsFragment;
    private EventsFragment eventsFragment;
    private FeedsFragment feedsFragment;
    private TasksFragment tasksFragment;
    private HomeFragment homeFragment;

    public static ArrayList<Account> accountsList = new ArrayList<Account>();
    public static ArrayList<Contact> contactsList = new ArrayList<Contact>();
    public static ArrayList<Lead> leadsList = new ArrayList<Lead>();
    public static ArrayList<Deal> dealsList = new ArrayList<Deal>();
    public static ArrayList<Event> eventsList = new ArrayList<Event>();
    public static ArrayList<Feed> feedsList = new ArrayList<Feed>();
    public static ArrayList<Task> tasksList = new ArrayList<Task>();

    public static AccountsAdapter accountsAdapter;
    public static ContactsAdapter contactsAdapter;
    public static LeadsAdapter leadsAdapter;
    public static DealsAdapter dealsAdapter;
    public static EventsAdapter eventsAdapter;
    public static FeedsAdapter feedsAdapter;
    public static TasksAdapter tasksAdapter;

    public static ProgressDialog progressDialog;

    public static FirebaseAuth auth;
    public static FirebaseUser user;
    public static DatabaseReference salesRef;
    public static DatabaseReference accountsRef;
    public static DatabaseReference contactsRef;
    public static DatabaseReference leadsRef;
    public static DatabaseReference dealsRef;
    public static DatabaseReference tasksRef;
    public static DatabaseReference eventsRef;
    public static DatabaseReference feedsRef;
    public static DatabaseReference commentsRef;
    public static StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.sales_activity_sales_main);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        profileImageView = (CircleImageView) header.findViewById(R.id.profile_image);
        userName = (TextView) header.findViewById(R.id.user_name);
        userEmail = (TextView) header.findViewById(R.id.user_email);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String photoUrl = dataSnapshot.child("photoUrl").getValue(String.class);
                Glide.with(MainActivity.this).load(photoUrl).into(profileImageView);
                userName.setText(dataSnapshot.child("name").getValue(String.class));
                userEmail.setText(user.getEmail());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        manager = getSupportFragmentManager();
        accountsFragment = new AccountsFragment();
        contactsFragment = new ContactsFragment();
        dealsFragment = new DealsFragment();
        eventsFragment = new EventsFragment();
        feedsFragment = new FeedsFragment();
        homeFragment = new HomeFragment();
        leadsFragment = new LeadsFragment();
        tasksFragment = new TasksFragment();
        manager.beginTransaction().add(R.id.parent, homeFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /******************************************
        METHOD TO GENERATE TEST DATA

        1. Comment out updateData()
        2. Remove the comments from createTestAccountsData(), createTestContactsData(), createTestLeadsData()
        3. Run the apk once and then revert this back i.e
           3.1. Remove comments from updateData()
           3.2. Comment out createTestAccountsData(), createTestContactsData(), createTestLeadsData()

        *******************************************/
        //createTestAccountsData();
        //createTestContactsData();
        //createTestLeadsData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.nav_home:
                FragmentTransaction transaction1 = manager.beginTransaction();
                transaction1.setCustomAnimations(R.anim.enter, R.anim.exit);
                transaction1.replace(R.id.parent, homeFragment).commit();
                break;
            case R.id.nav_feeds:
                FragmentTransaction transaction2 = manager.beginTransaction();
                transaction2.setCustomAnimations(R.anim.enter, R.anim.exit);
                transaction2.replace(R.id.parent, feedsFragment).commit();
                break;
            case R.id.nav_leads:
                FragmentTransaction transaction3 = manager.beginTransaction();
                transaction3.setCustomAnimations(R.anim.enter, R.anim.exit);
                transaction3.replace(R.id.parent, leadsFragment).commit();
                break;
            case R.id.nav_accounts:
                FragmentTransaction transaction4 = manager.beginTransaction();
                transaction4.setCustomAnimations(R.anim.enter, R.anim.exit);
                transaction4.replace(R.id.parent, accountsFragment).commit();
                break;
            case R.id.nav_contacts:
                FragmentTransaction transaction5 = manager.beginTransaction();
                transaction5.setCustomAnimations(R.anim.enter, R.anim.exit);
                transaction5.replace(R.id.parent, contactsFragment).commit();
                break;
            case R.id.nav_deals:
                FragmentTransaction transaction6 = manager.beginTransaction();
                transaction6.setCustomAnimations(R.anim.enter, R.anim.exit);
                transaction6.replace(R.id.parent, dealsFragment).commit();
                break;
            case R.id.nav_tasks:
                FragmentTransaction transaction7 = manager.beginTransaction();
                transaction7.setCustomAnimations(R.anim.enter, R.anim.exit);
                transaction7.replace(R.id.parent, tasksFragment).commit();
                break;
            case R.id.nav_events:
                FragmentTransaction transaction8 = manager.beginTransaction();
                transaction8.setCustomAnimations(R.anim.enter, R.anim.exit);
                transaction8.replace(R.id.parent, eventsFragment).commit();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user.getDisplayName();
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };
    }

    public void createTestAccountsData(){
        salesRef = FirebaseDatabase.getInstance().getReference("Sales");
        accountsRef = salesRef.child("accounts");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String[] array = name.split(" ");
                    String firstName = array[0];
                    String lastName = array[1];
                    String email = snapshot.child("email").getValue(String.class);
                    Account account = new Account(key, firstName, lastName, email, "8120018682", "AnmolAc", "accounts.com", "9568970898", "Distributor", "Private", "50", "55000");
                    accountsRef.child(key).setValue(account);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(MainActivity.this, "Test Accounts Created", Toast.LENGTH_SHORT).show();
    }

    private void createTestContactsData(){
        salesRef = FirebaseDatabase.getInstance().getReference("Sales");
        contactsRef = salesRef.child("contacts");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String[] array = name.split(" ");
                    String firstName = array[0];
                    String lastName = array[1];
                    String email = snapshot.child("email").getValue(String.class);
                    String photoUrl = snapshot.child("photoUrl").getValue(String.class);
                    Contact contact = new Contact(key, photoUrl, firstName, lastName, "AnmolAc", "Employee Referral", "Marketing", "24/2/1995", email, "8120018682", "661227", "661334", "anmolS", "linkedinurl", "twitterurl", "facebookurl");
                    contactsRef.child(key).setValue(contact);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(MainActivity.this, "Test Contacts Created", Toast.LENGTH_SHORT).show();
    }

    private void createTestLeadsData(){
        salesRef = FirebaseDatabase.getInstance().getReference("Sales");
        leadsRef = salesRef.child("leads");

        String key = leadsRef.push().getKey();
        Lead lead = new Lead(key, "url", "Anmol", "Saxena", "Tophawks", "anmolsaxena@gmail.com", "8120018682", "anmolsaxena.com", "Cold Call", "Contacted", "ManagementISV", "500", "55,000.00", "5");
        leadsRef.child(key).setValue(lead);

        key = leadsRef.push().getKey();
        lead = new Lead(key, "url", "Sanidhya", "Pal", "Tophawks", "anmolsaxena@gmail.com", "8120018682", "anmolsaxena.com", "Cold Call", "Contacted", "ManagementISV", "500", "55,000.00", "5");
        leadsRef.child(key).setValue(lead);

        key = leadsRef.push().getKey();
        lead = new Lead(key, "url", "Siddhart", "Somani", "Tophawks", "anmolsaxena@gmail.com", "8120018682", "anmolsaxena.com", "Cold Call", "Contacted", "ManagementISV", "500", "55,000.00", "5");
        leadsRef.child(key).setValue(lead);

        key = leadsRef.push().getKey();
        lead = new Lead(key, "url", "Kenneth", "Tenny", "Tophawks", "anmolsaxena@gmail.com", "8120018682", "anmolsaxena.com", "Cold Call", "Contacted", "ManagementISV", "500", "55,000.00", "5");
        leadsRef.child(key).setValue(lead);

        key = leadsRef.push().getKey();
        lead = new Lead(key, "url", "Tofiq", "Quadri", "Tophawks", "anmolsaxena@gmail.com", "8120018682", "anmolsaxena.com", "Cold Call", "Contacted", "ManagementISV", "500", "55,000.00", "5");
        leadsRef.child(key).setValue(lead);

        key = leadsRef.push().getKey();
        lead = new Lead(key, "url", "Yashaswi", "Priyadarshi", "Tophawks", "anmolsaxena@gmail.com", "8120018682", "anmolsaxena.com", "Cold Call", "Contacted", "ManagementISV", "500", "55,000.00", "5");
        leadsRef.child(key).setValue(lead);

        key = leadsRef.push().getKey();
        lead = new Lead(key, "url", "Stabja", "Hazra", "Tophawks", "anmolsaxena@gmail.com", "8120018682", "anmolsaxena.com", "Cold Call", "Contacted", "ManagementISV", "500", "55,000.00", "5");
        leadsRef.child(key).setValue(lead);

        Toast.makeText(MainActivity.this, "Test Leads Created", Toast.LENGTH_SHORT).show();
    }

    private void createTestDealsData(){
        salesRef = FirebaseDatabase.getInstance().getReference("Sales");
        dealsRef = salesRef.child("deals");
        //Create the test data here.
        Toast.makeText(MainActivity.this, "Test Deals Created", Toast.LENGTH_SHORT).show();
    }

    private void createTestTasksData(){
        salesRef = FirebaseDatabase.getInstance().getReference("Sales");
        tasksRef = salesRef.child("tasks");
        //Create the test data here.
        Toast.makeText(MainActivity.this, "Test Tasks Created", Toast.LENGTH_SHORT).show();
    }

    private void createTestEventData(){
        salesRef = FirebaseDatabase.getInstance().getReference("Sales");
        eventsRef = salesRef.child("events");
        //Create the test data here.
        Toast.makeText(MainActivity.this, "Test Events Created", Toast.LENGTH_SHORT).show();
    }

    private void createTestFeedsData(){
        salesRef = FirebaseDatabase.getInstance().getReference();
        feedsRef = salesRef.child("feeds");
        //Create the test data here.
        Toast.makeText(MainActivity.this, "Test Feeds Created", Toast.LENGTH_SHORT).show();
    }

}
