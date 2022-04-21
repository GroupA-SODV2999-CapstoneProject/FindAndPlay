package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class AddPlayers extends AppCompatActivity {

    Button Addplayer;
    HashMap<String, String> playersinfo = new HashMap<String, String>();
    TextView playerName;
    String Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        Addplayer = findViewById(R.id.btnAdd);
        playerName = findViewById(R.id.editTextPersonName);
        Name = playerName.getText().toString();
        Intent intent = getIntent();;
        String GroupName = intent.getStringExtra("GroupName");

        Addplayer.setOnClickListener(View ->{

            Groups.AddChild(GroupName,Name,playersinfo);
        });


    }
}