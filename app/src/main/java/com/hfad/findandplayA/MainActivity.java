package com.hfad.findandplayA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
	private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// init firebase auth
		auth = FirebaseAuth.getInstance();

		// get signed in user if any
		FirebaseUser currentUser = auth.getCurrentUser();

		if(currentUser == null){
			// load login activity
			Intent intent = new Intent(MainActivity.this, AuthLoginActivity.class);
			intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			MainActivity.this.startActivity(intent);
			finish(); // this is somehow needed to prevent backbtn showing the hello-world string
		} else { // logged in
			RelativeLayout spinner = (RelativeLayout) findViewById(R.id.layout_loading_spinner);
			RelativeLayout main = (RelativeLayout) findViewById(R.id.layout_main_content);
			spinner.setVisibility(spinner.GONE);
			main.setVisibility(spinner.VISIBLE);
			TextView text = (TextView) findViewById(R.id.centered_message);
			text.setText(String.format("Signed in as %s", currentUser.getEmail()));

			// sign out clickable
			TextView signout = (TextView) findViewById(R.id.signout);
			signout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View ref) {
					auth.signOut();
					finish();
					startActivity(MainActivity.this.getIntent());
				}
			});
		}
    }
}
