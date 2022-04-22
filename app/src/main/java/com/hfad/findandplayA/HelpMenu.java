package com.hfad.findandplayA;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HelpMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_menu);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8f),(int)(height*0.6f));
        TabLayout tab = findViewById(R.id.tabs);
        ViewPager2 vp2 = findViewById(R.id.view_pager);

        HelpViewPagerAdapter adapter = new HelpViewPagerAdapter(this);
        vp2.setAdapter(adapter);

        new TabLayoutMediator(tab, vp2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText("Tab "+ (position + 1));
                    }
                }).attach();

    }

}
