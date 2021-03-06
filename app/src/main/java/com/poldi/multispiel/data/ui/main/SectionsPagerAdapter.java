package com.poldi.multispiel.data.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.poldi.multispiel.R;
import com.poldi.multispiel.data.ui.main.ui.friendsfragmentmodel.FriendsFragmentModelFragment;
import com.poldi.multispiel.data.ui.main.ui.gamefragmentmodel.GameFragmentModelFragment;
import com.poldi.multispiel.data.ui.main.ui.gamefragmentmodel.GameFragmentModelViewModel;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final GameFragmentModelFragment ownGames = new GameFragmentModelFragment();
    private static final FriendsFragmentModelFragment ownFriends = new FriendsFragmentModelFragment();
    private static final RanglisteFragmentModel ownRanglist = new RanglisteFragmentModel();

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return ownGames;

            case 1:
                return ownFriends;

            case 2:
                return ownRanglist;
        }
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}