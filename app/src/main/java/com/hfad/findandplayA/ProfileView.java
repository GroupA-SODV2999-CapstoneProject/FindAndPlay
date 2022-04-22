package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileView extends AppCompatActivity {

    private Button back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        back_btn = findViewById(R.id.back_btn);
    }

    public void openEditProfile(View view) {
        Intent intent = new Intent(ProfileView.this, ProfileEdit.class);
        startActivity(intent);
    }

    public void logoutBtn(View view) {
        Intent intent = new Intent(ProfileView.this, AuthLoginActivity.class);
        startActivity(intent);
    }
}