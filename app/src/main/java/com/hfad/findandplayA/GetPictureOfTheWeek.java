package com.hfad.findandplayA;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GetPictureOfTheWeek extends AppCompatActivity {
    ImageView imgView;
    Button refreshBtn;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_picture_of_the_week);
        context = this;
        imgView = findViewById(R.id.pic_of_week);
        imgView.setVisibility(View.INVISIBLE);
        PictureIO.getPicOfWeekFromDb(imgView);
        refreshBtn  = findViewById(R.id.refresh_btn);
        refreshBtn.setOnClickListener(v -> PictureIO.getPicOfWeekFromDb(imgView));
    }
}