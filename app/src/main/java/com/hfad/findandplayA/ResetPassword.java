package com.hfad.findandplayA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView back_to_sign;
    private EditText email_text;
    private Button confirm_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // User Input
        email_text = (EditText) findViewById(R.id.email_text);
        confirm_btn = (Button) findViewById(R.id.confrim_btn);

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Button click will send the reset link to email
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordReset(); // run the reset method
            }
        });

        // Activity Navigation
        back_to_sign = (TextView) findViewById(R.id.back_sign);
        back_to_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Reset Password method
    private void passwordReset(){
        // Get the text input and store it in string
        String myEmail = email_text.getText().toString().trim();
        // If text is empty it will print error
        if(myEmail.isEmpty()){
            email_text.setError("Please Enter Email");
            email_text.requestFocus();
            return;
        }
        // By using firebase auth we send the email to the firebase.
        mAuth.sendPasswordResetEmail(myEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            // If email exist in firebase it will alert "Email link Sent" else will alert "Enter Again"
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPassword.this, "Email Link Sent", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ResetPassword.this, "Enter Again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}