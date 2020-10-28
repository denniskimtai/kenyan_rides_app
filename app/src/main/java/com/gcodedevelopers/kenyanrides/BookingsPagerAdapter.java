package com.gcodedevelopers.kenyanrides;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class BookingsPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public BookingsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentCustomerBookings fragmentCustomerBookings = new FragmentCustomerBookings();
                return fragmentCustomerBookings;

            case 1:
                MybookingsFragment mybookingsFragment = new MybookingsFragment();
                return mybookingsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
