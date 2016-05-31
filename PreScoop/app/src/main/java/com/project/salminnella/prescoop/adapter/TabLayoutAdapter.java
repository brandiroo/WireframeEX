package com.project.salminnella.prescoop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.salminnella.prescoop.fragment.TabLayoutFragment;
import com.project.salminnella.prescoop.model.PreSchool;

/**
 * Fragment Tablayout Adapter. Uses an integer page count to generate the titles for each tab.
 */
public class TabLayoutAdapter extends FragmentPagerAdapter {
    // region Member Variables
    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] { "All Visits", "Citations", "Inspections", "Complaints", "Other Visits", "Reports" };
    private PreSchool preSchool;
    // endregion Member Variables

    // constructor
    public TabLayoutAdapter(FragmentManager fm, PreSchool preschool) {
        super(fm);
        this.preSchool = preschool;
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
