package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.salminnella.prescoop.fragment.TabLayoutFragment;
import com.project.salminnella.prescoop.model.PreSchool;

/**
 * Created by anthony on 5/9/16.
 */
public class TabLayoutAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] { "All Visits", "Citations", "Inspections", "Complaints", "Other Visits", "Reports" };
    private Context context;
    PreSchool preSchool;

    public TabLayoutAdapter(FragmentManager fm, Context context, PreSchool preSchool) {
        super(fm);
        this.context = context;
        this.preSchool = preSchool;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return TabLayoutFragment.newInstance(position + 1, preSchool);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
