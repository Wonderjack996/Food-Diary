package it.fooddiary.ui.meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.models.Meal;
import it.fooddiary.ui.search.SearchFragment;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;

public class SearchHostActivity extends AppCompatActivity {

    private MealType mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_search);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_view) == null) {
            SearchFragment searchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view,
                    searchFragment).commit();
        }
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