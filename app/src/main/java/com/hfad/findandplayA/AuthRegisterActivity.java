package com.hfad.findandplayA;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.findandplayA.viewmodels.User;

public class AuthRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_register);

        // sign up hint clickable
        TextView signinHint = (TextView) findViewById(R.id.signin_hint);
        signinHint.setOnClickListener(ref -> {
            Intent intent = new Intent(AuthRegisterActivity.this, AuthLoginActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            AuthRegisterActivity.this.startActivity(intent);
        });

        // sign up submit
        Button btn = (Button) findViewById(R.id.register_submit);
        btn.setOnClickListener(ref -> {
            EditText emailRef = (EditText) findViewById(R.id.register_email);
            EditText passRef = (EditText) findViewById(R.id.register_pass);
            String email = emailRef.getText().toString().trim();
            String password = passRef.getText().toString();

            if ( email.length() <= 6 ) {
                Toast.makeText(AuthRegisterActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                emailRef.requestFocus();
                return;
            }

            if ( password.length() <= 6 ) {
                Toast.makeText(AuthRegisterActivity.this, "Password must be longer than 6 characters.", Toast.LENGTH_SHORT).show();
                passRef.requestFocus();
                return;
            }

            User user = new User(email, password, null, null);
            user.save(saved -> {
                if ( saved ) {
                    Intent intent = new Intent(AuthRegisterActivity.this, MainActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    AuthRegisterActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(AuthRegisterActivity.this, "Error: please try again or contact us.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
