package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class PractitionerListActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<PractitionerItem> mItemList;
    private PractitionerItemAdapter mAdapter;
    private boolean viewRow = true;

    private int gridNumber = 2;
    private Integer itemLimit = 5;

    private FirebaseFirestore mFireStore;
    private CollectionReference mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practitioner_list);

        user = FirebaseAuth.getInstance().getCurrentUser();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();
        mAdapter = new PractitionerItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mFireStore = FirebaseFirestore.getInstance();
        mItems = mFireStore.collection("items");

        queryData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver, filter);

        //initializeData();
        

    }

    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();

            if (intentAction == null)
                return;

            switch (intentAction) {
                case Intent.ACTION_POWER_CONNECTED:
                    itemLimit = 8;
                    queryData();
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    itemLimit = 4;
                    queryData();
                    break;
            }
        }
    };

    private void queryData() {
        mItemList.clear();
        mItems.orderBy("name").limit(itemLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                PractitionerItem item = document.toObject(PractitionerItem.class);
                mItemList.add(item);
            }

            // Notify the adapter of the change.
            mAdapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        String[] itemsList = getResources().getStringArray(R.array.item_names);
        //String[] itemsInfo = getResources().getStringArray(R.array.item_names);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.item_images);

        mItemList.clear();

        for (int i = 0; i<itemsList.length; i++){
            //mItems.add(new PractitionerItem(itemsList[i], 1));
        }

        itemsImageResource.recycle();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryData();
    }

    public void toElement(int id) {
        Intent intent = new Intent(this, ElementActivity.class);
        intent.putExtra("SECRET", 99);
        intent.putExtra("ID", id);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_plus:
                mItems.orderBy("identifier").limitToLast(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        PractitionerItem item2 = document.toObject(PractitionerItem.class);
                        mItems.add(new PractitionerItem("", item2.getIdentifier()+1, false, "", "", "", new Date(), "avatar1", "", ""));
                        toElement(item2.getIdentifier()+1);
                    }
                });
                return false;
            case R.id.app_bar_view:
                if (!viewRow) {
                    changeSpanCount(item, R.drawable.view_stream, 2);
                } else {
                    changeSpanCount(item, R.drawable.view_module, 1);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }

}