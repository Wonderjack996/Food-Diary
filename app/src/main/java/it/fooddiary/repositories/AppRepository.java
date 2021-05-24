package it.fooddiary.repositories;

import android.app.Application;
import android.util.Log;

import java.util.Date;
import java.util.List;

import it.fooddiary.databases.MealDao;
import it.fooddiary.models.Meal;
import it.fooddiary.models.edamam_models.EdamamResponse;
import it.fooddiary.services.IFoodServices;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;
import it.fooddiary.utils.ServicesLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {

    private static final String TAG = "AppRepository";

    private final IFoodServices foodServices;
    private final MealDao mealDao;

    public AppRepository(Application application) {
        this.foodServices = ServicesLocator.getInstance().getFoodServicesWithRetrofit();
        this.mealDao = ServicesLocator.getInstance().getAppDatabase(application).mealDao();
    }

    public void fetchFoods(String ingredient) {
        Call<EdamamResponse> call = foodServices.getFoodsByName(Constants.API_APP_ID,
                Constants.API_APP_KEY, ingredient, Constants.API_NUTRITION_TYPE_LOGGING,
                Constants.API_CATEGORY_LABEL_FOOD, Constants.API_CATEGORY_GENERIC_FOOD);

        call.enqueue(new Callback<EdamamResponse>() {
            @Override
            public void onResponse(Call<EdamamResponse> call, Response<EdamamResponse> response) {
                if (response.body() != null)
                    Log.d(TAG, response.body().getText());
                else
                    Log.d(TAG, "Error");
            }

            @Override
            public void onFailure(Call<EdamamResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });
    }

    public void saveMealInLocalDB(Meal meal) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mealDao.insert(meal);
            }
        };
        new Thread(runnable).start();
    }

    public void readMealFromLocalDB(MealType mealType, Date date) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Meal> list = mealDao.getMealsByDateAndType(mealType, date);
            }
        };
        new Thread(runnable).start();
    }
}
