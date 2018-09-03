package com.fiszy.qrcodescanner.Recylcler;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.fiszy.qrcodescanner.HistoryFragment;
import com.fiszy.qrcodescanner.R;
import com.fiszy.qrcodescanner.RecordDBHelper;
import com.fiszy.qrcodescanner.RecordDetailActivity;

import java.text.DateFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    ArrayList<MyRecord> users;
    ItemClickListener listener;
;


    public MyAdapter(Context c, ArrayList<MyRecord> users) {
        this.c = c;
        this.users = users;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);


        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(c);
          //  holder.myid.setText(dateFormat.format(users.get(position).getDate())+"");
        holder.myid.setText(users.get(position).getDate());
            holder.myname.setText(users.get(position).getText());

            if(!URLUtil.isValidUrl(users.get(position).getText()))
            {
                holder.icon.setImageResource(R.drawable.ic_text);
            }

        //WHEN ITEM IS CLICKED
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                //INTENT OBJ
                Intent i=new Intent(c,RecordDetailActivity.class);

                //ADD DATA TO OUR INTENT
                i.putExtra("Name",users.get(position).getText());
                i.putExtra("Date",users.get(position).getDate());


                //START DETAIL ACTIVITY
                c.startActivity(i);




            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public void deleteRecord(int pos)
    {
        //GET ID
        MyRecord p=users.get(pos);
        int id=p.getId();

        //DELETE FROM DB
        RecordDBHelper db=new RecordDBHelper(c);
       // RecordDBHelper recordHelper = new RecordDBHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();
        if(db.delete(id,database))
        {
            users.remove(pos);
        }else
        {
            Toast.makeText(c,"Unable To Delete",Toast.LENGTH_SHORT).show();
        }

        db.close();

        this.notifyItemRemoved(pos);
        Log.d(TAG, "deleteRecord: deleted record");
//        HistoryFragment historyFragment = new HistoryFragment();
//        historyFragment.showSnak();
        Toast.makeText(c, "Record Deleted", Toast.LENGTH_SHORT).show();




    }
}
