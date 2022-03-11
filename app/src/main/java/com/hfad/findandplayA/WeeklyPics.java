/*
*   @author Alexandr Pokorskiy
*   @para Simple weekly picture page with some code to go along with it to showcase example functionality
*   @version 0.1
*
*     TODO:
*      -ADD databse connectivity so that the weekly image is loaded from the main/designated database
*      -(Optional) Add sounds effects to better excite and stimulate the picture that is presented
* */

package com.hfad.findandplayA;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class WeeklyPics extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_weekly_picture);
        Button imageLoad = findViewById(R.id.btn_load);
        ImageView weeklyImage = findViewById(R.id.img_weekly);
        TextView title = findViewById(R.id.txt_Title);


        imageLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast setProfilePic = Toast.makeText(getApplicationContext(), "Image loaded", Toast.LENGTH_SHORT);
                weeklyImage.setImageResource(R.drawable.apple);
                imageLoad.setEnabled(false);
                title.setVisibility(View.VISIBLE);
                setProfilePic.show();
            }
        });
    }
}
