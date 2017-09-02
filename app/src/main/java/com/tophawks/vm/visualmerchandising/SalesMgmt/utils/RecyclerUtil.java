package com.tophawks.vm.visualmerchandising.SalesMgmt.utils;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tophawks.vm.visualmerchandising.SalesMgmt.MainActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Accounts.UpdateAccountActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Contacts.UpdateContactActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Deals.UpdateDealActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Events.UpdateEventActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Feeds.UpdateFeedActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Leads.UpdateLeadActivity;
import com.tophawks.vm.visualmerchandising.SalesMgmt.Modules.Tasks.UpdateTaskActivity;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.AccountsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.ContactsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.DealsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.EventsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.FeedsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.LeadsAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.adapters.TasksAdapter;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Account;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Contact;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Deal;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Event;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Feed;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Lead;
import com.tophawks.vm.visualmerchandising.SalesMgmt.models.Task;

public class RecyclerUtil{

    private Activity activity;
    private Paint paint;
    private String fromFragment;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private FirebaseAuth auth;
    private DatabaseReference dRef;
    private StorageReference sRef;

    public RecyclerUtil(String fromFragment, Activity activity, RecyclerView recyclerView, SwipeRefreshLayout refreshLayout){
        this.fromFragment = fromFragment;
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.refreshLayout = refreshLayout;
        paint = new Paint();

        dRef = FirebaseDatabase.getInstance().getReference("Sales");
        sRef = FirebaseStorage.getInstance().getReference();
    }

    public void refreshData(){
        DatabaseReference reference = dRef.child(fromFragment);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(fromFragment.equals("accounts")){

                    MainActivity.accountsList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Account temp = snapshot.getValue(Account.class);
                        MainActivity.accountsList.add(temp);
                    }
                    MainActivity.accountsAdapter = new AccountsAdapter(activity);
                    recyclerView.setAdapter(MainActivity.accountsAdapter);
                    refreshLayout.setRefreshing(false);

                } else if(fromFragment.equals("contacts")){

                    MainActivity.contactsList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Contact temp = snapshot.getValue(Contact.class);
                        MainActivity.contactsList.add(temp);
                    }
                    MainActivity.contactsAdapter = new ContactsAdapter(activity);
                    recyclerView.setAdapter(MainActivity.contactsAdapter);
                    refreshLayout.setRefreshing(false);

                } else if(fromFragment.equals("leads")){

                    MainActivity.leadsList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Lead lead = snapshot.getValue(Lead.class);
                        MainActivity.leadsList.add(lead);
                    }
                    MainActivity.leadsAdapter = new LeadsAdapter(activity);
                    recyclerView.setAdapter(MainActivity.leadsAdapter);
                    refreshLayout.setRefreshing(false);

                } else if(fromFragment.equals("deals")){

                    MainActivity.dealsList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Deal deal = snapshot.getValue(Deal.class);
                        MainActivity.dealsList.add(deal);
                    }
                    MainActivity.dealsAdapter = new DealsAdapter(activity);
                    recyclerView.setAdapter(MainActivity.dealsAdapter);
                    refreshLayout.setRefreshing(false);

                } else if(fromFragment.equals("events")){

                    MainActivity.eventsList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Event event = snapshot.getValue(Event.class);
                        MainActivity.eventsList.add(event);
                    }
                    MainActivity.eventsAdapter = new EventsAdapter(activity);
                    recyclerView.setAdapter(MainActivity.eventsAdapter);
                    refreshLayout.setRefreshing(false);

                } else if(fromFragment.equals("feeds")){

                    MainActivity.feedsList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Feed feed = snapshot.getValue(Feed.class);
                        MainActivity.feedsList.add(feed);
                    }
                    MainActivity.feedsAdapter = new FeedsAdapter(activity);
                    recyclerView.setAdapter(MainActivity.feedsAdapter);
                    refreshLayout.setRefreshing(false);

                } else if(fromFragment.equals("tasks")){

                    MainActivity.tasksList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Task temp = snapshot.getValue(Task.class);
                        MainActivity.tasksList.add(temp);
                    }
                    MainActivity.tasksAdapter = new TasksAdapter(activity);
                    recyclerView.setAdapter(MainActivity.tasksAdapter);
                    refreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Failed to Refresh Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if(direction == ItemTouchHelper.LEFT){
                    startDeleteDialog(position);
                }
                else if(direction == ItemTouchHelper.RIGHT){
                    startUpdateActivity(position);
                }
            }

            @Override
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                changeBackgroundColorOfItem(canvas, actionState, viewHolder, dX, dY);
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void changeBackgroundColorOfItem(Canvas canvas, int actionState, RecyclerView.ViewHolder viewHolder, float dX, float dY){
        Bitmap icon;
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;
            if(dX > 0){
                paint.setColor(Color.parseColor("#388E3C"));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                canvas.drawRect(background, paint);
                icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_edit_white);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                canvas.drawBitmap(icon,null,icon_dest, paint);
            } else {
                paint.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                canvas.drawRect(background, paint);
                icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_delete_white);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                canvas.drawBitmap(icon,null,icon_dest, paint);
            }
        }
    }

    private void startUpdateActivity(int position){
        Intent intent;
        if(fromFragment.equals("accounts")){
            intent = new Intent(activity, UpdateAccountActivity.class);
            String accountId = MainActivity.accountsList.get(position).getId();
            intent.putExtra("accountId", accountId);
        } else if(fromFragment.equals("contacts")){
            intent = new Intent(activity, UpdateContactActivity.class);
            String contactId = MainActivity.contactsList.get(position).getId();
            intent.putExtra("contactId", contactId);
        } else if(fromFragment.equals("leads")){
            intent = new Intent(activity, UpdateLeadActivity.class);
            String leadId = MainActivity.leadsList.get(position).getId();
            intent.putExtra("leadId", leadId);
        } else if(fromFragment.equals("deals")){
            intent = new Intent(activity, UpdateDealActivity.class);
            String dealId = MainActivity.dealsList.get(position).getId();
            intent.putExtra("dealId", dealId);
        } else if(fromFragment.equals("events")){
            intent = new Intent(activity, UpdateEventActivity.class);
            String eventId = MainActivity.eventsList.get(position).getId();
            intent.putExtra("eventId", eventId);
        } else if(fromFragment.equals("feeds")){
            intent = new Intent(activity, UpdateFeedActivity.class);
            String feedId = MainActivity.feedsList.get(position).getId();
            intent.putExtra("feedId", feedId);
        } else {
            intent = new Intent(activity, UpdateTaskActivity.class);
            String taskId = MainActivity.tasksList.get(position).getId();
            intent.putExtra("taskId", taskId);
        }
        intent.putExtra("position", position);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pop_enter, R.anim.still);
    }

    private void startDeleteDialog(final int position){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(activity);
        deleteDialog.setMessage("Are you sure you want to remove this item?");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(position);
                dialog.dismiss();
            }
        });
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshData();
                dialog.cancel();
            }
        });
        deleteDialog.setCancelable(false);
        deleteDialog.show();
    }

    public void removeItem(final int position){
        if(fromFragment.equals("accounts")){
            String id = MainActivity.accountsList.get(position).getId();
            DatabaseReference accountsRef = dRef.child("accounts");
            accountsRef.child(id).removeValue();                                  //Remove from Firebase
            MainActivity.accountsList.remove(position);                           //Remove from Local List
            MainActivity.accountsAdapter.notifyItemRemoved(position);
            MainActivity.accountsAdapter.notifyItemRangeChanged(position, MainActivity.accountsList.size());
            MainActivity.accountsAdapter.notifyDataSetChanged();
            refreshData();
        } else if(fromFragment.equals("contacts")){
            String id = MainActivity.contactsList.get(position).getId();
            DatabaseReference contactsRef = dRef.child("contacts");
            contactsRef.child(id).removeValue();                                  //Remove from Firebase
            MainActivity.contactsList.remove(position);                           //Remove from Local List
            MainActivity.contactsAdapter.notifyItemRemoved(position);
            MainActivity.contactsAdapter.notifyItemRangeChanged(position, MainActivity.contactsList.size());
            MainActivity.contactsAdapter.notifyDataSetChanged();
            refreshData();
        } else if(fromFragment.equals("leads")){
            String id = MainActivity.leadsList.get(position).getId();
            DatabaseReference leadsRef = dRef.child("leads");
            leadsRef.child(id).removeValue();                                  //Remove from Firebase
            MainActivity.leadsList.remove(position);                           //Remove from Local List
            MainActivity.leadsAdapter.notifyItemRemoved(position);
            MainActivity.leadsAdapter.notifyItemRangeChanged(position, MainActivity.leadsList.size());
            MainActivity.leadsAdapter.notifyDataSetChanged();
            refreshData();
        } else if(fromFragment.equals("deals")){
            String id = MainActivity.dealsList.get(position).getId();
            DatabaseReference dealsRef = dRef.child("deals");
            dealsRef.child(id).removeValue();                                  //Remove from Firebase
            MainActivity.dealsList.remove(position);                           //Remove from Local List
            MainActivity.dealsAdapter.notifyItemRemoved(position);
            MainActivity.dealsAdapter.notifyItemRangeChanged(position, MainActivity.dealsList.size());
            MainActivity.dealsAdapter.notifyDataSetChanged();
            refreshData();
        } else if(fromFragment.equals("events")){
            String id = MainActivity.eventsList.get(position).getId();
            DatabaseReference eventsRef = dRef.child("events");
            eventsRef.child(id).removeValue();                                  //Remove from Firebase
            MainActivity.eventsList.remove(position);                           //Remove from Local List
            MainActivity.eventsAdapter.notifyItemRemoved(position);
            MainActivity.eventsAdapter.notifyItemRangeChanged(position, MainActivity.eventsList.size());
            MainActivity.eventsAdapter.notifyDataSetChanged();
            refreshData();
        } else if(fromFragment.equals("feeds")){
            String id = MainActivity.feedsList.get(position).getId();
            DatabaseReference feedsRef = dRef.child("feeds");
            feedsRef.child(id).removeValue();                                  //Remove from Firebase
            MainActivity.feedsList.remove(position);                           //Remove from Local List
            MainActivity.feedsAdapter.notifyItemRemoved(position);
            MainActivity.feedsAdapter.notifyItemRangeChanged(position, MainActivity.feedsList.size());
            MainActivity.feedsAdapter.notifyDataSetChanged();
            refreshData();
        } else {
            String id = MainActivity.tasksList.get(position).getId();
            DatabaseReference tasksRef = dRef.child("tasks");
            tasksRef.child(id).removeValue();                                  //Remove from Firebase
            MainActivity.tasksList.remove(position);                           //Remove from Local List
            MainActivity.tasksAdapter.notifyItemRemoved(position);
            MainActivity.tasksAdapter.notifyItemRangeChanged(position, MainActivity.tasksList.size());
            MainActivity.tasksAdapter.notifyDataSetChanged();
            refreshData();
        }
    }

    /*public void addItem(Object item){
        if(fromFragment.equals("AccountsFragment")) {
            MainActivity.accountsList.add((Account) item);
            MainActivity.accountsAdapter.notifyItemInserted(MainActivity.accountsList.size());
        }
    }*/

}
