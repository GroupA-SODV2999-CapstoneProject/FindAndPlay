package com.hfad.findandplayA;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Picture_check extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_pic_check);
        Button btn_approve_all = findViewById(R.id.btn_approve_all);
        Button btn_1 = findViewById(R.id.btn_sudmission_1);
        Button btn_2 = findViewById(R.id.btn_sudmission_2);


        btn_approve_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast approveAll = Toast.makeText(getApplicationContext(), "All submissions are approved", Toast.LENGTH_SHORT);
                approveAll.show();
            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_auth_picture_approval);
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_auth_picture_approval);
            }
        });
    }


}
