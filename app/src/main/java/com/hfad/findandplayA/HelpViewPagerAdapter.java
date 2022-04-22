package com.hfad.findandplayA;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HelpViewPagerAdapter extends FragmentStateAdapter
{
    public HelpViewPagerAdapter(@NonNull FragmentActivity fa) { super (fa);}

    @NonNull
    @Override
    public Fragment createFragment(int Pos) {
        switch (Pos)
        {
            case 1: return new HelpPage1();
            case 2: return new HelpPage2();
            default: return new HelpPage0();
        }
    }
    @Override
    public int getItemCount()
    {
        return 3;
    }
}

