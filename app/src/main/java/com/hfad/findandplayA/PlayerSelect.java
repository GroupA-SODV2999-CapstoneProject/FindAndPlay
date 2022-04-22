package com.hfad.findandplayA;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class PlayerSelect extends AppCompatActivity {

    private final String TAG = "PlayerSelect";
    // childGroups
    private final ArrayList<String> allChildGroups = new ArrayList<>();
    // children
    private final ArrayList<String> allChildren = new ArrayList<>();
    LinearLayout playerButtonLayout;
    ScrollView playerButtonScrollView;
    Context context;
    private Spinner spinner;
    private String selectedGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        context = this;
        Button selectBtn = findViewById(R.id.groupSelectBtn);

        spinner = (Spinner) findViewById(R.id.groupSelectSpinner); // Dropdown menu for groups

        playerButtonLayout = findViewById(R.id.playerButtonLinearLayoutID); // Linear Layout where buttons will be added
        playerButtonScrollView = findViewById(R.id.playerButtonScrollViewLayout); // ScrollView Layout where buttons will be added
        Button constructivePlayBtn = (Button) findViewById(R.id.constructivePlayRulesBtn);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Groups")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            allChildGroups.add(document.getId());
                        }
                        // setting childGroups to the dropdown
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, allChildGroups);
                        adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item)); // Setting dropdown
                        spinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(context, "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
                        finish();
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                });

        constructivePlayBtn.setOnClickListener(view -> {
            // TODO finish intent to constructive play rules activity
//                Intent constructivePlayIntent = new Intent(PlayerSelect.this, .class);
//                startActivity(constructivePlayIntent);
        });

        selectBtn.setOnClickListener(v -> {
            selectedGroup = spinner.getSelectedItem().toString();
            createPlayerButtons(selectedGroup);
        });
    }

    // Will create and add buttons for each child present in a selected group
    protected void createPlayerButtons(String selectedGroup) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Groups")
                .document(selectedGroup)
                .collection("Children")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        playerButtonLayout.removeAllViews();
                        Log.d(TAG, "Query successful: " + selectedGroup);
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            allChildren.add(document.getId());
                        }
                        for (int i = 0; i < allChildren.size(); i++) {

                            String childName = allChildren.get(i);

                            final Button playerButton = new Button(context);

                            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            buttonParams.setMargins(100, 0, 100, 10);

                            playerButton.setText(childName);
                            playerButton.setLayoutParams(buttonParams);
                            playerButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkGreen))); // Sets the button to red
                            playerButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                            GradientDrawable roundCorners = new GradientDrawable(); // Rounds the corners of the button to keep style with the other app buttons
                            roundCorners.setCornerRadius(50);
                            playerButton.setBackground(roundCorners);

                            //set button onClick to open the camera intent
                            playerButton.setOnClickListener(view -> openCamera());
                            playerButtonLayout.addView(playerButton); //Creates the new button within the above params
                        }
                    } else {
                        Toast.makeText(context, "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    //Function to open the camera activity
    public void openCamera() {
        Intent startGameIntent = new Intent(PlayerSelect.this, CameraFunctionality.class);
        startActivity(startGameIntent);
    }
}