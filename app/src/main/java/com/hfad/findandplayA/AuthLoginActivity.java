package com.hfad.findandplayA;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.findandplayA.viewmodels.Auth;

public class AuthLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        // sign up hint clickable
        TextView signupHint = (TextView) findViewById(R.id.signup_hint);
        signupHint.setOnClickListener(ref -> {
            Intent intent = new Intent(AuthLoginActivity.this, AuthRegisterActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            AuthLoginActivity.this.startActivity(intent);
        });

        // login submit
        Button btn = (Button) findViewById(R.id.login_submit);
        btn.setOnClickListener(ref -> {
            EditText emailRef = (EditText) findViewById(R.id.login_email);
            EditText passRef = (EditText) findViewById(R.id.login_pass);
            String email = emailRef.getText().toString().trim();
            String password = passRef.getText().toString();

            if ( email.length() <= 6 ) {
                emailRef.requestFocus();
                return;
            }

            if ( password.length() <= 6 ) {
                passRef.requestFocus();
                return;
            }

            Auth.login(email, password, ok ->
            {
                if ( ok ) {
                    Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    AuthLoginActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(AuthLoginActivity.this, "Error: invalid credentials.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
