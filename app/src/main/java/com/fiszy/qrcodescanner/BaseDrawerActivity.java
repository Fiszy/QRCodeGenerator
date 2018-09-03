package com.fiszy.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        frameLayout = (FrameLayout) findViewById(R.id.frag_container);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        switch (id) {
            case R.id.nav_create:

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_scan:

                startActivity(new Intent(getApplicationContext(), ScanBarcode.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_scan_history:

                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_scan_gallery:
                startActivity(new Intent(getApplicationContext(), ScannGallery.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(), MySettings.class));
                overridePendingTransition(0,0);
                break;
                case R.id.nav_about:
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                overridePendingTransition(0,0);
                break;
                case R.id.nav_menu_share:
                    Intent intent = new Intent();
                  intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, Constant.SHARE_CONTENT);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));;

        }

//        if (id == R.id.nav_create) {
//            // Handle the camera action
//            startActivity(new Intent(getApplicationContext(), CameraActivity.class));
//        } else if (id == R.id.nav_gallery) {
//            startActivity(new Intent(getApplicationContext(), GalleryActivity.class));
//        } else if (id == R.id.nav_slideshow) {
//            startActivity(new Intent(getApplicationContext(), SlideshowActivity.class));
//        } else if (id == R.id.nav_manage) {
//            startActivity(new Intent(getApplicationContext(), ManageActivity.class));
//        } else if (id == R.id.nav_share) {
//            startActivity(new Intent(getApplicationContext(), ShareActivity.class));
//        } else if (id == R.id.nav_send) {
//            startActivity(new Intent(getApplicationContext(), SendActivity.class));
//        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
