package com.fiszy.qrcodescanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment {

    //initialize variables to make them global
    private ImageButton Scan;
    private static final int SELECT_PHOTO = 100;
    //for easy manipulation of the result
    public String barcode;
    public ScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        Scan = (ImageButton)view.findViewById(R.id.ScanBut);

        //set a new custom listener
       // Scan.setOnClickListener((View.OnClickListener) getContext());
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch gallery via intent
                Intent photoPic = new Intent(Intent.ACTION_PICK);
                photoPic.setType("image/*");
                startActivityForResult(photoPic, SELECT_PHOTO);

            }
        });

        return view;
    }








}
