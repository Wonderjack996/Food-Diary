package it.fooddiary.ui.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TabsStateAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList;

    public TabsStateAdapter(@NotNull @NonNull FragmentManager fm,
                            @NotNull @NonNull Lifecycle lc,
                            @NotNull @NonNull List<Fragment> fragments) {
        super(fm, lc);
        this.fragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragmentList != null) {
            if (position >= 0 && position < fragmentList.size())
                return fragmentList.get(position);
            else
                return fragmentList.get(0);
        }
        else
            return new SearchFragment();
    }

    @Override
    public int getItemCount() {
        if (fragmentList != null)
            return fragmentList.size();
        return 0;
    }
}