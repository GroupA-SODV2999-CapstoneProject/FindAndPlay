package com.hfad.findandplayA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hfad.findandplayA.viewmodels.Game;
import com.hfad.findandplayA.viewmodels.PlayItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiConsumer;

public class CameraFunctionality extends Activity {

    private ImageView pictureImageView, itemOneImageView, itemTwoImageView, itemThreeImageView, itemFourImageView, selectedImageView;
    private Button openCameraButton;
    Uri pictureUri; // picture
    private static final int requestCode = 100;
    private String playerName = "";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_camera_page);
        playerName = getIntent().getStringExtra("NAME_KEY");
        if(playerName.isEmpty()) {
            Log.d("CameraFunctionality", "Error: No playerName in intent extra.");
            Toast.makeText(this, "Unexpected Error. Try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
        openCameraButton = findViewById(R.id.btn_Camera);
        Button clearBtn = findViewById(R.id.clearBtn);
        pictureImageView = findViewById(R.id.cameraImageView);
        itemOneImageView = findViewById(R.id.itemOne);
        itemTwoImageView = findViewById(R.id.itemTwo);
        itemThreeImageView = findViewById(R.id.itemThree);
        itemFourImageView = findViewById(R.id.itemFour);


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
        clearBtn.setOnClickListener(view -> clearFound());
        itemOneImageView.setOnClickListener(this::markSelected);
        itemTwoImageView.setOnClickListener(this::markSelected);
        itemThreeImageView.setOnClickListener(this::markSelected);
        itemFourImageView.setOnClickListener(this::markSelected);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void markSelected(View v) {
        ImageView imageView = (ImageView) v;
        if(imageView == selectedImageView) {
            selectedImageView.setForeground(null);
            selectedImageView = null;
        }
        else {
            if(selectedImageView != null) {
                selectedImageView.setForeground(null);
            }
            selectedImageView = (ImageView) v;
            selectedImageView.setForeground(getResources().getDrawable(R.drawable.sm_category_overlay));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void foundItem() {
        if(selectedImageView == null) {
            Toast.makeText(this, "Please select an item first", Toast.LENGTH_LONG).show();
            return;
        }
        boolean[] playerFound = PlayerSelect.itemsFound.get(playerName);
        assert playerFound != null;
        int id = selectedImageView.getId();

        if (id == R.id.itemOne) {
            playerFound[0] = true;
            itemOneImageView.setEnabled(false);
            itemOneImageView.setBackgroundResource(R.drawable.red_camera_item_border);
        } else if (id == R.id.itemTwo) {
            playerFound[1] = true;
            itemTwoImageView.setEnabled(false);
            itemTwoImageView.setBackgroundResource(R.drawable.red_camera_item_border);
        } else if (id == R.id.itemThree) {
            playerFound[2] = true;
            itemThreeImageView.setEnabled(false);
            itemThreeImageView.setBackgroundResource(R.drawable.red_camera_item_border);
        } else if (id == R.id.itemFour) {
            playerFound[3] = true;
            itemFourImageView.setEnabled(false);
            itemFourImageView.setBackgroundResource(R.drawable.red_camera_item_border);
        }

        if(playerFound[0] && playerFound[1] &&  playerFound[2] && playerFound[3]) {
            openCameraButton.setEnabled(false);
        }
        PlayerSelect.itemsFound.put(playerName, playerFound);

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
        Toast.makeText(this, "Picture Saved to Pictures/Find and Play Pictures", Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Is this the correct item?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int id) {
                foundItem();
            }
        });
        builder.setNegativeButton("No", (dialog, id) -> pictureImageView.setImageDrawable(null));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clearFound() { // Function to clear image and hide the clear image button
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to clear all finds?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int id) {
                boolean[] noneFound = new boolean[] {false, false, false, false};
                PlayerSelect.itemsFound.put(playerName, noneFound);
                itemOneImageView.setBackgroundResource(R.drawable.green_camera_item_border);
                itemTwoImageView.setBackgroundResource(R.drawable.green_camera_item_border);
                itemThreeImageView.setBackgroundResource(R.drawable.green_camera_item_border);
                itemFourImageView.setBackgroundResource(R.drawable.green_camera_item_border);
                itemOneImageView.setEnabled(true);
                itemTwoImageView.setEnabled(true);
                itemThreeImageView.setEnabled(true);
                itemFourImageView.setEnabled(true);
                openCameraButton.setEnabled(true);
            }
        });
        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    // function to create a folder location on the device and save the picture with the current date and time
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Uri createPicture(){
        Uri uri;
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String pictureName = format.format(date);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setItemImages(){
        final int[] counter = {0};
        String url;
        boolean[] found = PlayerSelect.itemsFound.get(playerName);
        assert found != null;
        for(PlayItem ignored : Game.inGameItems) {
            BiConsumer<Bitmap, Integer> then = (bitmap, integer) -> {
                switch(counter[0]) {
                    case 0:
                        itemOneImageView.setImageBitmap(bitmap);
                        if(found[0]) {
                            itemOneImageView.setBackgroundResource(R.drawable.red_camera_item_border);
                            itemOneImageView.setEnabled(false);
                    }
                        break;
                    case 1:
                        itemTwoImageView.setImageBitmap(bitmap);
                        if(found[1]) {
                            itemTwoImageView.setBackgroundResource(R.drawable.red_camera_item_border);
                            itemTwoImageView.setEnabled(false);
                        }
                        break;
                    case 2:
                        itemThreeImageView.setImageBitmap(bitmap);
                        if(found[2]) {
                            itemThreeImageView.setBackgroundResource(R.drawable.red_camera_item_border);
                            itemThreeImageView.setEnabled(false);
                        }
                        break;
                    case 3:
                        itemFourImageView.setImageBitmap(bitmap);
                        if(found[3]) {
                            itemFourImageView.setBackgroundResource(R.drawable.red_camera_item_border);
                            itemFourImageView.setEnabled(false);
                        }
                        break;
                }
            };

            url = Game.inGameItems.get(counter[0]).getIcon();
            // load image from cache or url into view
            Picasso.get().load(url).into(new Target() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    then.accept(bitmap, counter[0]);
                    counter[0]++;
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    then.accept(null, counter[0]);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
        });

        }
        // url = "https://picsum.photos/300/300?random=2&_=" + java.util.UUID.randomUUID().toString();

    }

}
