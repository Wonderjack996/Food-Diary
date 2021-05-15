package it.fooddiary.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import it.fooddiary.R;

public class SearchFragment extends Fragment {

    private ViewPager2 searchViewPager;
    private TabLayout searchTabLayout;
    private TabsStateAdapter tabsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_search:
                // action
                break;
            default:
                return false;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchViewPager = view.findViewById(R.id.searchViewPager2);
        searchTabLayout = view.findViewById(R.id.searcTabLayout);
        tabsAdapter = new TabsStateAdapter(getChildFragmentManager(), getLifecycle());

        searchViewPager.setAdapter(tabsAdapter);

        new TabLayoutMediator(searchTabLayout, searchViewPager,
                (tab, position) -> {
                    switch(position) {
                        case 0:
                            tab.setText(R.string.searchTab1);
                            break;
                        case 1:
                            tab.setText(R.string.searchTab2);
                            break;
                        default:
                            tab.setText("error");
                    }
                }).attach();
    }
}
