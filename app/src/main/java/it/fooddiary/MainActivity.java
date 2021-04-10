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
import it.fooddiary.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView toolbarTitle;
    private ImageButton calendarButton;
    private CalendarPopUp calendarPopup;

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
        //calendarPopup = new CalendarPopUp();
    }

    public void setDiaryToolbar() {
        toolbarTitle.setText(R.string.diary);
        calendarButton.setVisibility(View.VISIBLE);
    }

    public void setSearchToolbar() {
        toolbarTitle.setText(R.string.search);
        calendarButton.setVisibility(View.INVISIBLE);
    }

    public void setAccountToolbar() {
        toolbarTitle.setText(R.string.account);
        calendarButton.setVisibility(View.INVISIBLE);
    }

    public void openCalendar(View view) {
        Log.d(TAG, "calendar opened");
    }
}