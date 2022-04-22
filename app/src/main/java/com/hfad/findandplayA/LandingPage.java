package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingPage extends FindAndPlay_ParentClass {

    private Button playButton, createGroupButton, picOTWBtn, selectPicOTWBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        playButton = findViewById(R.id.playBtn);
        createGroupButton = findViewById(R.id.createGroupBtn);
        picOTWBtn = findViewById(R.id.pictureOTWBtn);
        selectPicOTWBtn = findViewById(R.id.pickPictureOTWBTN);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent slotMachineIntent = new Intent(LandingPage.this, SlotMachine.class);
                startActivity(slotMachineIntent);

            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createGroupIntent = new Intent(LandingPage.this, Group_Create.class);
                startActivity(createGroupIntent);
            }
        });

        picOTWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent picOTWIntent = new Intent(LandingPage.this, GetPictureOfTheWeek.class);
                startActivity(picOTWIntent);
            }
        });

        selectPicOTWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent postPicOTWIntent = new Intent(LandingPage.this, PostPicture.class);
                startActivity(postPicOTWIntent);
            }
        });

        init_BurgerMenuButton();
    }
}