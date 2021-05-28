package it.fooddiary.ui.search.searched;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.FragmentFoodSearchedBinding;
import it.fooddiary.models.Food;
import it.fooddiary.models.edamam_models.EdamamResponse;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;
import it.fooddiary.viewmodels.AppViewModel;

public class FoodSearchedFragment extends Fragment {

    private static final String TAG = "FoodSearchedFragment";

    private static final int MAX_SEARCHED_FOODS_DISPLAYED = 20;

    private static final List<Food> lastSearchedFoodList = new ArrayList<>();

    private FoodSearchedRecyclerAdapter recyclerAdapter = null;
    private FragmentFoodSearchedBinding binding;

    private LiveData<EdamamResponse> edamamResponseLiveData;
    private LiveData<Integer> databaseResponseLiveData;
    private AppViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoodSearchedBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        recyclerAdapter = new FoodSearchedRecyclerAdapter(getActivity(), new IFoodAdded() {
            @Override
            public void onFoodAddedToMeal(Food foodToAdd, MealType mealToModify) {
                FoodSearchedFragment.this.onFoodAddedToMeal(foodToAdd, mealToModify);
            }
        });
        if (lastSearchedFoodList.size() == 0) {
            binding.searchingTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerAdapter.setFoodDataset(lastSearchedFoodList);
            binding.searchingTextView.setVisibility(View.INVISIBLE);
        }

        binding.searchingRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.searchingRecView.setAdapter(recyclerAdapter);

        return binding.getRoot();
    }

    public void onFoodSearched(String ingredient) {
        binding.searchingTextView.setVisibility(View.INVISIBLE);
        binding.searchingProgressBar.setVisibility(View.VISIBLE);

        if (edamamResponseLiveData == null) {
            edamamResponseLiveData = viewModel.getEdamamResponse(ingredient);
            edamamResponseLiveData.observe(getViewLifecycleOwner(),
                    new Observer<EdamamResponse>() {
                        @Override
                        public void onChanged(EdamamResponse edamamResponse) {
                            if (edamamResponse.getMessage() == null &&
                                    edamamResponse.getStatus() == null) {
                                if (edamamResponse.getHints().size() > 0 ||
                                        edamamResponse.getParsed().size() > 0) {
                                    int i;
                                    lastSearchedFoodList.clear();
                                    for(i = 0; i < edamamResponse.getParsed().size() &&
                                            i < MAX_SEARCHED_FOODS_DISPLAYED; ++i) {
                                        Food tmp = edamamResponse.getParsed().get(i)
                                                .getFood().toFood();
                                        lastSearchedFoodList.add(tmp);
                                    }
                                    for(int j = 0; j < edamamResponse.getHints().size() &&
                                            i < MAX_SEARCHED_FOODS_DISPLAYED; ++i, ++j) {
                                        Food tmp = edamamResponse.getHints().get(j)
                                                .getFood().toFood();
                                        lastSearchedFoodList.add(tmp);
                                    }
                                    recyclerAdapter.setFoodDataset(lastSearchedFoodList);
                                } else {
                                    recyclerAdapter.setFoodDataset(new ArrayList<>());
                                    binding.searchingTextView.setText(edamamResponse.getText()
                                            + " "
                                            + getResources().getString(R.string.not_found) + "!");
                                    binding.searchingTextView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerAdapter.setFoodDataset(new ArrayList<>());
                                binding.searchingTextView.setText(edamamResponse.getMessage());
                                binding.searchingTextView.setVisibility(View.VISIBLE);
                            }
                            binding.searchingProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        } else
            viewModel.getEdamamResponse(ingredient);
    }

    private void onFoodAddedToMeal(Food foodToAdd, MealType mealToModify) {
        Date currentDate = viewModel.getCurrentDate(getActivity()
                .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                        Context.MODE_PRIVATE));

        Snackbar dbSnackBar = Snackbar.make(binding.getRoot(), R.string.adding,
            Snackbar.LENGTH_INDEFINITE);

        databaseResponseLiveData = viewModel.insertFoodInMeal(foodToAdd,
                mealToModify, currentDate);
        databaseResponseLiveData.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case Constants.DATABASE_INSERT_OK:
                    case Constants.DATABASE_UPDATE_OK:
                        Snackbar.make(binding.getRoot(), R.string.added,
                            Snackbar.LENGTH_SHORT)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FoodSearchedFragment.this
                                            .onFoodRemovedToMeal(foodToAdd, mealToModify);
                                }
                            })
                            .show();
                        break;
                    case Constants.DATABASE_REMOVE_OK:
                        Snackbar.make(binding.getRoot(), R.string.removed,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                    case Constants.DATABASE_REMOVE_NOT_PRESENT:
                        Snackbar.make(binding.getRoot(), R.string.not_found,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                    case Constants.DATABASE_INSERT_ERROR:
                    case Constants.DATABASE_UPDATE_ERROR:
                    case Constants.DATABASE_REMOVE_ERROR:
                        Snackbar.make(binding.getRoot(), R.string.error,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        dbSnackBar.show();
    }

    private void onFoodRemovedToMeal(Food foodToRemove, MealType mealToModify) {
        Date currentDate = viewModel.getCurrentDate(getActivity()
                .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                        Context.MODE_PRIVATE));

        viewModel.removeFoodFromMeal(foodToRemove, mealToModify, currentDate);

        Snackbar.make(binding.getRoot(), R.string.removing,
                Snackbar.LENGTH_INDEFINITE).show();
    }
}