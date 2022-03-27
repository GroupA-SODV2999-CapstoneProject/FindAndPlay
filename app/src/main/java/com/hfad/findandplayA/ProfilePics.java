package com.hfad.findandplayA;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilePics extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pics);
        ImageButton set_Profile_One = findViewById(R.id.img_One);
        ImageButton set_Profile_Two = findViewById(R.id.img_Two);
        ImageButton set_Profile_Three = findViewById(R.id.img_Three);
        ImageButton set_Profile_Four = findViewById(R.id.img_Four);
        ImageButton set_Profile_Five = findViewById(R.id.img_Five);
        ImageButton set_Profile_Six = findViewById(R.id.img_Six);

        set_Profile_One.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast setProfilePic = Toast.makeText(getApplicationContext(), "Profile picture set to Kirby", Toast.LENGTH_SHORT);
                setProfilePic.show();
            }
        });
        set_Profile_Two.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast setProfilePic = Toast.makeText(getApplicationContext(), "Profile picture set Ghost", Toast.LENGTH_SHORT);
                setProfilePic.show();
            }
        });
        set_Profile_Three.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast setProfilePic = Toast.makeText(getApplicationContext(), "Profile picture set to Bread", Toast.LENGTH_SHORT);
                setProfilePic.show();
            }
        });
        set_Profile_Four.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast setProfilePic = Toast.makeText(getApplicationContext(), "Profile picture set Froggy", Toast.LENGTH_SHORT);
                setProfilePic.show();
            }
        });
        set_Profile_Five.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast setProfilePic = Toast.makeText(getApplicationContext(), "Profile picture set to Dino", Toast.LENGTH_SHORT);
                setProfilePic.show();
            }
        });
        set_Profile_Six.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast setProfilePic = Toast.makeText(getApplicationContext(), "Profile picture set to Apple", Toast.LENGTH_SHORT);
                setProfilePic.show();
            }
        });
    }

}
