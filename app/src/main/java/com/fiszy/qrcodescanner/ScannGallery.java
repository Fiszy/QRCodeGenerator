package com.fiszy.qrcodescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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

public class ScannGallery extends BaseDrawerActivity implements View.OnClickListener{

    //initialize variables to make them global
    private ImageButton Scan;
    private static final int SELECT_PHOTO = 100;
    //for easy manipulation of the result
    public String barcode;
    LinearLayout linearLayout, openurl;
    ImageView linkImg, linkopen;
    TextView myt,weblink;
    Boolean where = false;
    private Menu menu;
    private AdView mAdView;
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


//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//         super.onPrepareOptionsMenu(menu);
//
//        MenuItem item = menu.findItem(R.id.nav_share);
//       // Boolean mys = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_PREF_SYNC_CONN,false);
//
//        if (where)
//        {
//            item.setVisible(true);
//        }else{
//            item.setVisible(false);
//        }
//
//        return true;
//    }

    public void showMenu(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }
    public void hideMenu(){
            MenuItem item = menu.findItem(R.id.nav_share);
            MenuItem item2 = menu.findItem(R.id.nav_copy);
        item.setVisible(false);
        item2.setVisible(false);
    }

    //call oncreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_scann_gallery);

        getLayoutInflater().inflate(R.layout.activity_scann_gallery, frameLayout);

        // Setting title
        setTitle("Scan From Gallery");

        MobileAds.initialize(this, "ca-app-pub-8188358964887123~3055505349");

        mAdView = findViewById(R.id.myadViewgallery);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        myt = (TextView)findViewById(R.id.realtext);
        linearLayout = (LinearLayout)findViewById(R.id.topside);
        openurl = (LinearLayout)findViewById(R.id.openlink);
        linkImg = (ImageView)findViewById(R.id.linkimg);
        linkopen = (ImageView)findViewById(R.id.linkopen);
        weblink = (TextView)findViewById(R.id.weblink);
        linearLayout.setVisibility(View.GONE);

        //cast neccesary variables to their views
        Scan = (ImageButton)findViewById(R.id.ScanB);

        //set a new custom listener
        Scan.setOnClickListener(this);


    }

    //do necessary coding for each ID
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ScanB:
                //launch gallery via intent
                Intent photoPic = new Intent(Intent.ACTION_PICK);
                photoPic.setType("image/*");
                startActivityForResult(photoPic, SELECT_PHOTO);
                break;
        }
    }

    public void copy()
    {
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied Code",barcode);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Copied to clipboard.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, barcode);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share Bar code Result"));
                break;
            case R.id.nav_copy:
                copy();
        }
        return super.onOptionsItemSelected(item);
    }

    //call the onactivity result method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
//doing some uri parsing
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        //getting the image
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    //decoding bitmap
                    Bitmap bMap = BitmapFactory.decodeStream(imageStream);
                    Scan.setImageURI(selectedImage);// To display selected image in image view
                    int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
                    // copy pixel data from the Bitmap into the 'intArray' array
                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(),
                            bMap.getHeight());

                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(),
                            bMap.getHeight(), intArray);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    Reader reader = new MultiFormatReader();// use this otherwise
                    // ChecksumException
                    try {
                        Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();
                        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                        decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

                        Result result = reader.decode(bitmap, decodeHints);
                        //*I have created a global string variable by the name of barcode to easily manipulate data across the application*//
                        barcode =  result.getText().toString();

                        //do something with the results for demo i created a popup dialog
                        if(barcode!=null){
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Scan Result");
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setMessage("" + barcode);
                            showMenu(R.id.nav_share);
                            showMenu(R.id.nav_copy);
                            linearLayout.setVisibility(View.VISIBLE);

                            if(!URLUtil.isValidUrl(barcode))
                            {
                               openurl.setVisibility(View.GONE);
                               linkImg.setImageResource(R.drawable.ic_text);
                               weblink.setText("PlainText");
                            }else
                            {
                                openurl.setVisibility(View.VISIBLE);
                                linkImg.setImageResource(R.drawable.ic_link);
                                weblink.setText("Weblink");
                                linkopen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent();
                                        intent.setData(Uri.parse(barcode));
                                        intent.setAction(Intent.ACTION_VIEW);
                                        startActivity(intent);
                                    }
                                });
                            }
                            myt.setText(barcode);

                            Boolean mys = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_PREF_SYNC_URL,false);

                            if (mys && URLUtil.isValidUrl(barcode)){
                                Intent intent = new Intent();

                                intent.setData(Uri.parse(barcode));
                                intent.setAction(Intent.ACTION_VIEW);
                                startActivity(intent);

                            }
                            Boolean mys1 = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_PREF_SYNC_COPY,false);
                            if (mys1)
                            {
                                copy();
                            }
                            Boolean mysound = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_PREF_SYNC_CONN,false);
                            if(mysound)
                            {
                                final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
                                mp.start();
                            }

                            final AlertDialog alert1 = builder.create();
                            alert1.setButton(DialogInterface.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent i = new Intent (getBaseContext(),ScannGallery.class);
//                                    startActivity(i);
                                    alert1.dismiss();
                                }
                            });

                            alert1.setCanceledOnTouchOutside(false);

                            alert1.show();
                            RecordDBHelper recordHelper = new RecordDBHelper(this);
                            SQLiteDatabase database = recordHelper.getWritableDatabase();
                            recordHelper.addRecord(barcode,database);
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Scan Result");
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setMessage("Nothing found try a different image or try again");
                            AlertDialog alert1 = builder.create();
                            alert1.setButton(DialogInterface.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent (getBaseContext(),ScannGallery.class);
                                    startActivity(i);
                                }
                            });

                            alert1.setCanceledOnTouchOutside(false);
                              linearLayout.setVisibility(View.GONE);
                              hideMenu();

                            alert1.show();

                        }
                        //the end of do something with the button statement.

                    } catch (NotFoundException e) {
                        Toast.makeText(getApplicationContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.GONE);
                        hideMenu();
                        e.printStackTrace();
                    } catch (ChecksumException e) {
                        Toast.makeText(getApplicationContext(), "Something weird happen, i was probably tired to solve this issue", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (FormatException e) {
                        Toast.makeText(getApplicationContext(), "Wrong Barcode/QR format", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        Toast.makeText(getApplicationContext(), "Something weird happen, i was probably tired to solve this issue", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }

}