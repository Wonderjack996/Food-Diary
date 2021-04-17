package it.fooddiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import it.fooddiary.ui.account.AccountFragment;
import it.fooddiary.ui.diary.CalendarPopUp;
import it.fooddiary.ui.diary.DiaryFragment;
import it.fooddiary.ui.search.AddPopUp;
import it.fooddiary.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView toolbarTitle;
    private ImageButton calendarButton;
    private CalendarPopUp calendarPopup;
    private AddPopUp addPopup;

    private ImageButton searchButton;
    private ImageButton addButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Insert my toolbar
         */
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_custom);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_diary,
                R.id.navigation_search,
                R.id.navigation_account).build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,
                navController,
                appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        toolbarTitle = findViewById(R.id.toolbar_custom_title);
        calendarButton = findViewById(R.id.calendar_button);

        searchButton = findViewById(R.id.search_button);
        addButton = findViewById(R.id.add_button);

        //calendarPopup = new CalendarPopUp();
       // addPopup = new AddPopUp(); //todo...
    }

    public void setDiaryToolbar() {
        toolbarTitle.setText(R.string.diary);
        calendarButton.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
    }

    public void setSearchToolbar() {
        toolbarTitle.setText(R.string.search);
        searchButton.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        calendarButton.setVisibility(View.INVISIBLE);

    }

    public void setAccountToolbar() {
        toolbarTitle.setText(R.string.account);
        calendarButton.setVisibility(View.INVISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);

    }

    public void openCalendar(View view) {
        Log.d(TAG, "calendar opened");
    }

    public void openSearch(View view) {
        Log.d(TAG, "search opened");
    }

    public void openAdd(View view) {
        Log.d(TAG, "add opened");
    }

}