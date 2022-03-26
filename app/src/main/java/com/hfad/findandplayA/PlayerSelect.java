package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class PlayerSelect extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button cameraBtn; //TODO remove this button code once data is implemented
    private Spinner spinner;
    LinearLayout playerButtonLayout;

    private static final String[] tempGroups = {"Group 1", "Group 2", "Group 3", "Group 4"}; // TODO remove and replace this temp array with actual group data from FireBase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);

        spinner = (Spinner)findViewById(R.id.groupSelectSpinner); // Dropdown menu for groups
        playerButtonLayout = findViewById(R.id.playerButtonLinearLayoutID); // Linear Layout where buttons will be added
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlayerSelect.this, android.R.layout.simple_spinner_item,tempGroups); // TODO change this from tempGroups to actual groups from FireBase
        adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item)); // Setting dropdown
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        cameraBtn = (Button) findViewById(R.id.playerOneBtn); //TODO remove this button code once data is implemented
        cameraBtn.setOnClickListener(new View.OnClickListener() { //TODO remove this button code once data is implemented
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){

        String selection = spinner.getSelectedItem().toString(); // Getting the selected group

        // TODO add the rest of the code to get children from selected group and

        createPlayerButtons(selection);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO set what happens if nothing is selected from the dropdown
    }

    protected void createPlayerButtons(String groupSelection){
        // TODO Will create and add buttons for each child present in a selected group

        for (int i=1;i<=10;i++){ // TODO need to change to iterate collection of children from FireBase

            final Button playerButton = new Button(this);

            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(390,50);
            buttonParams.setMargins(10,2,2,10);
//            playerButton.setLayoutParams(new LinearLayout.LayoutParams(390,50));

            playerButton.setId(i); // TODO Need to change when have access to children data
            playerButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.playerBtnRed))); // Sets the button to red
            playerButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            GradientDrawable roundCorners = new GradientDrawable(); // Rounds the corners of the button to keep style with the other app buttons
            roundCorners.setCornerRadius(50);
            playerButton.setBackground(roundCorners);

            playerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // TODO add any other data that needs to be passed to the camera activity if any at all
                    openCamera();
                }
            });
            playerButtonLayout.addView(playerButton); //Creates the new button within the above params
        }
    }

    protected void playerComplete(){
        // TODO Will change player button color if player has completed all of the required tasks
    }

    //Function to open the camera activity
    public void openCamera(){
        Intent startGameIntent = new Intent(PlayerSelect.this, CameraFunctionality.class);
        startActivity(startGameIntent);
    }
}