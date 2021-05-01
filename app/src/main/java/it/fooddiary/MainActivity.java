package it.fooddiary;

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();
    }

    public void changeToolbarTitle(String title) {
        MaterialToolbar matTool = getSupportActionBar().
                getCustomView().findViewById(R.id.materialToolbar);
        matTool.setTitle(title);
    }

    public void setDiaryToolbar() {
        getSupportActionBar().setCustomView(R.layout.toolbar_diary);
    }

    public void setSearchToolbar() {
        getSupportActionBar().setCustomView(R.layout.toolbar_search);
    }

    public void setAccountToolbar() {
        getSupportActionBar().setCustomView(R.layout.toolbar_account);
    }

    public Toolbar getCurrentToolbar() {
        return (Toolbar) findViewById(R.id.materialToolbar);
    }

    private void setupUI() {
        // set associated layout
        setContentView(R.layout.activity_main);

        // change default toolbar with my toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setDiaryToolbar();

        setUpBottomNavView();
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
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,
                navController,
                appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


}
