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

import com.hfad.findandplayA.viewmodels.Game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hfad.findandplayA.viewmodels.Game;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFunctionality extends Activity {

    private ImageView pictureImageView, itemOneImageView, itemTwoImageView, itemThreeImageView, itemFourImageView;
    private Button clearImageBtn, openCameraButton;
    Uri pictureUri; // picture
    private static final int requestCode = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_camera_page);

        openCameraButton = findViewById(R.id.btn_Camera);
        clearImageBtn = findViewById(R.id.clearImageBtn);
        pictureImageView = findViewById(R.id.cameraImageView);
        itemOneImageView = findViewById(R.id.itemOneIV);
        itemTwoImageView = findViewById(R.id.itemTwoIV);
        itemThreeImageView = findViewById(R.id.itemThreeIV);
        itemFourImageView = findViewById(R.id.itemFourIV);


        clearImageBtn.setVisibility(View.GONE); // hides the clear image button
        openCameraButton.setVisibility(View.GONE); // hides the camera button

        setItemImages(); // calls function to set item image, images

        // onClick for the camera button
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                userPermissions();
            }
        });

        // onClick for the clear image button
        clearImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearImage();
            }
        });

        // onClick for the first items image view will change border to purple, shows the camera button and needs to pass item data
        itemOneImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                itemOneImageView.setBackground(getDrawable(R.drawable.purple_camera_item_border));
                openCameraButton.setVisibility(View.VISIBLE);

                // TODO add code to pass item data?
            }
        });

        // onClick for the second items image view will change border to purple, shows the camera button and needs to pass item data
        itemTwoImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                itemTwoImageView.setBackground(getDrawable(R.drawable.purple_camera_item_border));
                openCameraButton.setVisibility(View.VISIBLE);

                // TODO add code to pass item data?
            }
        });

        // onClick for the third items image view will change border to purple, shows the camera button and needs to pass item data
        itemThreeImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                itemThreeImageView.setBackground(getDrawable(R.drawable.purple_camera_item_border));
                openCameraButton.setVisibility(View.VISIBLE);

                // TODO add code to pass item data?
            }
        });

        // onClick for the fourth items image view will change border to purple, shows the camera button and needs to pass item data
        itemFourImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                itemFourImageView.setBackground(getDrawable(R.drawable.purple_camera_item_border));
                openCameraButton.setVisibility(View.VISIBLE);

                // TODO add code to pass item data?
            }
        });
    }

    // function will check to see if permissions have been granted, if so the camera will open if not permissions will be requested
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

    // function will again run the userPermissions function
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        userPermissions();
    }

    // function to open the camera and run the create picture function to save the picture to the device
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void camera(){
        Uri picturePath = createPicture();
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCamera.putExtra(MediaStore.EXTRA_OUTPUT,picturePath);
        startActivityForResult(openCamera, 100);
    }

    // function will add the picture taken to the imageview, show the clear image button and print a message indicating it was saved to the device
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pictureImageView.setImageURI(pictureUri);
        clearImageBtn.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Picture Saved to Pictures/Find and Play Pictures", Toast.LENGTH_LONG).show();
    }

    public void clearImage() { // Function to clear image and hide the clear image button

        pictureImageView.setImageDrawable(null);
        clearImageBtn.setVisibility(View.GONE);

    }

    // function to create a folder location on the device and save the picture with the current date and time
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

    public void setItemImages(){

        //TODO implement code to set the three item imageview images

    }

}
