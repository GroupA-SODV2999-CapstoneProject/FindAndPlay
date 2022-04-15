package com.hfad.findandplayA;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HamburgerMenu extends FindAndPlay_ParentClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamburger_menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        init_cameraButton();
        init_profileButton();
        init_slotsButton();
        init_adminButton();
    }

    public void init_cameraButton() {
        //Set the onclick listeners as these are fragments
        Button camera_button = (Button) findViewById(R.id.cameraButton);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Check", "called");
            }
        });
    }

    public void init_slotsButton() {
        //Set the onclick listeners
        Button slots_button = (Button) findViewById(R.id.slotsButton);
        slots_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call the nav class
            }
        });
    }

    public void init_profileButton() {
        //Set the onclick listeners
        Button profile_button = (Button) findViewById(R.id.profileButton);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call the nav class
            }
        });
    }

    public void init_adminButton() {
        //Set the onclick listener
        Button admin_button = (Button) findViewById(R.id.adminButton);
        admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call the nav class
            }
        });
    }
}
