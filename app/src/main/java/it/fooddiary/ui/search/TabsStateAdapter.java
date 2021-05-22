package it.fooddiary.ui.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabsStateAdapter extends FragmentStateAdapter {

    private final RecentFragment recent;
    private final FavouriteFragment favorite;

    public TabsStateAdapter(FragmentManager fm, Lifecycle lc) {
        super(fm, lc);
        recent = new RecentFragment();
        favorite = new FavouriteFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return recent;
            case 1:
                return favorite;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
