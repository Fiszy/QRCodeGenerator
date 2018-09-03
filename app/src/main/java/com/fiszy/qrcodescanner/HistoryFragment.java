package com.fiszy.qrcodescanner;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiszy.qrcodescanner.Recylcler.MyAdapter;
import com.fiszy.qrcodescanner.Recylcler.MyRecord;
import com.fiszy.qrcodescanner.Recylcler.SwipeHelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<MyRecord> users  = new ArrayList<>();
    MyAdapter adapter;
    CoordinatorLayout coordinatorLayout;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView =(RecyclerView)view.findViewById(R.id.sqlite_recycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyAdapter(getActivity(),users);
        retriev();

        ItemTouchHelper.Callback callback=new SwipeHelper(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        return view;
    }


    private void retriev()
    {
        users.clear();
        RecordDBHelper contactHelper = new RecordDBHelper(getActivity());
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

//    public void showSnak(){
//        Snackbar snackbar = Snackbar
//                .make(coordinatorLayout, "Record deleted", Snackbar.LENGTH_LONG);
//
//
//        snackbar.show();
//
//    }

}
