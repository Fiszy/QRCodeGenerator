package com.fiszy.qrcodescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordDetailActivity extends BaseDrawerActivity {

    TextView nameTxt;
    Toolbar toolbar;
    LinearLayout  openurl1;
    ImageView  linkopen1,linkImg1;
    TextView weblink1,datetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_record_detail);
        getLayoutInflater().inflate(R.layout.activity_record_detail, frameLayout);
       // toolbar = findViewById(R.id.toolbar1);
       // setSupportActionBar(toolbar);
      //  final ActionBar actionbar = getSupportActionBar();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       setTitle("History");

        openurl1 = (LinearLayout)findViewById(R.id.openlink1);
        linkImg1 = (ImageView)findViewById(R.id.linkimg1);
        linkopen1 = (ImageView)findViewById(R.id.linkopen1);
        weblink1 = (TextView)findViewById(R.id.weblink1);
        datetext = (TextView)findViewById(R.id.datetext);
       toggle.setDrawerIndicatorEnabled(false);
       toggle.setHomeAsUpIndicator(R.drawable.ic_back);
       toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onBackPressed();
           }
       });


        Intent i=getIntent();

        final String name=i.getExtras().getString("Name");
        final  String date = i.getExtras().getString("Date");
        //REFERENCE VIEWS FROM XML

        nameTxt= (TextView) findViewById(R.id.nametext);


        //ASSIGN DATA TO THOSE VIEWS

        nameTxt.setText(name);
        datetext.setText(date);
        if(URLUtil.isValidUrl(nameTxt.getText().toString())){
            linkopen1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(nameTxt.getText().toString()));
                    intent.setAction(Intent.ACTION_VIEW);
                    startActivity(intent);
                }
            });

        }else{
            openurl1.setVisibility(View.GONE);
            weblink1.setText("PlainText");
            linkImg1.setImageResource(R.drawable.ic_text);
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(3).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_list,menu);

//        MenuItem item = menu.findItem(R.id.nav_share);
//        MenuItem item1 = menu.findItem(R.id.nav_copy);
//        item.setVisible(false);
//        item1.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, nameTxt.getText().toString());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share Bar code Result"));
                break;
            case R.id.nav_copy:
                copy();
        }
        return super.onOptionsItemSelected(item);
    }
    public void copy()
    {
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied Code",nameTxt.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Copied to clipboard.", Toast.LENGTH_SHORT).show();

    }
}
