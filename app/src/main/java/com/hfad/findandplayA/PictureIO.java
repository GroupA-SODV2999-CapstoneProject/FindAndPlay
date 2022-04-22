package com.hfad.findandplayA;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class PictureIO {
    public static void getPicOfWeekFromDb(ImageView imageView) {
        final String TAG = "addPicOfWeekToStorage";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();

        db.collection("PictureOfTheWeek")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String s_url = String.valueOf(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getData()).get("refUrl"));
                        //Handle error retrieving url
                        if (s_url.isEmpty()) {
                            Log.e(TAG, "Error: refUrl to picOfWeek empty. Abandoning retrieval...");
                        } else {
                            Log.d(TAG, "Successfully retrieved url:  " + s_url);
                            Picasso.get().load(s_url).fit().into(imageView);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.e(TAG, "Error: Couldn't retrieve url from Firestore.");
                    }
                });
    }
}