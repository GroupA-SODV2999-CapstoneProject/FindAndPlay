package com.hfad.findandplayA;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Group_Create extends AppCompatActivity {

    Button addPlayerButton;
    EditText groupNameBox, childNameBox;
    public static String GroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        groupNameBox = findViewById(R.id.txtGroupName);
        childNameBox = findViewById(R.id.childNameBox);
        String groupName = groupNameBox.getText().toString();
        String childName = childNameBox.getText().toString();
        addPlayerButton = findViewById(R.id.btnAdd);
        addPlayerButton.setOnClickListener(View ->{
            //Create Firestore data map
            Map<String, Object> childData = new HashMap<>();
            childData.put("Children", childName );
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            //Attempt to add document to Firestore
            db.collection("Groups")
                    .document(groupName)
                    .set(childData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Child added to group.", Toast.LENGTH_SHORT).show();
                        Log.d("Group_Create", "Child added to group: " + groupName);
                    })

                    .addOnFailureListener(e -> Log.w("Group_Create", "Error adding document to Firestore", e));
        });
    }
}