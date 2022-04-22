package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Group_Create extends AppCompatActivity {

Button addGroupButton, addChildren;
TextView Groupid;
public static String GroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        Groupid = findViewById(R.id.txtGroupName);
        GroupName = Groupid.getText().toString();
        addGroupButton = findViewById(R.id.btnCreateGroup);
        addChildren = findViewById(R.id.btnAddPlayer);
        addChildren.setOnClickListener(View ->{
            Intent intent = new Intent(Group_Create.this, AddPlayers.class);
            intent.putExtra("GroupName", GroupName);
            Group_Create.this.startActivity(intent);
        });
        addGroupButton.setOnClickListener(View ->{
            Groups.AddGroup(GroupName);
        });









    }


}