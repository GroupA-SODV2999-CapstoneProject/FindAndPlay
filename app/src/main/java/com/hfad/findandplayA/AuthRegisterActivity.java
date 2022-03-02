package com.hfad.findandplayA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hfad.findandplayA.viewmodels.User;

public class AuthRegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_register);

        // init firebase auth
        auth = FirebaseAuth.getInstance();

        // sign up hint clickable
        TextView signinHint = (TextView) findViewById(R.id.signin_hint);
        signinHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                Intent intent = new Intent(AuthRegisterActivity.this, AuthLoginActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                AuthRegisterActivity.this.startActivity(intent);
            }
        });

        // sign up submit
        Button btn = (Button) findViewById(R.id.register_submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                EditText emailRef = (EditText) findViewById(R.id.register_email);
                EditText passRef = (EditText) findViewById(R.id.register_pass);
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
            }
        });
    }
}
