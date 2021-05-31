package it.fooddiary.ui.meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.ui.search.SearchFragment;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;

public class SearchHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_search);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search);

        SearchFragment searchFragment = new SearchFragment();
        Intent intent = getIntent();
        MealType mealType;
        try {
            mealType = (MealType) intent.getSerializableExtra(Constants.MEAL_TYPE);
        } catch (ClassCastException cce) {
            mealType = null;
        }

        if (mealType != null)
            searchFragment.setDisplayedMealType(mealType);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view,
                searchFragment).commit();
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