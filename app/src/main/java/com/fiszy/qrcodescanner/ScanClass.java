package com.fiszy.qrcodescanner;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by MilcanX on 02.09.2017.
 */

public class ScanClass extends AppCompatActivity implements ZXingScannerView.ResultHandler
{

    private ZXingScannerView  mScannerView;

    // In onCreate() method, we have set view as scannerview instead of xml file layout.
    // In onResume() method, we have set the resulthandler and started camera preview.
    // In onPause() method, we are stopping camera preview.

    // OnCreate () yönteminde, görünümü xml dosya düzeni yerine scannerview olarak ayarladık.
    // onResume () yönteminde resulandler ayarladık ve kamera önizlemesini başlattık.
    // OnPause () yönteminde kamera önizlemesini durduruyoruz.

    //camera permission is needed. getCameraPermission class ;)
    // kamera iznine ihtiyaç vardır. getCameraPermission class ;)

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Programmatically initialize the scanner view
        // Tarayıcı görünümünü programlı olarak başlatır
        mScannerView = new ZXingScannerView (ScanClass.this);
        // Set the scanner view as the content view
        // Tarayıcı görünümünü içerik görünümü olarak ayarlayın
        setContentView(mScannerView);


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
    public void handleResult(Result result) {

        // Do something with the result here
        Log.v("kkkk", result.getText()); // Prints scan results - okunan veri
        Log.v("uuuu", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.) - okunanın türü

        MainActivity.tvtv_result.setText("Data : "+result.getText()+"\nFormat : "+result.getBarcodeFormat().toString());
        onBackPressed();

        // If you would like to resume scanning, call this method below:
        // Taramaya devam etmek isterseniz, aşağıdaki yöntemi arayın:
        mScannerView.resumeCameraPreview(this);
    }

}



