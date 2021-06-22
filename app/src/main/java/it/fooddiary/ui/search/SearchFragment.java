package it.fooddiary.ui.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databases.IDatabaseOperation;
import it.fooddiary.models.Food;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.ui.search.recents.RecentFragment;
import it.fooddiary.ui.search.searched.FoodSearchedFragment;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;

public class SearchFragment extends Fragment implements IDatabaseOperation {

    private static final String TAG = "SearchFragment";

    private ViewPager2 searchViewPager;

    private FoodInsertItemAlert alert;

    private FoodViewModel viewModel;

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

                    for( Fragment frag : getChildFragmentManager().getFragments()) {
                        if (frag instanceof FoodSearchedFragment) {
                            ((FoodSearchedFragment)frag).onFoodSearched(query);
                            searchView.clearFocus();
                            searchViewPager.setCurrentItem(0);
                        }
                    }

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

        viewModel = new ViewModelProvider(this,
                new FoodViewModelFactory(requireActivity().getApplication(),
                        new FoodRepository(requireActivity().getApplication())))
                .get(FoodViewModel.class);

        alert = new FoodInsertItemAlert(this);

        FloatingActionButton fab = root.findViewById(R.id.addCalories_floatingButton);
        fab.setOnClickListener(v -> alert.show(getChildFragmentManager(), TAG));

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
        TabsStateAdapter tabsAdapter = new TabsStateAdapter(getChildFragmentManager(),
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

    @SuppressLint("ShowToast")
    @Override
    public void addFoodToMeal(@NonNull @NotNull Food foodToAdd,
                              @NonNull @NotNull MealType mealType) {
        if (foodToAdd.getName() == null || foodToAdd.getName().length() <= 2) {
            Snackbar.make(requireView(), R.string.name_not_valid, Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.addCalories_floatingButton)
                    .show();
        } else {
            Date currDate = viewModel.getCurrentDate();
            viewModel.insertFoodInMeal(foodToAdd, mealType, currDate)
                    .observe(this, integer -> {
                        switch (integer) {
                            case Constants.DATABASE_INSERT_OK:
                                Snackbar.make(requireView(), R.string.added,
                                        Snackbar.LENGTH_SHORT)
                                        .setAction("Undo", v -> removeFoodFromMeal(foodToAdd, mealType))
                                        .setAnchorView(R.id.addCalories_floatingButton)
                                        .show();
                                break;
                            case Constants.DATABASE_INSERT_ALREADY_PRESENT:
                                Snackbar.make(requireView(), R.string.food_present,
                                        Snackbar.LENGTH_SHORT)
                                        .setAnchorView(R.id.addCalories_floatingButton)
                                        .show();
                                break;
                            case Constants.DATABASE_INSERT_ERROR:
                                Snackbar.make(requireView(), R.string.error,
                                        Snackbar.LENGTH_SHORT)
                                        .setAnchorView(R.id.addCalories_floatingButton)
                                        .show();
                                break;
                        }
                    });
            viewModel.addFoodToRecent(foodToAdd).observe(this, integer -> {
                if (integer != Constants.DATABASE_INSERT_RECENT_FOOD_OK) {
                    Snackbar.make(requireView(), R.string.error,
                            Snackbar.LENGTH_SHORT)
                            .setAnchorView(R.id.addCalories_floatingButton)
                            .show();
                }
            });
        }
    }

    @Override
    public void modifyFood(Food updatedFood) {

    }

    @SuppressLint("ShowToast")
    private void removeFoodFromMeal(@NotNull @NonNull Food foodToRemove,
                                    @NotNull @NonNull MealType mealToModify) {
        Date currentDate = viewModel.getCurrentDate();

        viewModel.removeFoodFromMeal(foodToRemove, mealToModify, currentDate)
                .observe(getViewLifecycleOwner(), integer -> {
                    switch (integer) {
                        case Constants.DATABASE_REMOVE_OK:
                            Snackbar.make(requireView(), R.string.removed,
                                    Snackbar.LENGTH_SHORT)
                                    .setAnchorView(R.id.addCalories_floatingButton)
                                    .show();
                            break;
                        case Constants.DATABASE_REMOVE_NOT_PRESENT:
                            Snackbar.make(requireView(), R.string.not_found,
                                    Snackbar.LENGTH_SHORT)
                                    .setAnchorView(R.id.addCalories_floatingButton)
                                    .show();
                            break;
                        case Constants.DATABASE_REMOVE_ERROR:
                            Snackbar.make(requireView(), R.string.error,
                                    Snackbar.LENGTH_SHORT)
                                    .setAnchorView(R.id.addCalories_floatingButton)
                                    .show();
                            break;
                    }
                });
        viewModel.removeFoodFromRecent(foodToRemove).observe(this, integer -> {
            if (integer != Constants.DATABASE_REMOVE_RECENT_FOOD_OK) {
                Snackbar.make(requireView(), R.string.error,
                        Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.addCalories_floatingButton)
                        .show();
            }
        });
    }
}