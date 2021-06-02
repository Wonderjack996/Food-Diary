package it.fooddiary.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.ui.search.recents.RecentFragment;
import it.fooddiary.ui.search.searched.FoodSearchedFragment;
import it.fooddiary.utils.Constants;

public class SearchFragment extends Fragment {

    private ViewPager2 searchViewPager;

    private TabsStateAdapter tabsAdapter;

    private MaterialAlertDialogBuilder addCaloriesDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && query.length() > 1) {
                    query = query.trim();

                    FoodSearchedFragment foodSearchedFragment;

                    Fragment frag = getChildFragmentManager()
                            .findFragmentById((int)tabsAdapter.getItemId(0));
                    if (frag instanceof FoodSearchedFragment)
                        foodSearchedFragment = (FoodSearchedFragment) frag;
                    else
                        return false;

                    foodSearchedFragment.onFoodSearched(query);
                    searchView.clearFocus();
                    searchViewPager.setCurrentItem(0);
                    return true;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        addCaloriesDialog = new MaterialAlertDialogBuilder(requireActivity());

        FloatingActionButton fab = root.findViewById(R.id.addCalories_floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FoodSearchedFragment foodSearchedFragment = new FoodSearchedFragment();
        RecentFragment recentFragment = new RecentFragment();

        TabLayout searchTabLayout;

        List<Fragment> fragmentToShow = new ArrayList<>();
        fragmentToShow.add(foodSearchedFragment);
        fragmentToShow.add(recentFragment);

        searchViewPager = view.findViewById(R.id.searchViewPager2);
        searchTabLayout = view.findViewById(R.id.searchTabLayout);
        tabsAdapter = new TabsStateAdapter(getChildFragmentManager(),
                getLifecycle(), fragmentToShow);

        searchViewPager.setAdapter(tabsAdapter);

        new TabLayoutMediator(searchTabLayout, searchViewPager,
                (tab, position) -> {
                    switch(position) {
                        case 0:
                            tab.setText(R.string.searched);
                            break;
                        case 1:
                            tab.setText(R.string.recents);
                            break;
                        default:
                            tab.setText(R.string.error);
                    }
        }).attach();
    }
}
