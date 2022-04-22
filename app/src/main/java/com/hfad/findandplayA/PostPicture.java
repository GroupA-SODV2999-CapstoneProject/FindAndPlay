package com.hfad.findandplayA;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostPicture extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseUser user;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    // view for image view
    private ImageView imageView;
    // Uri indicates, where the image will be picked from
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_potw);
        user = mAuth.getCurrentUser();


        // initialise views
        Button btnSelect = findViewById(R.id.btnChoose);
        Button btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(v -> SelectImage());

        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(v -> uploadImage());

    }

    // Select Image method
    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {
            addPicOfWeekToDb(filePath);
        }
    }


    /**
     * Adds a remote storage reference to the user chosen "Picture of the Week" to Firestore
     *
     * @param file - A uri reference to the location of the picture in local storage
     */

    private void addPicOfWeekToDb(Uri file) {
        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");

        progressDialog.show();
        final String TAG = "addPicOfWeekToStorage";
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://findandplay-acad0.appspot.com");
        StorageReference storageRef = storage.getReference();
        final String[] storageUrl = {""};
        StorageReference ref = storageRef.child("PictureOfTheWeek/" + file.getLastPathSegment());
        ref.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                    // Continue with the task to get the download URL
                    ref.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                        String url = String.valueOf(downloadUrl);
                        if (url.isEmpty()) {
                            Log.e(TAG, "Error: url is empty. Abandoning Firestore save...");
                        } else {
                            //Create Firestore data map
                            Map<String, Object> picData = new HashMap<>();
                            picData.put("refUrl", url);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            //Get the current user
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            assert user != null;
                            String userId = user.getUid();
                            //Attempt to add document to Firestore
                            db.collection("PictureOfTheWeek")
                                    .document(userId)
                                    .set(picData)
                                    .addOnSuccessListener(aVoid -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(PostPicture.this, "Success!", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "Picture of the week successfully saved to Firestore!");
                                    })

                                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document to Firestore", e));
                        }
                    });
                    Log.d(TAG, "Success: img added at " + storageUrl[0]);
                }).addOnProgressListener(
                taskSnapshot -> {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int) progress + "%");
                })
                .addOnFailureListener(exception -> {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast
                            .makeText(getApplicationContext(),
                                    "Failed " + exception.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                    Log.e(TAG, "Error sending image to storage: " + exception);
                });
    }
}
