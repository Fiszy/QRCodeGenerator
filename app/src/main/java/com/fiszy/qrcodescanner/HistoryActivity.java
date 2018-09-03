package com.fiszy.qrcodescanner;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;

import com.fiszy.qrcodescanner.Recylcler.MyAdapter;
import com.fiszy.qrcodescanner.Recylcler.MyRecord;
import com.fiszy.qrcodescanner.Recylcler.SwipeHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class HistoryActivity extends BaseDrawerActivity {


    private RecyclerView recyclerView;
    private ArrayList<MyRecord> users  = new ArrayList<>();
    MyAdapter adapter;
    private AdView mAdView;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getLayoutInflater().inflate(R.layout.fragment_history, frameLayout);

        // Setting title
        setTitle("History");
        MobileAds.initialize(this, "ca-app-pub-8166358774047123~3055505349");

        mAdView = findViewById(R.id.myadViewhistory);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        recyclerView =(RecyclerView)findViewById(R.id.sqlite_recycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyAdapter(this,users);
        retriev();

        ItemTouchHelper.Callback callback=new SwipeHelper(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(3).setChecked(true);
    }

    private void retriev()
    {
        users.clear();
        RecordDBHelper contactHelper = new RecordDBHelper(this);
        SQLiteDatabase db = contactHelper.getReadableDatabase();
        Cursor cur = contactHelper.readContact(db);
        while (cur.moveToNext())
        {
            // user = new myUsers();
            int id = cur.getInt(0);
            String text = cur.getString(1);
            String date = cur.getString(2);

            MyRecord user = new MyRecord(id,text,date);
            users.add(user);
        }

        if(!(users.size()<1))
        {
            recyclerView.setAdapter(adapter);
        }
        contactHelper.close();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
