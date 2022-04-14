package com.hfad.findandplayA;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Picture_approval extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_picture_approval);
        Button btn_approved = findViewById(R.id.btn_Approve);
        Button btn_declined = findViewById(R.id.btn_Decline);

        btn_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast msg_approved = Toast.makeText(getApplicationContext(), "Picture approve", Toast.LENGTH_SHORT);
                msg_approved.show();

                setContentView(R.layout.activity_auth_pic_check);

            }
        });
        btn_declined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast msg_declined = Toast.makeText(getApplicationContext(), "Picture is declined. Good luck next time", Toast.LENGTH_SHORT);
                msg_declined.show();

                setContentView(R.layout.activity_auth_pic_check);

            }
        });

    }

}
