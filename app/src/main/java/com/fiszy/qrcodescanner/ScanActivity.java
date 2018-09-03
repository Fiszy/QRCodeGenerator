package com.fiszy.qrcodescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    String mine;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        ScanBarcode.tvresult.setText(rawResult.getText());
        mine = rawResult.getText().toString();
        ScanBarcode.showMenu(R.id.nav_share);
        ScanBarcode.showMenu(R.id.nav_copy);
        ScanBarcode.linearLayout2.setVisibility(View.VISIBLE);
        ScanBarcode.start.setVisibility(View.GONE);

        if(!URLUtil.isValidUrl(rawResult.getText()))
        {
            ScanBarcode. openurl2.setVisibility(View.GONE);
            ScanBarcode.linkImg2.setImageResource(R.drawable.ic_text);
            ScanBarcode. weblink2.setText("PlainText");
        }else
        {
            ScanBarcode.openurl2.setVisibility(View.VISIBLE);
            ScanBarcode.linkImg2.setImageResource(R.drawable.ic_link);
            ScanBarcode.weblink2.setText("Weblink");
            ScanBarcode.linkopen2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(rawResult.getText()));
                    intent.setAction(Intent.ACTION_VIEW);
                    startActivity(intent);
                }
            });
        }
       // myt.setText(barcode);

        Boolean mys = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_PREF_SYNC_URL,false);

        if (mys && URLUtil.isValidUrl(rawResult.getText())){
            Intent intent = new Intent();

            intent.setData(Uri.parse(rawResult.getText()));
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent);

        }
        Boolean mys1 = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_PREF_SYNC_COPY,false);
        if (mys1)
        {
            ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Copied Code",mine);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Copied to clipboard.", Toast.LENGTH_SHORT).show();
        }
        Boolean mysound = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_PREF_SYNC_CONN,false);
        if(mysound)
        {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
            mp.start();
        }




        RecordDBHelper recordHelper = new RecordDBHelper(this);
        SQLiteDatabase database = recordHelper.getWritableDatabase();
        recordHelper.addRecord(rawResult.getText(),database);
        onBackPressed();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

}