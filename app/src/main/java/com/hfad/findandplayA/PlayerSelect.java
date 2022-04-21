package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.findandplayA.Groups;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import java.util.ArrayList;


public class PlayerSelect extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button  constructivePlayBtn;
    private Spinner spinner;
    LinearLayout playerButtonLayout;
    ScrollView playerButtonScrollView;

    // childGroups
    private ArrayList<String> allChildGroups = new ArrayList<>();
    // children
    private ArrayList<String> allChildren = new ArrayList<>();

    private String selectedGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);

        spinner = (Spinner)findViewById(R.id.groupSelectSpinner); // Dropdown menu for groups
        spinner.setOnItemSelectedListener(this);

        playerButtonLayout = findViewById(R.id.playerButtonLinearLayoutID); // Linear Layout where buttons will be added
        playerButtonScrollView = findViewById(R.id.playerButtonScrollViewLayout); // ScrollView Layout where buttons will be added
        constructivePlayBtn = (Button) findViewById(R.id.constructivePlayRulesBtn);

        getChildGroups();

        // setting childGroups to the dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allChildGroups);
        adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item)); // Setting dropdown
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

        constructivePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO finish intent to constructive play rules activity
//                Intent constructivePlayIntent = new Intent(PlayerSelect.this, .class);
//                startActivity(constructivePlayIntent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.print("Testing");
        selectedGroup = spinner.getSelectedItem().toString(); // Getting the selected group
        createPlayerButtons(selectedGroup);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // gets the child groups via the Groups class and saves them to the allChildGroups ArrayList
    public void getChildGroups(){
        Groups groups = new Groups();

        allChildGroups = groups.getGroups();
        System.out.print(allChildGroups.toString());
    }

    // Will create and add buttons for each child present in a selected group
    protected void createPlayerButtons(String selectedGroup){
        Groups groups = new Groups();

        allChildren = groups.getChildren();
        System.out.print(allChildren);

        for (int i = 0; i <= allChildren.size(); i++){

            String childName = allChildren.get(i);

            final Button playerButton = new Button(this);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(390,50);
            buttonParams.setMargins(10,2,10,2);

            playerButton.setText(childName);
            playerButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkGreen))); // Sets the button to red
            playerButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            GradientDrawable roundCorners = new GradientDrawable(); // Rounds the corners of the button to keep style with the other app buttons
            roundCorners.setCornerRadius(50);
            playerButton.setBackground(roundCorners);

            //set button onClick to open the camera intent
            playerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openCamera();
                }
            });
            playerButtonLayout.addView(playerButton); //Creates the new button within the above params
        }
    }

    //Function to open the camera activity
    public void openCamera(){
        Intent startGameIntent = new Intent(PlayerSelect.this, CameraFunctionality.class);
        startActivity(startGameIntent);
    }

}