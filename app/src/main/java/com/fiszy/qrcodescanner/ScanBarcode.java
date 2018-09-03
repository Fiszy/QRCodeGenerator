package com.fiszy.qrcodescanner;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ScanBarcode extends BaseDrawerActivity {
    public static TextView tvresult;
    private Button btn;
    boolean isFlash;
    Camera camera;
    Camera.Parameters parameters;
    private AdView mAdView;


    public static  LinearLayout linearLayout2, openurl2;
    public  static ImageView linkImg2, linkopen2;
    public static  TextView myt,weblink2,start;

    private static Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_list,menu);

        MenuItem item = menu.findItem(R.id.nav_share);
        MenuItem item1 = menu.findItem(R.id.nav_copy);
        item.setVisible(false);
        item1.setVisible(false);
        return true;
    }

    public static void showMenu(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }
    public void copy()
    {
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied Code",tvresult.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Copied to clipboard.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, tvresult.getText().toString());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share Bar code Result"));
                break;
            case R.id.nav_copy:
                copy();
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_scan_barcode, frameLayout);

        // Setting title
        setTitle("Scan From Camera");

        tvresult = (TextView) findViewById(R.id.mtvresult);
        MobileAds.initialize(this, "ca-app-pub-8102353967747123~3055505349");

        mAdView = findViewById(R.id.myadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        linearLayout2 = (LinearLayout)findViewById(R.id.topside2);
        openurl2 = (LinearLayout)findViewById(R.id.openlink2);
        linkImg2 = (ImageView)findViewById(R.id.linkimg2);
        linkopen2 = (ImageView)findViewById(R.id.linkopen2);
        weblink2 = (TextView)findViewById(R.id.weblink2);
        start = (TextView)findViewById(R.id.start);
        linearLayout2.setVisibility(View.GONE);


        btn = (Button) findViewById(R.id.btnScan);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (checkStat()) {
                   Intent intent = new Intent(ScanBarcode.this, ScanActivity.class);
                   startActivity(intent);

               }else {
                   checkStat();
               }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    private boolean checkStat() {

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                camera = Camera.open();
                parameters = camera.getParameters();
                isFlash = true;
            }


        }
        return isFlash;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                      camera = Camera.open();
                    parameters = camera.getParameters();
                    isFlash = true;
                    Intent intent = new Intent(ScanBarcode.this, ScanActivity.class);
                    startActivity(intent);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
