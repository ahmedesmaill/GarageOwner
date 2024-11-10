package com.homegarage.garageowner.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.homegarage.garageowner.home.Active;
import com.homegarage.garageowner.home.Finish;
import com.homegarage.garageowner.home.Requestes;

public class TabAdepter  extends FragmentPagerAdapter {

    String Title[] = {"Requestes", "Actives","Finished"};

    public TabAdepter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new Requestes();
        else if (position==1)return new Active();
        else return new Finish();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (Title != null && position < Title.length) {
            return Title[position];
        }
        return super.getPageTitle(position);
    }
}
