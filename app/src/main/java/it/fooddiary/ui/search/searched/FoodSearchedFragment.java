package it.fooddiary.ui.search.searched;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.FragmentFoodSearchedBinding;
import it.fooddiary.models.Food;
import it.fooddiary.models.edamam_models.EdamamResponse;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.ui.FoodRecyclerAdapter;
import it.fooddiary.ui.search.SearchFragment;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;

public class FoodSearchedFragment extends Fragment {

    private static final String TAG = "FoodSearchedFragment";

    private static final int MAX_SEARCHED_FOODS_DISPLAYED = 20;

    private static final List<Food> lastSearchedFoodList = new ArrayList<>();

    private FoodRecyclerAdapter recyclerAdapter = null;
    private FragmentFoodSearchedBinding binding;

    private FoodViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFoodSearchedBinding.inflate(inflater);

        viewModel = new ViewModelProvider(this,
                new FoodViewModelFactory(requireActivity().getApplication(),
                        new FoodRepository(requireActivity().getApplication())))
                .get(FoodViewModel.class);

        Fragment parent = getParentFragment();
        if (parent instanceof SearchFragment) {
            recyclerAdapter =
                    new FoodRecyclerAdapter(getChildFragmentManager(),
                            new FoodSearchedItemAlert((SearchFragment)parent));
        } else {
            recyclerAdapter =
                    new FoodRecyclerAdapter(getChildFragmentManager(),
                            new FoodSearchedItemAlert(null));
        }

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

        LiveData<EdamamResponse> edamamResponseLiveData = viewModel.getEdamamResponse(ingredient);
        edamamResponseLiveData.observe(getViewLifecycleOwner(), new Observer<EdamamResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(@NonNull @NotNull EdamamResponse edamamResponse) {
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
                                if (!lastSearchedFoodList.contains(tmp) && tmp.getTotalCalories() > 0)
                                    lastSearchedFoodList.add(tmp);
                            }
                            for(int j = 0; j < edamamResponse.getHints().size() &&
                                    i < MAX_SEARCHED_FOODS_DISPLAYED; ++i, ++j) {
                                Food tmp = edamamResponse.getHints().get(j)
                                        .getFood().toFood();
                                if (!lastSearchedFoodList.contains(tmp) && tmp.getTotalCalories() > 0)
                                    lastSearchedFoodList.add(tmp);
                            }
                            recyclerAdapter.setFoodDataset(lastSearchedFoodList);
                        } else {
                            recyclerAdapter.setFoodDataset(new ArrayList<>());
                            binding.searchingTextView
                                    .setText(edamamResponse.getText() + " "
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
    }
}