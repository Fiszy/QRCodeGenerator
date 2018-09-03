package com.fiszy.qrcodescanner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

//import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {

    private ImageButton Scan;
    private static final int SELECT_PHOTO = 100;
    //for easy manipulation of the result
    public String barcode;
    private DrawerLayout mDrawerLayout;
    public static TextView tvtv_result;
    Button btn_start;
    boolean isFlash;
    Camera camera;
    Camera.Parameters parameters;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.my_pref, false);
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        Scan = (ImageButton)findViewById(R.id.ScanBut);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Create QR Code");
     //   CheckBoxPreference mys = (CheckBoxPreference) fin

       // SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
      //  String syncConnPref = sharedPref.getString(SettingsFragment.KEY_PREF_SYNC_CONN, "");
        Boolean mys2 = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_PREF_SYNC_NOT,false);

//        if (mys2){
//            actionbar.setTitle("youuuuu");
//        }


        //actionbar.setTitle(syncConnPref);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        switch (menuItem.getItemId()) {
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
                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));

                        }

                        return true;
                    }
                });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        if (findViewById(R.id.frag_container)!= null)
        {
            if (savedInstanceState != null){
                return;
            }
            CreateFragment homeFragment = new CreateFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frag_container,homeFragment).commit();
        }

//
//        tvtv_result = (TextView) findViewById(R.id.txtv_result);
//        btn_start = (Button) findViewById(R.id.btn_qr_start);
//        checkStat();
//        btn_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // kamera izni kontrolü için bir sınıf yazmıştık ve bir metod yazmıştık,
//                // getCameraPermission sınıfı içerisine boolean değer dönderen isCameraPermission metodu
//                // şimdi onu kullanalım ve duruma göre okuma ve izin konusunda yol izleyelim
//
//                // we wrote a class for camera permission control and we wrote a method
//                //The isCameraPermission method, which returns a boolean value into the getCameraPermission class
//
//                if(isFlash)
//                {
//                    //izin alındığına göre okuma sınıfını çalıştıralım
//                    // Permission is granted and approved, go ScanClass
//                    Intent intent = new Intent(MainActivity.this, ScanClass.class);
//                    startActivity(intent);
//
//                }
//
//            }
     //   });

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
                  //  camera = Camera.open();
                    //parameters = camera.getParameters();
                    isFlash = true;
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
              break;
            case R.id.top_settings:
                startActivity(new Intent(getApplicationContext(), MySettings.class));
                overridePendingTransition(0,0);
                break;
                case R.id.nav_up_about:

                startActivity(new Intent(getApplicationContext(), AboutUs.class));
                overridePendingTransition(0,0);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.other_menu,menu);

        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }



//    //call the onactivity result method
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        Log.d("ScanFragment", "onActivityResult: we are here "+requestCode);
//        switch (requestCode) {
//            case 100:
//                if (resultCode == RESULT_OK) {
//                    Log.d("ScanFragment", "onActivityResult: we are here again");
////doing some uri parsing
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    InputStream imageStream = null;
//                    try {
//                        //getting the image
//                        imageStream = getContentResolver().openInputStream(selectedImage);
//                    } catch (FileNotFoundException e) {
//                        Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//                    //decoding bitmap
//                    Bitmap bMap = BitmapFactory.decodeStream(imageStream);
//                    Scan.setImageURI(selectedImage);// To display selected image in image view
//                    int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
//                    // copy pixel data from the Bitmap into the 'intArray' array
//                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(),
//                            bMap.getHeight());
//
//                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(),
//                            bMap.getHeight(), intArray);
//                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//                    Reader reader = new MultiFormatReader();// use this otherwise
//                    // ChecksumException
//                    try {
//                        Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();
//                        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//                        decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
//
//                        Result result = reader.decode(bitmap, decodeHints);
//                        //*I have created a global string variable by the name of barcode to easily manipulate data across the application*//
//                        barcode =  result.getText().toString();
//
//                        //do something with the results for demo i created a popup dialog
//                        if(barcode!=null){
//                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                            builder.setTitle("Scan Result");
//                            builder.setIcon(R.mipmap.ic_launcher);
//                            builder.setMessage("" + barcode);
//                            AlertDialog alert1 = builder.create();
//                            alert1.setButton(DialogInterface.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent i = new Intent (getBaseContext(),MainActivity.class);
//                                    startActivity(i);
//                                }
//                            });
//
//                            alert1.setCanceledOnTouchOutside(false);
//
//                            alert1.show();}
//                        else
//                        {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                            builder.setTitle("Scan Result");
//                            builder.setIcon(R.mipmap.ic_launcher);
//                            builder.setMessage("Nothing found try a different image or try again");
//                            AlertDialog alert1 = builder.create();
//                            alert1.setButton(DialogInterface.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent i = new Intent (getBaseContext(),MainActivity.class);
//                                    startActivity(i);
//                                }
//                            });
//
//                            alert1.setCanceledOnTouchOutside(false);
//
//                            alert1.show();
//
//                        }
//                        //the end of do something with the button statement.
//
//                    } catch (NotFoundException e) {
//                        Toast.makeText(getApplicationContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    } catch (ChecksumException e) {
//                        Toast.makeText(getApplicationContext(), "Something weird happen, i was probably tired to solve this issue", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    } catch (FormatException e) {
//                        Toast.makeText(getApplicationContext(), "Wrong Barcode/QR format", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    } catch (NullPointerException e) {
//                        Toast.makeText(getApplicationContext(), "Something weird happen, i was probably tired to solve this issue", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//                }
//        }
//        Log.d("ScanFragment", "onActivityResult: we are here no more");
//    }

}