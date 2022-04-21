package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AddPlayers extends AppCompatActivity {

    Button Addplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        Addplayer = findViewById(R.id.btnAdd);
        Intent intent = getIntent();;
        String GroupName = intent.getStringExtra("GroupName");

        Addplayer.setOnClickListener(View ->{

        });


    }
}