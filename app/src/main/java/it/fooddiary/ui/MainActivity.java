package it.fooddiary.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityMainBinding;
import it.fooddiary.repositories.FoodRepository;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpBottomNavView();

        // esperimento uso API
        FoodRepository foodRepository = new FoodRepository();
        foodRepository.fetchFood("oats flour");
    }

    public void changeToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setUpBottomNavView() {
        // set up bottom navigation view
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_diary,
                R.id.navigation_search,
                R.id.navigation_account).build();
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,
                navController,
                appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


}