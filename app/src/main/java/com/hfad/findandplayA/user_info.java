package com.hfad.findandplayA;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class user_info extends AppCompatActivity {

    TextView editTextName, editTextAge, editTextNickname, editTextGroup;
    Button btnCreateAccount, btnUpdateUserInfo;
    DatabaseReference reference;
    User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        editTextName = findViewById(R.id.editTextName);
        editTextNickname = findViewById(R.id.editTextNickname);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGroup = findViewById(R.id.editTextGroup);

       // reference = FirebaseDatabase.getInstance().getReference("Children");       //probably should change the path here

        user = new User();

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        btnUpdateUserInfo = findViewById(R.id.btnUpdateUserInfo);
        btnUpdateUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    public void addUser() {
        int Age = Integer.parseInt(editTextAge.getText().toString().trim());
        String Name = editTextName.getText().toString().trim();
        String Nickname = editTextNickname.getText().toString().trim();
        String Group = editTextGroup.getText().toString().trim();

      //  user.setName(Name);
        user.setNickname(Nickname);
        user.setAge(Age);
       // user.setGroup(Group);
        if(user.getAge().equals(3)){
            db.collection("groups").document("groups1").collection("children").add(user);
        }
        if(user.getAge().equals(4)){
            db.collection("groups").document("groups2").collection("children").add(user);
        }
        if(user.getAge().equals(5)){
            db.collection("groups").document("groups3").collection("children").add(user);
        }
        //reference.push().setValue(user);
        Toast.makeText(this, "data inserted successfully", Toast.LENGTH_SHORT).show();
    }

    public void updateUser() {
        if ( isNameChanged() || isNicknameChanged() || isGroupChanged() || isAgeChanged()) {
            Toast.makeText(this, "Data has been updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data has NOT been updated!", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isNameChanged() {
        if ( ! user.Name.equals(editTextName.getText().toString().trim())){
            reference.child(user.Name).child("Name").setValue(editTextName.getText().toString().trim());   //the path for the user name in the firebase should be exactly "Name"
            return true;
        } else {
            return false;
        }
    }
    private boolean isGroupChanged() {
        if ( ! user.Group.equals(editTextGroup.getText().toString().trim())){
            reference.child(user.Group).child("Group").setValue(editTextGroup.getText().toString().trim());   //the path for the user group in the firebase should be exactly "Group"
            return true;
        } else {
            return false;
        }
    }
    private boolean isNicknameChanged() {
        if ( ! user.Nickname.equals(editTextNickname.getText().toString().trim())){
            reference.child(user.Nickname).child("Nickname").setValue(editTextNickname.getText().toString().trim());   //the path for the user nickname in the firebase should be exactly "Nickname"
            return true;
        } else {
            return false;
        }
    }
    private boolean isAgeChanged() {
        if ( ! user.Age.equals(editTextAge.getText().toString().trim())){
            reference.child(user.Age.toString()).child("Age").setValue(editTextAge.getText().toString().trim());   //the path for the user age in the firebase should be exactly "Age"
            return true;
        } else {
            return false;
        }
    }
}