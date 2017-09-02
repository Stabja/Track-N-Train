package com.tophawks.vm.visualmerchandising.SalesMgmt.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.fabtransitionactivity.SheetLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tophawks.vm.visualmerchandising.SalesMgmt.MainActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Feeds.CreateFeedActivity;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.FeedsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Feed;


public class FeedsFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton mFab;
    private SheetLayout mSheetLayout;

    //private RecyclerUtil recyclerUtil;
    LinearLayout progressLayout;
    public static FeedsFragment feedsFragment;

    private DatabaseReference dRef;
    private StorageReference sRef;

    public FeedsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.sales_fragment_feeds, container, false);

        progressLayout = (LinearLayout) vi.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.VISIBLE);
        dRef = FirebaseDatabase.getInstance().getReference("Sales");
        sRef = FirebaseStorage.getInstance().getReference();

        feedsFragment = this;

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) vi.findViewById(R.id.feed_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(MainActivity.feedsAdapter);

        refreshLayout = (SwipeRefreshLayout) vi.findViewById(R.id.feed_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        mFab = (FloatingActionButton) vi.findViewById(R.id.feed_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();
            }
        });

        mSheetLayout = (SheetLayout) vi.findViewById(R.id.feed_bottom_sheet);
        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(new SheetLayout.OnFabAnimationEndListener() {
            @Override
            public void onFabAnimationEnd() {
                Intent intent = new Intent(getActivity(), CreateFeedActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        loadValues();

        return vi;
    }

    public void refreshData(){
        DatabaseReference feedsRef = dRef.child("feeds");
        feedsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.feedsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Feed feed = snapshot.getValue(Feed.class);
                    MainActivity.feedsList.add(feed);
                }
                MainActivity.feedsAdapter = new FeedsAdapter(getActivity());
                recyclerView.setAdapter(MainActivity.feedsAdapter);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to Refresh Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadValues(){
        DatabaseReference feedsRef = dRef.child("feeds");
        feedsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.feedsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Feed temp = snapshot.getValue(Feed.class);
                    MainActivity.feedsList.add(temp);
                }
                MainActivity.feedsAdapter = new FeedsAdapter(getActivity());
                recyclerView.setAdapter(MainActivity.feedsAdapter);
                progressLayout.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            mSheetLayout.contractFab();
        }
    }

}
