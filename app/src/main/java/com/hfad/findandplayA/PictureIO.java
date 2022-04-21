package com.hfad.findandplayA;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

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


}
