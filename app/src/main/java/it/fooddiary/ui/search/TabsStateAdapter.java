package it.fooddiary.ui.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import it.fooddiary.ui.search.favourites.FavouriteFragment;
import it.fooddiary.ui.search.recents.RecentFragment;
import it.fooddiary.ui.search.searched.FoodSearchedFragment;

public class TabsStateAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList;

    public TabsStateAdapter(FragmentManager fm, Lifecycle lc, List<Fragment> fragments) {
        super(fm, lc);
        this.fragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
