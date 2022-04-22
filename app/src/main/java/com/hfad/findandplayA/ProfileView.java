package com.hfad.findandplayA;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileView extends AppCompatActivity {

    private Button back_btn;
    private ImageView profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        back_btn = findViewById(R.id.back_btn);
        profile_pic = findViewById(R.id.profile_pic);
        back_btn.setOnClickListener(v -> finish());
        profilePic();
    }

    private void profilePic() {
        try{
            final Intent intent = getIntent();
            if (intent.getStringExtra("kid").equals("kid1")){
                profile_pic.setBackgroundResource(R.drawable.kid1);
            }else if (intent.getStringExtra("kid").equals("kid2")){
                profile_pic.setBackgroundResource(R.drawable.kid2);
            }else if (intent.getStringExtra("kid").equals("kid3")){
                profile_pic.setBackgroundResource(R.drawable.kid3);
            }else if (intent.getStringExtra("kid").equals("kid4")){
                profile_pic.setBackgroundResource(R.drawable.kid4);
            }else if (intent.getStringExtra("kid").equals("kid5")){
                profile_pic.setBackgroundResource(R.drawable.kid5);
            }else if (intent.getStringExtra("kid").equals("kid6")){
                profile_pic.setBackgroundResource(R.drawable.kid6);
            }
        }catch (Exception e){
            Log.d("Err", "Oops");
        }
    }

    public void openEditProfile(View view) {
        Intent intent = new Intent(ProfileView.this, ProfilePics.class);
        startActivity(intent);
    }

    public void logoutBtn(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileView.this, AuthLoginActivity.class);
        startActivity(intent);
    }
}