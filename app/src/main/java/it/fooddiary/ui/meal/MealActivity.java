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

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityMealBinding;
import it.fooddiary.models.Food;
import it.fooddiary.models.Meal;
import it.fooddiary.models.MealProperties;
import it.fooddiary.repositories.AppRepository;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.DateUtils;
import it.fooddiary.utils.MealType;
import it.fooddiary.viewmodels.AppViewModel;
import it.fooddiary.viewmodels.AppViewModelFactory;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MealActivity extends AppCompatActivity {

    private ActivityMealBinding binding;

    private final List<Food> foodDataset = new ArrayList<>();
    private FoodRecyclerAdapter recyclerAdapter;

    private Date associatedDate;
    private MealType mealType;

    private AppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this,
                new AppViewModelFactory(getApplication(),
                        new AppRepository(getApplication())))
                .get(AppViewModel.class);

        Intent intent = getIntent();
        associatedDate = (Date) intent.getSerializableExtra(Constants.CURRENT_DATE);
        mealType = (MealType) intent.getSerializableExtra(Constants.MEAL_TYPE);

        getSupportActionBar().setTitle(mealType.toString(getResources())
                + " - " + DateUtils.dateFormat.format(associatedDate));

        recyclerAdapter = new FoodRecyclerAdapter(this);

        viewModel.getMealProperties().observe(this, new Observer<MealProperties>() {
            @Override
            public void onChanged(MealProperties mealProperties) {
                if (binding.getMealProperties() == null)
                    binding.setMealProperties(mealProperties);
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

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position;
                switch(swipeDir) {
                    case ItemTouchHelper.LEFT:
                        position = viewHolder.getAdapterPosition();
                        Food foodToRemove = recyclerAdapter.getFoodByPosition(position);

                        LiveData<Integer> databaseLiveData = viewModel
                                .removeFoodFromMeal(foodToRemove, mealType, associatedDate);

                        databaseLiveData.observe(MealActivity.this,
                                new Observer<Integer>() {
                                    @Override
                                    public void onChanged(Integer integer) {
                                        switch (integer) {
                                            case Constants.DATABASE_REMOVE_OK:
                                                Food removed = recyclerAdapter.removeItem(position);
                                                recyclerAdapter.notifyItemRemoved(position);
                                                Snackbar.make(binding.getRoot(),
                                                        removed.getName() + " " +
                                                                getResources().getString(R.string.deleted),
                                                        Snackbar.LENGTH_LONG)
                                                        .setAction("Undo", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                onDeleteUndo(removed,
                                                                        mealType, associatedDate,
                                                                        position);
                                                            }
                                                }).show();
                                                reloadMeal();
                                                break;
                                            case Constants.DATABASE_REMOVE_NOT_PRESENT:
                                                Snackbar.make(binding.getRoot(),
                                                        R.string.not_found, Snackbar.LENGTH_LONG)
                                                        .show();
                                                break;
                                            case Constants.DATABASE_REMOVE_ERROR:
                                                Snackbar.make(binding.getRoot(),
                                                        R.string.error, Snackbar.LENGTH_LONG)
                                                        .show();
                                                break;
                                        }
                                    }
                                });
                        break;
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

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this,
                        SearchHostActivity.class);

                intent.putExtra(Constants.MEAL_TYPE, mealType);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getMealByTypeAndDate(mealType, associatedDate).observe(this, new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                recyclerAdapter.setFoodDataset(meal.getMealFoods());
                binding.setMeal(meal);
            }
        });
    }

    private void onDeleteUndo(Food foodToAdd, MealType mealType, Date date, int position) {
        LiveData<Integer> databaseResponse = viewModel.insertFoodInMeal(foodToAdd, mealType, date);

        databaseResponse.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case Constants.DATABASE_INSERT_OK:
                    case Constants.DATABASE_UPDATE_OK:
                        recyclerAdapter.addItem(foodToAdd, position);
                        recyclerAdapter.notifyItemInserted(position);
                        Snackbar.make(binding.getRoot(),
                                R.string.added, Snackbar.LENGTH_LONG)
                                .show();
                        reloadMeal();
                        break;
                    case Constants.DATABASE_INSERT_ERROR:
                    case Constants.DATABASE_UPDATE_ERROR:
                        Snackbar.make(binding.getRoot(),
                                R.string.error, Snackbar.LENGTH_LONG)
                                .show();
                        break;
                }
            }
        });
    }

    private void reloadMeal() {
        LiveData<Meal> databaseResponse = viewModel
                .getMealByTypeAndDate(mealType, associatedDate);

        databaseResponse.observe(this, new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                binding.setMeal(meal);
                binding.invalidateAll();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return false;
        }
        return true;
    }
}