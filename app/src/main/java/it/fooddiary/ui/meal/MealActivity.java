package it.fooddiary.ui.meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityMealsBinding;
import it.fooddiary.models.Food;
import it.fooddiary.util.Constants;
import it.fooddiary.util.DateUtils;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MealActivity extends AppCompatActivity {

    private ActivityMealsBinding binding;

    private final List<Food> foodDataset = new ArrayList<>();
    private FoodRecyclerAdapter recyclerAdapter;

    private Date currentDate;
    private String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMealsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mealType = intent.getStringExtra(Constants.MEALS_NAME);
        try {
            currentDate = DateUtils.dateFormat.
                    parse(getIntent().getStringExtra(Constants.CURRENT_DATE));
        } catch (ParseException e) {
            currentDate = null;
        }

        if( mealType != null && currentDate != null )
            getSupportActionBar().setTitle(mealType + " - " +
                    DateUtils.dateFormat.format(currentDate));

        // carico il dataset
        for(int i = 0; i < 12; ++i)
            foodDataset.add(new Food("Food " + i, 100,
                    0.5,0.2, 0.3));

        //creo l'adapter
        recyclerAdapter = new FoodRecyclerAdapter(foodDataset, this);

        binding.foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.foodRecyclerView.setAdapter(recyclerAdapter);

        //settaggio swipe
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
                Food removed = null;
                switch(swipeDir) {
                    case ItemTouchHelper.LEFT:
                        position = viewHolder.getAdapterPosition();
                        removed = recyclerAdapter.removeItem(position);
                        recyclerAdapter.notifyItemRemoved(position);
                        Food finalRemoved = removed;
                        Snackbar.make(binding.foodRecyclerView, removed.getName() + " deleted!",
                                Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        recyclerAdapter.addItem(finalRemoved, position);
                                        recyclerAdapter.notifyItemInserted(position);
                                    }
                                }).show();
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