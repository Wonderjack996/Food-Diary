package it.fooddiary.ui.meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import it.fooddiary.R;
import it.fooddiary.databases.IDatabaseOperation;
import it.fooddiary.databinding.ActivityMealBinding;
import it.fooddiary.models.Food;
import it.fooddiary.models.Meal;
import it.fooddiary.models.UserProperties;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.repositories.UserRepository;
import it.fooddiary.ui.FoodRecyclerAdapter;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.DateUtils;
import it.fooddiary.utils.MealType;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;
import it.fooddiary.viewmodels.user.UserViewModel;
import it.fooddiary.viewmodels.user.UserViewModelFactory;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MealActivity extends AppCompatActivity implements IDatabaseOperation {

    private ActivityMealBinding binding;

    private FoodRecyclerAdapter recyclerAdapter;

    private Date associatedDate;
    private MealType mealType;

    private FoodViewModel foodViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodViewModel = new ViewModelProvider(this,
                new FoodViewModelFactory(getApplication(),
                        new FoodRepository(getApplication())))
                .get(FoodViewModel.class);

        UserViewModel userViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(getApplication(), new UserRepository(getApplication())))
                .get(UserViewModel.class);

        Intent intent = getIntent();
        if (intent.getSerializableExtra(Constants.CURRENT_DATE) instanceof Date)
            associatedDate = (Date) intent.getSerializableExtra(Constants.CURRENT_DATE);
        else
            associatedDate = new Date();
        if (intent.getSerializableExtra(Constants.MEAL_TYPE) instanceof  MealType)
            mealType = (MealType) intent.getSerializableExtra(Constants.MEAL_TYPE);
        else
            mealType = MealType.BREAKFAST;

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mealType.toString(getResources())
                    + " - " + DateUtils.dateFormat.format(associatedDate));

        recyclerAdapter = new FoodRecyclerAdapter(getSupportFragmentManager(),
                new FoodPropertiesItemAlert(this));

        userViewModel.getUserProperties().observe(this, new Observer<UserProperties>() {
            @Override
            public void onChanged(@NonNull @NotNull UserProperties mealProperties) {
                binding.setUserProperties(mealProperties);
                binding.invalidateAll();
            }
        });

        binding.foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.foodRecyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

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
                    Food foodToRemove = recyclerAdapter.getFoodByPosition(position);

                    foodViewModel.removeFoodFromMeal(foodToRemove, mealType, associatedDate)
                            .observe(MealActivity.this, integer -> {
                                switch (integer) {
                                    case Constants.DATABASE_REMOVE_OK:
                                        Food removed = recyclerAdapter.removeItem(position);
                                        recyclerAdapter.notifyItemRemoved(position);
                                        Snackbar.make(binding.getRoot(),
                                                removed.getName() + " " + getResources().getString(R.string.deleted),
                                                Snackbar.LENGTH_LONG)
                                                .setAction("Undo", view -> onDeleteUndo(removed,
                                                        mealType, associatedDate,
                                                        position))
                                                .setAnchorView(binding.floatingActionButton)
                                                .show();
                                        reloadMeal();
                                        break;
                                    case Constants.DATABASE_REMOVE_NOT_PRESENT:
                                        Snackbar.make(binding.getRoot(),
                                                R.string.not_found, Snackbar.LENGTH_LONG)
                                                .setAnchorView(binding.floatingActionButton)
                                                .show();
                                        break;
                                    case Constants.DATABASE_REMOVE_ERROR:
                                        Snackbar.make(binding.getRoot(),
                                                R.string.error, Snackbar.LENGTH_LONG)
                                                .setAnchorView(binding.floatingActionButton)
                                                .show();
                                        break;
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
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MealActivity.this, R.color.primaryColor))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.foodRecyclerView);

        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(MealActivity.this,
                    SearchHostActivity.class);
            startActivity(intent1);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        completeReload();
    }

    @SuppressLint("ShowToast")
    private void onDeleteUndo(@NotNull @NonNull Food foodToAdd,
                              @NotNull @NonNull MealType mealType,
                              @NotNull @NonNull Date date, int position) {
        LiveData<Integer> databaseResponse =
                foodViewModel.insertFoodInMeal(foodToAdd, mealType, date);

        databaseResponse.observe(this, integer -> {
            switch (integer) {
                case Constants.DATABASE_INSERT_OK:
                case Constants.DATABASE_UPDATE_OK:
                    recyclerAdapter.addItem(foodToAdd, position);
                    recyclerAdapter.notifyItemInserted(position);
                    reloadMeal();
                    Snackbar.make(binding.getRoot(), R.string.added, Snackbar.LENGTH_LONG)
                                .setAnchorView(binding.floatingActionButton)
                                .show();
                    break;
                case Constants.DATABASE_INSERT_ERROR:
                case Constants.DATABASE_UPDATE_ERROR:
                    Snackbar.make(binding.getRoot(),
                            R.string.error, Snackbar.LENGTH_LONG)
                            .setAnchorView(binding.floatingActionButton)
                            .show();
                    break;
            }
        });
    }

    private void reloadMeal() {
        binding.searchingTextView.setVisibility(View.INVISIBLE);
        foodViewModel.getMealByTypeAndDate(mealType, associatedDate).observe(this, new Observer<Meal>() {
            @Override
            public void onChanged(@NonNull @NotNull Meal meal) {
                binding.setMeal(meal);
                if (meal.getMealFoods().size() == 0)
                    binding.searchingTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void completeReload() {
        binding.searchingTextView.setVisibility(View.INVISIBLE);
        foodViewModel.getMealByTypeAndDate(mealType, associatedDate)
                .observe(MealActivity.this, new Observer<Meal>() {
                    @Override
                    public void onChanged(@NonNull @NotNull Meal meal) {
                        recyclerAdapter.setFoodDataset(meal.getMealFoods());
                        binding.setMeal(meal);
                        if (meal.getMealFoods().size() == 0)
                            binding.searchingTextView.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void addFoodToMeal(Food foodToAdd, MealType mealType) {

    }

    @SuppressLint("ShowToast")
    @Override
    public void modifyFood(@NotNull @NonNull Food newFood) {
        Food oldFood = recyclerAdapter.getFood(newFood);
        if (oldFood != null) {
            foodViewModel.updateFoodInMeal(newFood, mealType, associatedDate).observe(this, integer -> {
                switch (integer) {
                    case Constants.DATABASE_UPDATE_OK:
                        completeReload();
                        break;
                    case Constants.DATABASE_UPDATE_ERROR:
                        Snackbar.make(binding.getRoot(),
                                R.string.error,
                                Snackbar.LENGTH_LONG)
                                .setAnchorView(binding.floatingActionButton)
                                .show();
                        break;
                }
            });
        }
    }
}