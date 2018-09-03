package com.fiszy.qrcodescanner;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment {
    public final static int QRcodeWidth = 500 ;
    private static final String IMAGE_DIRECTORY = "/QRcodeFiszy";
    Bitmap bitmap ;
    TextView mytext;
    private EditText etqr;
    private ImageView iv;
    private Button btn;
    String fileName;
    CoordinatorLayout coordinatorLayout;
    private AdView mAdView;
    private final String CHANNEL_ID ="qr_notification";


    public CreateFragment() {
        // Required empty public constructor
    }

    public interface OnDbListener
    {
        public void dbOperationPerform(int method);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        iv = (ImageView) view.findViewById(R.id.iv);
        etqr = (EditText)view. findViewById(R.id.etqr);

        btn = (Button) view.findViewById(R.id.btn);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.myframe);
        MobileAds.initialize(getContext(), "ca-app-pub-8102358964047123~3055505349");

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            hideKeyboar();


                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

                if(etqr.getText().toString().trim().length() == 0){
                    Toast.makeText(getContext(), "Enter String!", Toast.LENGTH_SHORT).show();
                }else {

                    //waitingDialog.setTitle("Generating QR Code");

                //    try {

                        new GenerateQR().execute(bitmap, bitmap);
                      //  bitmap = TextToImageEncode(etqr.getText().toString());
                       // iv.setImageBitmap(bitmap);

                      //  String path = saveImage(bitmap);  //give read write permission
                       // Toast.makeText(getContext(), "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();
                 //   } catch (WriterException e) {
                    //    e.printStackTrace();
                   // }
                   // waitingDialog.dismiss();

                }
            }
        });

        return  view;

    }

    private void hideKeyboar() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(),0);
    }

    public String saveImage(Bitmap myBitmap) {


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {

            fileName = Calendar.getInstance()
                    .getTimeInMillis()+ ".jpg";
            File f = new File(wallpaperDirectory, fileName);
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


    private class GenerateQR extends AsyncTask<Bitmap, String, Bitmap> {
        final android.app.AlertDialog waitingDialog = new SpotsDialog(getContext());

        protected Bitmap doInBackground(Void... voids) {

            return null;

        }

        protected void onProgressUpdate(String... progress) {

        }

        protected void onPostExecute(Bitmap result) {
         //   Toast.makeText(getContext(), "done with it", Toast.LENGTH_SHORT).show();

            waitingDialog.dismiss();
            iv.setImageBitmap(bitmap);
                                        Boolean mysound = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsFragment.KEY_PREF_SYNC_CONN,false);
                            if(mysound)
                            {
                                final android.media.MediaPlayer mp = android.media.MediaPlayer.create(getContext(), R.raw.sound);
                                mp.start();

                            }
              final String path = saveImage(bitmap);  //give read write permission
             Toast.makeText(getContext(), "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();
           // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(path))); /** replace with your own uri */
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "QRCode Saved", Snackbar.LENGTH_LONG)
                    .setAction("VIEW", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);


//
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_VIEW);
//                            File file = new File(String.valueOf(path));
                            if(Build.VERSION.SDK_INT>=24)
                            {

                                File f = new File(Environment.getExternalStorageDirectory()+IMAGE_DIRECTORY, fileName);

                                // File imagepath = new File(Environment.getRootDirectory(), fileName);

                                Uri photoUri  = FileProvider.getUriForFile(getContext(),"com.fiszy.qrcodescanner"+".fileprovider", f);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(photoUri,"image/*");
                                //  intent.setDataAndType(Uri.parse("content://" + path), "image/*");
                            }else
                            {


                                intent.setDataAndType(Uri.parse("file://" + path), "image/*");
                            }

                            startActivity(intent);
                        }
                    });
//            Notification notification = new NotificationCompat.Builder(getContext(),"Personal _notifications")
//                    .setSmallIcon(R.drawable.ic_camera)
//                    .setContentTitle("QR Code created")
//                    .setContentText(etqr.getText().toString())
//                    .setLargeIcon(bitmap).setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setStyle(new NotificationCompat.BigPictureStyle()
//                            .bigPicture(bitmap)
//                            .bigLargeIcon(null))
//                    .build();

            Boolean mys2 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(SettingsFragment.KEY_PREF_SYNC_NOT,false);
            if(mys2)
            {
                createNotificationChannel();

                Intent intent;
               // File file = new File(String.valueOf(path));
                if(Build.VERSION.SDK_INT>=24)

                {
                    intent = new Intent(getContext(), ResultActivity.class);
                    intent.setAction(Intent.ACTION_VIEW);
                   intent.putExtra("Name",fileName);
                    File wallpaperDirectory = new File(
                            Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
                    File f = new File(wallpaperDirectory, fileName);

                   // File imagepath = new File(Environment.getRootDirectory(), fileName);

                    Uri photoUri  = FileProvider.getUriForFile(getContext(),"com.fiszy.qrcodescanner"+".fileprovider", f);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                   intent.setDataAndType(photoUri,"image/*");
                  //  intent.setDataAndType(Uri.parse("content://" + path), "image/*");
                }else
                {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);

                    intent.setDataAndType(Uri.parse("file://" + path), "image/*");
                }


//                FileProvider.getUriForFile(getContext(),
//                        BuildConfig.APPLICATION_ID + ".provider",
//                        createImageFile());


                //Intent intent = new Intent(this, ResultActivity.class);
// Set the Activity to start in a new, empty task
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);
                builder.setSmallIcon(R.drawable.ic_code);

                builder.setContentTitle("QR Code created")
                        .setContentText(etqr.getText().toString())
                        .setLargeIcon(bitmap).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap)
                                .bigLargeIcon(null))
                        .build();
                builder.addAction(R.drawable.ic_image,"View in Gallery",notifyPendingIntent);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                builder.setContentIntent(notifyPendingIntent);

// notificationId is a unique int for each notification that you must define
                notificationManager.notify(001, builder.build());

            }



          snackbar.show();
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.parse("file://" + path), "image/*");
//            startActivity(intent);

        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {

            try {
                bitmap = TextToImageEncode(etqr.getText().toString());
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
           // final android.app.AlertDialog waitingDialog = new SpotsDialog(getActivity());
            waitingDialog.setTitle("Generating");
            waitingDialog.show();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Personal notification";
            String description = "Include all description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
