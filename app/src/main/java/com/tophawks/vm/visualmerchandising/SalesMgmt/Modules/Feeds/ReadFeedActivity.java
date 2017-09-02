package com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Feeds;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.tophawks.vm.visualmerchandising.SalesMgmt.DynamicHeight;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.CommentsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Account;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Comment;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Feed;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReadFeedActivity extends AppCompatActivity implements DynamicHeight{

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dRef;
    private StorageReference sRef;

    @BindView(R.id.parent_scrollview) ScrollView scrollView;
    @BindView(R.id.authorimage) CircleImageView authorIcon;
    @BindView(R.id.author) TextView authorName;
    @BindView(R.id.title) TextView postTitle;
    @BindView(R.id.body) TextView postBody;
    @BindView(R.id.timestamp) TextView timeStamp;
    @BindView(R.id.comments_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.comment_text) EditText commentText;
    @BindView(R.id.button_publish_comment) Button publishComment;
    @BindView(R.id.loading_layout) LinearLayout progressLayout;

    private String feedId;
    private LinearLayoutManager layoutManager;
    private CommentsAdapter adapter;
    private ArrayList<Comment> commentsList = new ArrayList<Comment>();
    private HashMap<Integer, Integer> itemHeight = new HashMap<>();

    private String fetchedFeedId;
    private String author;
    private int commentNo;
    private int likesNo;
    private Boolean liked;

    private String commentAuthor;
    private String commentPhoto;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.sales_activity_read_feed);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dRef = FirebaseDatabase.getInstance().getReference("Sales");

        Intent intent = getIntent();
        feedId = intent.getStringExtra("feedId");

        scrollView.setNestedScrollingEnabled(true);
        scrollView.setSmoothScrollingEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        progressLayout.setVisibility(View.VISIBLE);

        publishComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishNewComment();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchFeed();
        fetchComments();
        getUserDetailsForComment();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_comments_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.refresh_comments){
            fetchComments();
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

    private void fetchFeed(){
        DatabaseReference feedsRef = dRef.child("feeds");
        feedsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Feed temp = dataSnapshot.child(feedId).getValue(Feed.class);
                fetchedFeedId = temp.getId();
                obtainUsernameFromId(temp.getUserId());
                postTitle.setText(temp.getTitle());
                postBody.setText(temp.getStatus());
                calculateTimeAGO(timeStamp, temp.getTimeStamp());
                commentNo = temp.getComments();
                likesNo = temp.getLikes();
                Glide.with(getApplicationContext()).load(temp.getPhotoUrl()).into(authorIcon);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cannot Load Feed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDetailsForComment(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentAuthor = dataSnapshot.child("name").getValue(String.class);
                commentPhoto = dataSnapshot.child("photoUrl").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void obtainUsernameFromId(final String id){
        DatabaseReference accountRef = dRef.child("accounts");
        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account temp = dataSnapshot.child(id).getValue(Account.class);
                String name = temp.getFirstName() + " " + temp.getLastName();
                authorName.setText(name);
                author = name;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void fetchComments(){
        DatabaseReference commentsRef = dRef.child("feed-comments").child(feedId);
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comment temp = snapshot.getValue(Comment.class);
                    commentsList.add(temp);
                }
                adapter = new CommentsAdapter(getApplicationContext(), commentsList, ReadFeedActivity.this);
                recyclerView.setAdapter(adapter);
                progressLayout.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load comments, Please Refresh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void publishNewComment(){
        if(!TextUtils.isEmpty(commentText.getText().toString())){
            DatabaseReference commentsRef = dRef.child("feed-comments").child(feedId);
            String key = commentsRef.push().getKey();
            String text = commentText.getText().toString();
            String timeStamp = DateTime.now().getSecondOfMinute() + "/" +
                    DateTime.now().getMinuteOfHour() + "/" +
                    DateTime.now().getHourOfDay() + "/" +
                    DateTime.now().getDayOfMonth() + "/" +
                    DateTime.now().getMonthOfYear() + "/" +
                    DateTime.now().getYear();

            Comment comment = new Comment(key, feedId, commentPhoto, commentAuthor, text, timeStamp);
            commentsRef.child(key).setValue(comment);      //Adding in Firebase
            commentsList.add(comment);                     //Adding in Local List
            adapter.notifyDataSetChanged();
            commentText.setText("");

            DatabaseReference feedsRef = dRef.child("feeds").child(feedId).child("comments");
            feedsRef.setValue(commentNo+1);                //Increase the no of comments in feeds
            commentNo++;
        }
    }

    private void calculateTimeAGO(TextView timeText, String timeStamp){
        String[] chars = timeStamp.split("/");
        int second = Integer.parseInt(chars[0]);
        int minute = Integer.parseInt(chars[1]);
        int hour = Integer.parseInt(chars[2]);
        int day = Integer.parseInt(chars[3]);
        int month = Integer.parseInt(chars[4]);
        int year = Integer.parseInt(chars[5]);
        int secondNow = DateTime.now().getSecondOfMinute();
        int minuteNow = DateTime.now().getMinuteOfHour();
        int hourNow = DateTime.now().getHourOfDay();
        int dayNow = DateTime.now().getDayOfMonth();
        int monthNow = DateTime.now().getMonthOfYear();
        int yearNow = DateTime.now().getYear();
        int displayTime;
        if(yearNow - year != 0){
            displayTime = yearNow - year;
            timeText.setText(displayTime + " years ago");
        }else if(monthNow - month != 0){
            displayTime = monthNow - month;
            timeText.setText(displayTime + " months ago");
        }else if(dayNow - day != 0){
            displayTime = dayNow - day;
            timeText.setText(displayTime + " days ago");
        }else if(hourNow - hour != 0){
            displayTime = hourNow - hour;
            timeText.setText(displayTime + " hours ago");
        }else if(minuteNow - minute != 0){
            displayTime = minuteNow - minute;
            timeText.setText(displayTime + " minutes ago");
        }else{
            displayTime = secondNow - second;
            timeText.setText(displayTime + " seconds ago");
        }
    }

    @Override
    public void HeightChange(int position, int height) {
        itemHeight.put(position, height);
        int sumHeight = SumHashItem(itemHeight);

        float density = this.getResources().getDisplayMetrics().density;
        float viewHeight = sumHeight * density;
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = sumHeight;
        recyclerView.setLayoutParams(params);
    }

    int SumHashItem (HashMap<Integer, Integer> hashMap) {
        int sum = 0;
        for(Map.Entry<Integer, Integer> myItem: hashMap.entrySet())  {
            sum += myItem.getValue();
        }
        return sum;
    }

    public class CustomLayoutManager extends LinearLayoutManager{
        private boolean isScrollEnabled = true;

        public CustomLayoutManager(Context context) {
            super(context);
        }
        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }
        @Override
        public boolean canScrollVertically() {
            return isScrollEnabled && super.canScrollVertically();
        }
    }

}
