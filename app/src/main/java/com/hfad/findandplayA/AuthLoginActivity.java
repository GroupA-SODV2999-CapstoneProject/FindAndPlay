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

public class AuthLoginActivity extends AppCompatActivity {
	private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        // init firebase auth
		auth = FirebaseAuth.getInstance();

		// sign up hint clickable
		TextView signupHint = (TextView) findViewById(R.id.signup_hint);
		signupHint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View ref) {
				Intent intent = new Intent(AuthLoginActivity.this, AuthRegisterActivity.class);
				intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
				AuthLoginActivity.this.startActivity(intent);
			}
		});

		// login submit
		Button btn = (Button) findViewById(R.id.login_submit);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View ref) {
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

				auth.signInWithEmailAndPassword(email, password)
					.addOnCompleteListener(AuthLoginActivity.this, new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (task.isSuccessful()) {
								Log.d("AUTH", "signInWithEmail:success");
								FirebaseUser user = auth.getCurrentUser();

								Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
								intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
								AuthLoginActivity.this.startActivity(intent);
							} else {
								Log.w("AUTH", "signInWithEmail:failure", task.getException());
								Toast.makeText(AuthLoginActivity.this, "Error: invalid credentials.",
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			}
		});
	}
}
