package com.hfad.findandplayA;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PlayerSelect extends AppCompatActivity{

    private Button  constructivePlayBtn, selectBtn;
    private final String TAG  = "PlayerSelect";
    private Spinner spinner;
    LinearLayout playerButtonLayout;
    ScrollView playerButtonScrollView;
    Context context;

    // childGroups
    private ArrayList<String> allChildGroups = new ArrayList<>();
    // children
    private ArrayList<String> allChildren = new ArrayList<>();

    private String selectedGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);
        context = this;
        selectBtn = findViewById(R.id.groupSelectBtn);

        spinner = (Spinner)findViewById(R.id.groupSelectSpinner); // Dropdown menu for groups

        playerButtonLayout = findViewById(R.id.playerButtonLinearLayoutID); // Linear Layout where buttons will be added
        playerButtonScrollView = findViewById(R.id.playerButtonScrollViewLayout); // ScrollView Layout where buttons will be added
        constructivePlayBtn = (Button) findViewById(R.id.constructivePlayRulesBtn);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allChildGroups.add(document.getId());
                            }
                                // setting childGroups to the dropdown
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, allChildGroups);
                                adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item)); // Setting dropdown
                                spinner.setAdapter(adapter);
                        }else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        constructivePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO finish intent to constructive play rules activity
//                Intent constructivePlayIntent = new Intent(PlayerSelect.this, .class);
//                startActivity(constructivePlayIntent);
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGroup = spinner.getSelectedItem().toString();
                createPlayerButtons(selectedGroup);
            }
        });
    }


    // gets the child groups via the Groups class and saves them to the allChildGroups ArrayList
    public void getChildGroups(){
        Groups groups = new Groups();

        allChildGroups = groups.getGroups();

        System.out.print(allChildGroups.toString());
    }

    // Will create and add buttons for each child present in a selected group
    protected void createPlayerButtons(String selectedGroup){
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("Groups")
                .document(selectedGroup)
                .collection("Children")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Query successful: " + selectedGroup);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            allChildren.add(document.getId());
                        }
                        for (int i = 0; i < allChildren.size(); i++) {

                            String childName = allChildren.get(i);

                            final Button playerButton = new Button(context);

                            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(390, 50);
                            buttonParams.setMargins(10, 2, 10, 2);

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
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    //Function to open the camera activity
    public void openCamera(){
        Intent startGameIntent = new Intent(PlayerSelect.this, CameraFunctionality.class);
        startActivity(startGameIntent);
    }
}