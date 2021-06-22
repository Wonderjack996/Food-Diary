package it.fooddiary.ui.meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.ui.search.SearchFragment;

public class SearchHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_search);

        if (getSupportActionBar() != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.search);
        }

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_view) == null) {
            SearchFragment searchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view,
                    searchFragment).commit();
        }
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
}