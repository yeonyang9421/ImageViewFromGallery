package com.example.edu.imageviewfromgallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public  static  final  int  PERMISSION_REQUEST_CODE=100;
    Button mfromGalleryButton ,mimageCaptureButton;
    ImageView mimageViewFromGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mfromGalleryButton=(Button)findViewById(R.id.fromGalleryButton);
        mimageCaptureButton=(Button)findViewById(R.id.imageCaptureButton);
        mimageViewFromGallery=(ImageView)findViewById(R.id.imageViewFromGallery);
        mfromGalleryButton.setOnClickListener(this);
        mimageCaptureButton.setOnClickListener(this);


        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case  PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("", "Permission has been granted by user");
                }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromGalleryButton:
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/");
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
            case R.id.imageCaptureButton:
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                    Intent intent1=new Intent();
                    intent1.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent1, REQUEST_CODE_CAMERA);
                }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:

            if (data != null) {
                Uri selectedImage = data.getData();
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(selectedImage);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mimageViewFromGallery.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case REQUEST_CODE_CAMERA:
                if(resultCode==RESULT_OK){
                    Bundle extras =data.getExtras();
                    Bitmap bitmap=(Bitmap)extras.get("data");
                    mimageViewFromGallery.setImageBitmap(bitmap);

                }
        }
    }
}
