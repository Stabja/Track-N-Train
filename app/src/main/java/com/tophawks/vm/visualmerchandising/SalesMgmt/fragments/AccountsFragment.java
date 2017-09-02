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

import com.github.fabtransitionactivity.SheetLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tophawks.vm.visualmerchandising.SalesMgmt.MainActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Accounts.CreateAccountActivity;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.AccountsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Account;
import com.tophawks.vm.visualmerchandising.SalesMgmt.utils.RecyclerUtil;


public class AccountsFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton mFab;
    private SheetLayout mSheetLayout;

    LinearLayout progressLayout;
    public static RecyclerUtil recyclerUtil;
    public static AccountsFragment accountsFragment;

    private DatabaseReference dRef;
    private StorageReference sRef;

    public AccountsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.sales_fragment_accounts, container, false);

        progressLayout = (LinearLayout) vi.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.VISIBLE);
        dRef = FirebaseDatabase.getInstance().getReference("Sales");
        sRef = FirebaseStorage.getInstance().getReference();

        accountsFragment = this;

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) vi.findViewById(R.id.account_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(MainActivity.accountsAdapter);

        refreshLayout = (SwipeRefreshLayout) vi.findViewById(R.id.account_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerUtil.refreshData();
            }
        });

        recyclerUtil = new RecyclerUtil("accounts", getActivity(), recyclerView, refreshLayout);
        recyclerUtil.initSwipe();

        mFab = (FloatingActionButton) vi.findViewById(R.id.account_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();
            }
        });

        mSheetLayout = (SheetLayout) vi.findViewById(R.id.account_bottom_sheet);
        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(new SheetLayout.OnFabAnimationEndListener() {
            @Override
            public void onFabAnimationEnd() {
                Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        loadValues();

        return vi;
    }

    private void loadValues(){
        DatabaseReference accountsRef = dRef.child("accounts");
        accountsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.accountsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Account temp = snapshot.getValue(Account.class);
                    MainActivity.accountsList.add(temp);
                }
                MainActivity.accountsAdapter = new AccountsAdapter(getActivity());
                recyclerView.setAdapter(MainActivity.accountsAdapter);
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
