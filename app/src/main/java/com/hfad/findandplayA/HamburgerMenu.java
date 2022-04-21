package com.hfad.findandplayA;

import android.os.Bundle;
import android.widget.Button;

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
        init_returnButton();
    }

    public void init_cameraButton() {
        //Set the onclick listeners as these are fragments
        Button camera_button = findViewById(R.id.cameraButton);
        camera_button.setOnClickListener(view -> {
            //Call the nav class
            UpdateView(CameraFunctionality.class);
        });
    }
    public void init_slotsButton() {
        //Set the onclick listeners
        Button slots_button = findViewById(R.id.slotsButton);
        slots_button.setOnClickListener(view -> {
            //Call the nav class
            UpdateView(SlotMachine.class);
        });
    }
    public void init_profileButton() {
        //Set the onclick listeners
        Button profile_button = findViewById(R.id.profileButton);
        profile_button.setOnClickListener(view -> {
            //Call the nav class
            UpdateView(ProfileView.class);
        });
    }
    public void init_adminButton() {
        //Set the onclick listener
        Button admin_button = findViewById(R.id.adminButton);
        admin_button.setOnClickListener(view -> {
            //Call the nav class
            //????
        });
    }
    public void init_returnButton() {
        //Set the onclick listener
        Button return_button = findViewById(R.id.returnButton);
        return_button.setOnClickListener(view -> {
            //Calls to the stack to finish this activity
            finish();
        });
    }
    public void init_helpButton() {
        //Set the onclick listener
        Button help_button = findViewById(R.id.helpButton);
        help_button.setOnClickListener(view -> {
            //Make the call here to the help activity

        });
    }
}
