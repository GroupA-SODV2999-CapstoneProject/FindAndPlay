package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingPage extends AppCompatActivity {

    private Button playButton, createGroupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        playButton = findViewById(R.id.playBtn);
        createGroupButton = findViewById(R.id.createGroupBtn);

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
    }
}