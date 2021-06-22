package it.fooddiary.ui.search.recents;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.FragmentRecentBinding;
import it.fooddiary.models.Food;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.ui.FoodRecyclerAdapter;
import it.fooddiary.ui.search.SearchFragment;
import it.fooddiary.ui.search.searched.FoodSearchedItemAlert;
import it.fooddiary.utils.Constants;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RecentFragment extends Fragment {

    private FoodRecyclerAdapter foodRecyclerAdapter;

    private FragmentRecentBinding binding;

    private FoodViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecentBinding.inflate(inflater);

        viewModel = new ViewModelProvider(this,
                new FoodViewModelFactory(requireActivity().getApplication(),
                        new FoodRepository(requireActivity().getApplication())))
                .get(FoodViewModel.class);

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

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @SuppressLint("ShowToast")
                    @Override
                    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position;
                        if (swipeDir == ItemTouchHelper.LEFT) {
                            position = viewHolder.getAdapterPosition();
                            Food foodToRemove = foodRecyclerAdapter.getFoodByPosition(position);
                            viewModel.removeFoodFromRecent(foodToRemove)
                                    .observe(RecentFragment.this.requireActivity(), integer -> {
                                        if (integer == Constants.DATABASE_REMOVE_RECENT_FOOD_OK) {
                                            Food removed = foodRecyclerAdapter.removeItem(position);
                                            foodRecyclerAdapter.notifyItemRemoved(position);
                                            Snackbar.make(binding.getRoot(),
                                                    removed.getName() + " " + getResources().getString(R.string.deleted),
                                                    Snackbar.LENGTH_LONG)
                                                    .setAction("Undo", view -> onDeleteUndo(foodToRemove, position))
                                                    .setAnchorView(R.id.addCalories_floatingButton)
                                                    .show();
                                        } else {
                                            Snackbar.make(binding.getRoot(),
                                                    R.string.error, Snackbar.LENGTH_LONG)
                                                    .setAnchorView(R.id.addCalories_floatingButton)
                                                    .show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c,
                                            @NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY,
                                            int actionState, boolean isCurrentlyActive) {
                        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder,
                                dX, dY, actionState, isCurrentlyActive)
                                .addSwipeLeftBackgroundColor(ContextCompat.getColor(RecentFragment.this.requireContext(),
                                        R.color.primaryColor))
                                .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                                .create()
                                .decorate();
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.recentList);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.searchingTextView.setVisibility(View.VISIBLE);
        viewModel.getRecentFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(@NonNull @NotNull List<Food> foods) {
                Collections.reverse(foods);
                foodRecyclerAdapter.setFoodDataset(foods);
                if(foods.size() > 0)
                    binding.searchingTextView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @SuppressLint("ShowToast")
    public void onDeleteUndo(@NotNull @NonNull Food foodToAdd, int position) {
        viewModel.addFoodToRecent(foodToAdd).observe(this.requireActivity(), integer -> {
            if (integer == Constants.DATABASE_INSERT_RECENT_FOOD_OK) {
                foodRecyclerAdapter.addItem(foodToAdd, position);
                foodRecyclerAdapter.notifyItemInserted(position);
                Snackbar.make(binding.getRoot(), R.string.added, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.addCalories_floatingButton)
                        .show();
            } else {
                Snackbar.make(binding.getRoot(),
                        R.string.error, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.addCalories_floatingButton)
                        .show();
            }
        });
    }
}