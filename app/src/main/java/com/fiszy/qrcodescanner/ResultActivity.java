package com.fiszy.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class ResultActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY = "/QRcodeFiszy";
    Boolean backpressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i=getIntent();

        final String name=i.getExtras().getString("Name");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);


//
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_VIEW);
//                            File file = new File(String.valueOf(path));
        if(Build.VERSION.SDK_INT>=24)
        {

            File f = new File(Environment.getExternalStorageDirectory()+IMAGE_DIRECTORY, name);

            // File imagepath = new File(Environment.getRootDirectory(), fileName);

            Uri photoUri  = FileProvider.getUriForFile(ResultActivity.this,"com.fiszy.qrcodescanner"+".fileprovider", f);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(photoUri,"image/*");
            //  intent.setDataAndType(Uri.parse("content://" + path), "image/*");
        }else
        {


            //intent.setDataAndType(Uri.parse("file://" + path), "image/*");
        }

        startActivity(intent);
        finish();
        backpressed = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (backpressed)
            finish();
    }
}
