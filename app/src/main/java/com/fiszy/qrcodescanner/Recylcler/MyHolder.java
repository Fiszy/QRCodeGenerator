package com.fiszy.qrcodescanner.Recylcler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiszy.qrcodescanner.R;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView myid, myname;
    ImageView icon;
    private ItemClickListener itemClickListener;


    public MyHolder(View itemView) {
        super(itemView);
        myid = (TextView)itemView.findViewById(R.id.myid);
        myname = (TextView)itemView.findViewById(R.id.myname);
        icon = (ImageView)itemView.findViewById(R.id.icon);
       // this.listener = listener;
        itemView.setOnClickListener(this);

      //  itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;

    }
}
