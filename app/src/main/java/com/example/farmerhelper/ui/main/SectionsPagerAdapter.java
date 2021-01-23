package com.example.farmerhelper.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.farmerhelper.R;
import com.example.farmerhelper.ui.main.fragments.BaseFragment;
import com.example.farmerhelper.ui.main.fragments.FlowerFragment;
import com.example.farmerhelper.ui.main.fragments.FruitFragment;
import com.example.farmerhelper.ui.main.fragments.TreeFragment;
import com.example.farmerhelper.ui.main.fragments.VegetableFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_tree, R.string.tab_flower, R.string.tab_vegetable, R.string.tab_fruit};
    private final Context context;
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 1:return new FlowerFragment();
            case 2:return new VegetableFragment();
            case 3:return new FruitFragment();
            default:return new TreeFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}