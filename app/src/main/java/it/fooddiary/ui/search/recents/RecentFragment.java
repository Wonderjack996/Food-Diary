package it.fooddiary.ui.search.recents;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.FragmentRecentBinding;
import it.fooddiary.models.Food;
import it.fooddiary.repositories.AppRepository;
import it.fooddiary.ui.FoodRecyclerAdapter;
import it.fooddiary.ui.search.SearchFragment;
import it.fooddiary.ui.search.searched.FoodSearchedItemAlert;
import it.fooddiary.viewmodels.AppViewModel;
import it.fooddiary.viewmodels.AppViewModelFactory;

public class RecentFragment extends Fragment {
    private FoodRecyclerAdapter foodRecyclerAdapter;
    private FragmentRecentBinding binding;

    private AppViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecentBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this,
                new AppViewModelFactory(requireActivity().getApplication(),
                        new AppRepository(requireActivity().getApplication())))
                .get(AppViewModel.class);
        Fragment parent = getParentFragment();
        if (parent instanceof SearchFragment) {
            foodRecyclerAdapter =
                    new FoodRecyclerAdapter(getChildFragmentManager(),
                            new FoodSearchedItemAlert((SearchFragment)parent));
        } else {
            foodRecyclerAdapter =
                    new FoodRecyclerAdapter(getChildFragmentManager(),
                            new FoodSearchedItemAlert(null));
        }
        binding.recentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recentList.setAdapter(foodRecyclerAdapter);
        viewModel.getRecentFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                if(foods != null ) {
                    Collections.reverse(foods);
                    foodRecyclerAdapter.setFoodDataset(foods);
                    if(foods.size() == 0)
                        binding.searchingTextView.setVisibility(View.VISIBLE);
                    else
                        binding.searchingTextView.setVisibility(View.INVISIBLE);

                }



            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}