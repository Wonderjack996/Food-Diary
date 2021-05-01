package it.fooddiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import it.fooddiary.util.Constants;

public class MealsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        Intent intent = getIntent();
        String mealName = intent.getStringExtra(Constants.MEALS_NAME);
        if( mealName != null )
            getSupportActionBar().setTitle(mealName);
    }
}