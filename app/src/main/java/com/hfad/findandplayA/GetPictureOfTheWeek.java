package com.hfad.findandplayA;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class GetPictureOfTheWeek extends AppCompatActivity {
    ImageView imgView;
    private final String TAG = "GetPicOfWeek";
    Button refreshBtn;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_picture_of_the_week);
        context = this;
        imgView = findViewById(R.id.pic_of_week);
        imgView.setVisibility(View.INVISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        db.collection("PictureOfTheWeek")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult().getData() == null) {
                            return;
                        }
                        String s_url = String.valueOf(task.getResult().getData().get("refUrl"));
                        //Handle error retrieving url
                        if (s_url.isEmpty()) {
                            Log.e(TAG, "Error: refUrl to picOfWeek empty. Abandoning retrieval...");
                        } else {
                            Log.d(TAG, "Successfully retrieved url:  " + s_url);
                            Picasso.get().load(s_url).fit().into(imgView);
                            imgView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.e(TAG, "Error: Couldn't retrieve url from Firestore.");
                    }
                });
        refreshBtn  = findViewById(R.id.refresh_btn);
        refreshBtn.setOnClickListener(v -> PictureIO.getPicOfWeekFromDb(imgView));
    }
}