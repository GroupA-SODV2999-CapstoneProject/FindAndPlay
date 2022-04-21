package com.hfad.findandplayA;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PictureIO {
    static String defaultConfigDir = "/Pictures";

    static void downloadImage(String ImageName, String FirebaseStorageLocation, String ImageLocation) {

        //Initiallize an instance of the firebase storage class from the library to the FirebaseStorageLocation
        FirebaseStorage storage = FirebaseStorage.getInstance(FirebaseStorageLocation);
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child(ImageLocation); //Get the file at the ImageLocation path inside the FireBase Storage

        try {
            //Create a temporary local file
            File localFile = File.createTempFile(ImageName, ".jpg");
            String Path = null;

            //Set the temp file to the file downloaded from database
            spaceRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                System.out.println("------------------------------------------------------");
                System.out.println(Uri.fromFile(localFile));
                return ;
            }).addOnFailureListener(e -> {
                e.printStackTrace();
            });
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    static Bitmap retrieveImage(String imageLocation){

        System.out.println(imageLocation);
        String imgPath = imageLocation;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        return bitmap;
    }

    static String imageLocationList()[] {
        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderDir = storageDir + defaultConfigDir;
        File folder = new File(folderDir);

        String[] list = new String[folder.listFiles().length];
        for (int i = 0; i < folder.listFiles().length; i++){
            list[i] = folder.listFiles()[i].toString();
        }
        return list;
    }

    static String imageLocationList(String configDir)[] {
        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderDir = storageDir + ((!configDir.equals("") && configDir != null) ? configDir : defaultConfigDir);
        File folder = new File(folderDir);

        String[] list = new String[folder.listFiles().length];
        for (int i = 0; i < folder.listFiles().length; i++){
            list[i] = folder.listFiles()[i].toString();
        }
        return list;
    }

    /**
     * Adds a remote storage reference to the user chosen "Picture of the Week" to Firestore
     *
     * @param localPathToPic - A string reference to the location of the picture in local storage
     */
    public static void addPicOfWeekToDb(String localPathToPic) {
        final String TAG = "addPicOfWeekToDb";
        //Send to picture to remote storage
        String refUrl = addPicOfWeekToStorage(localPathToPic);
        //Handle error
        if(refUrl.isEmpty()) {
            Log.e(TAG, "Error: refUrl to picOfWeek empty. Abandoning save...");
            return;
        }
        //Create Firestore data map
        Map<String, Object> picData = new HashMap<>();
        picData.put("refUrl", refUrl);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Get the current user
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        //Attempt to add document to Firestore
        db.collection("PictureOfTheWeek")
                .document(userId)
                .set(picData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }



    private static String addPicOfWeekToStorage(String localPathToPicture) {
        final String TAG = "addPicOfWeekToStorage";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(localPathToPicture));
        StorageReference ref = storageRef.child("pictureOfTheWeek/" + file.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(file);
        final String[] storageUrl = {""};
        uploadTask.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure (@NonNull Exception exception){
                                                        Log.e(TAG, "Error sending image to storage: " + exception);
                                                    }
                                                }).

                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess (UploadTask.TaskSnapshot taskSnapshot){
                        // Continue with the task to get the download URL
                        storageUrl[0] = String.valueOf(ref.getDownloadUrl());
                        Log.d(TAG, "Success: img added at " + storageUrl[0]);
                    }
                });
        return storageUrl[0];
    }

    public static void getPicOfWeekFromDb(ImageView imageView, Context context) {
        final String TAG = "addPicOfWeekToStorage";
        String refUrl = getPicOfWeekRefFromFirestore();

        //Handle error retrieving url
        if (refUrl.isEmpty()) {
            Log.e(TAG, "Error: refUrl to picOfWeek empty. Abandoning retrieval...");
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Glide.with(context)
                .load(storageReference)
                .into(imageView);
    }

    private static String getPicOfWeekRefFromFirestore() {
        final String TAG = "getPicOfWeekFromFire";
        final String[] url = {""};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();

        db.collection("PictureOfTheWeek")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        url[0] = String.valueOf(task.getResult());
                        Log.d(TAG, "Success: " + url[0]);
                    } else {
                        Log.e(TAG, "Error retrieving saved ciphers. Exception: ", task.getException());
                    }
                });
        return url[0];
    }
}
