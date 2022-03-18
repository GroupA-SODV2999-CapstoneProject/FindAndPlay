package com.hfad.findandplayA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFunctionality extends Activity {

    private ImageView pictureImageView;
    private Button clearImageBtn;
    Uri pictureUri;// picture
    private static final int requestCode = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_camera_page);

        Button openCameraButton = findViewById(R.id.btn_Camera);
        clearImageBtn = findViewById(R.id.clearImageBtn);
        pictureImageView = findViewById(R.id.cameraImageView);

        clearImageBtn.setVisibility(View.GONE);

        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                userPermissions();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void userPermissions(){
        String [] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED) {

            camera();

        }
        else{
            ActivityCompat.requestPermissions(CameraFunctionality.this,permissions,requestCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        userPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void camera(){
        Uri picturePath = createPicture();
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCamera.putExtra(MediaStore.EXTRA_OUTPUT,picturePath);
        startActivityForResult(openCamera, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pictureImageView.setImageURI(pictureUri);
        clearImageBtn.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Picture Saved to Pictures/Find and Play Pictures", Toast.LENGTH_LONG).show();
    }

    public void clearImage(View view) {
        pictureImageView.setImageDrawable(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Uri createPicture(){
        Uri uri;
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String pictureName = String.valueOf(format.format(date));
        ContentResolver resolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, pictureName + ".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/" + "Find and Play Pictures/");
        Uri finalPictureUri = resolver.insert(uri, contentValues);
        pictureUri = finalPictureUri;
        return finalPictureUri;
    }

}
