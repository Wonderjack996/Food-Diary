package it.fooddiary.ui.search;

import android.content.DialogInterface;
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
import it.fooddiary.ui.search.favourites.FavouriteFragment;
import it.fooddiary.ui.search.recents.RecentFragment;
import it.fooddiary.ui.search.searched.FoodSearchedFragment;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;

public class SearchFragment extends Fragment {

    private ViewPager2 searchViewPager;
    private TabLayout searchTabLayout;
    private TabsStateAdapter tabsAdapter;

    private FoodSearchedFragment foodSearchedFragment = new FoodSearchedFragment();
    private RecentFragment recentFragment = new RecentFragment();

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
                    foodSearchedFragment.onFoodSearched(query);
                    searchView.clearFocus();
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

        addCaloriesDialog = new MaterialAlertDialogBuilder(getActivity());

        FloatingActionButton fab = root.findViewById(R.id.addCalories_floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPicker numberPicker = new NumberPicker(getActivity());
                numberPicker.setMinValue(Constants.MIN_CALORIES_KCAL);
                numberPicker.setMaxValue(Constants.MAX_CALORIES_KCAL);
                numberPicker.setValue(Constants.MID_CALORIES_KCAL);
                addCaloriesDialog.setTitle(R.string.addCalories);
                addCaloriesDialog.setView(numberPicker);
                addCaloriesDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                addCaloriesDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });
                addCaloriesDialog.show();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Fragment> fragmentToShow = new ArrayList<>();
        fragmentToShow.add(foodSearchedFragment);
        fragmentToShow.add(recentFragment);

        searchViewPager = view.findViewById(R.id.searchViewPager2);
        searchTabLayout = view.findViewById(R.id.searchTabLayout);
        tabsAdapter = new TabsStateAdapter(getChildFragmentManager(), getLifecycle(),
                fragmentToShow);

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
                            tab.setText("error");
                    }
                }).attach();
    }

    public void setDisplayedMealType(MealType mealType) {
        foodSearchedFragment.setDisplayedMealType(mealType);
    }
}
