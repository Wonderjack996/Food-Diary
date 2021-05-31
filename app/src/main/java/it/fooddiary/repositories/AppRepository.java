package it.fooddiary.repositories;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.fooddiary.databases.MealDao;
import it.fooddiary.databases.RecentFoodDao;
import it.fooddiary.models.Food;
import it.fooddiary.models.MealProperties;
import it.fooddiary.models.Meal;
import it.fooddiary.models.edamam_models.EdamamResponse;
import it.fooddiary.services.IFoodServices;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.DateUtils;
import it.fooddiary.utils.MealType;
import it.fooddiary.utils.ServicesLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {

    private static final String TAG = "AppRepository";

    private final IFoodServices foodServices;
    private final MealDao mealDao;
    private final RecentFoodDao recentFoodDao;
    private final Application application;

    private final MealProperties mealPropertiesCurrentSetting;

    private final MutableLiveData<MealProperties> mealProperties;

    public AppRepository(Application application) {
        this.application = application;
        this.foodServices = ServicesLocator.getInstance().getFoodServicesWithRetrofit();
        this.mealDao = ServicesLocator.getInstance().getAppDatabase(application).mealDao();
        this.recentFoodDao = ServicesLocator.getInstance().getAppDatabase(application).recentFoodDao();

        this.mealProperties = new MutableLiveData<>();

        this.mealPropertiesCurrentSetting = loadMealPropertiesFromSharedPreferences();
    }

    public MutableLiveData<EdamamResponse> fetchFoods(String ingredient) {
        MutableLiveData<EdamamResponse> edamamResponse = new MutableLiveData<>();

        Call<EdamamResponse> call = foodServices.getFoodsByName(Constants.API_APP_ID,
                Constants.API_APP_KEY, ingredient, Constants.API_NUTRITION_TYPE_LOGGING,
                Constants.API_CATEGORY_LABEL_FOOD, Constants.API_CATEGORY_GENERIC_FOOD);

        call.enqueue(new Callback<EdamamResponse>() {
            @Override
            public void onResponse(Call<EdamamResponse> call, Response<EdamamResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    edamamResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<EdamamResponse> call, Throwable t) {
                edamamResponse.postValue(new EdamamResponse(null, t.getMessage(),
                        null, null, null));
            }
        });

        return edamamResponse;
    }

    public LiveData<Meal> getMealByTypeAndDate(MealType mealType, Date date) {
        MutableLiveData<Meal> mealMutableLiveData = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Meal> meals = mealDao.getMeals(mealType, date);
                if (meals == null || meals.size() != 1)
                    mealMutableLiveData.postValue(new Meal(mealType, date));
                else
                    mealMutableLiveData.postValue(meals.get(0));
            }
        }).start();

        return mealMutableLiveData;
    }

    public LiveData<Integer> insertFoodInMeal(Food foodToInsert,
                                                     MealType mealType, Date date) {
        MutableLiveData<Integer> databaseOperationResult = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Meal> mealList = mealDao.getMeals(mealType, date);
                if (mealList == null || mealList.size() == 0) {
                    Meal newMeal = new Meal(mealType, date);
                    newMeal.addFood(foodToInsert);
                    mealDao.insert(newMeal);
                    databaseOperationResult.postValue(Constants.DATABASE_INSERT_OK);
                } else if (mealList.size() == 1) {
                    Meal updatedMeal = mealList.get(0);
                    updatedMeal.addFood(foodToInsert);
                    mealDao.update(updatedMeal);
                    databaseOperationResult.postValue(Constants.DATABASE_UPDATE_OK);
                } else
                    databaseOperationResult.postValue(Constants.DATABASE_UPDATE_ERROR);
            }
        }).start();

        return databaseOperationResult;
    }

    public LiveData<Integer> addFoodToRecent(Food foodToAdd){
        new Thread(new Runnable() {
            @Override
            public void run() {
               List<Food> recentList = recentFoodDao.getAll();
                if(recentList != null && !recentList.contains(foodToAdd)){
                    recentFoodDao.insert(foodToAdd);
                    databaseOperationResult.postValue(Constants.DATABASE_INSERT_FOOD_OK);
                }
                else {
                    databaseOperationResult.postValue(Constants.DATABASE_INSERT_FOOD_ERROR);
                }
            }

        }).start();
        return databaseOperationResult;
    }

    public LiveData<Integer> removeFoodFromMeal(Food foodToRemove,
                                                       MealType mealType, Date date) {
        MutableLiveData<Integer> databaseOperationResult = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Meal> mealList = mealDao.getMeals(mealType, date);
                if (mealList == null || mealList.size() == 0) {
                    databaseOperationResult.postValue(Constants.DATABASE_REMOVE_ERROR);
                } else if (mealList.size() == 1) {
                    Meal updatedMeal = mealList.get(0);
                    boolean isRemoved = updatedMeal.removeFood(foodToRemove);
                    if (isRemoved && updatedMeal.getMealFoods().size() == 0) {
                        mealDao.delete(updatedMeal);
                        databaseOperationResult.postValue(Constants.DATABASE_REMOVE_OK);
                    } else if (isRemoved) {
                        mealDao.update(updatedMeal);
                        databaseOperationResult.postValue(Constants.DATABASE_REMOVE_OK);
                    } else
                        databaseOperationResult.postValue(Constants.DATABASE_REMOVE_NOT_PRESENT);
                } else
                    databaseOperationResult.postValue(Constants.DATABASE_REMOVE_ERROR);
            }
        }).start();

        return databaseOperationResult;
    }

    public LiveData<MealProperties> getMealProperties() {
        mealProperties.setValue(mealPropertiesCurrentSetting);
        return mealProperties;
    }

    public void setDailyCaloriesIntake(int newCaloriesIntake) {
        mealPropertiesCurrentSetting.setCaloriesDailyIntake(newCaloriesIntake);
        mealProperties.postValue(mealPropertiesCurrentSetting);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = application
                        .getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                                Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.remove(Constants.USER_DAILY_INTAKE_KCAL);
                editor.putInt(Constants.USER_DAILY_INTAKE_KCAL, newCaloriesIntake);

                editor.apply();
            }
        }).start();
    }

    private MealProperties loadMealPropertiesFromSharedPreferences() {
        SharedPreferences preferences = application
                .getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);

        int cal = preferences
                .getInt(Constants.USER_DAILY_INTAKE_KCAL, 0);
        float carbsPerc = preferences
                .getFloat(Constants.USER_DAILY_CARBS_PERCENT,
                        Constants.DEFAULT_CARBS_PERCENT_DAILY);
        float proteinsPerc = preferences
                .getFloat(Constants.USER_DAILY_PROTEINS_PERCENT,
                        Constants.DEFAULT_PROTEINS_PERCENT_DAILY);
        float fatsPerc = preferences
                .getFloat(Constants.USER_DAILY_FATS_PERCENT,
                        Constants.DEFAULT_FATS_PERCENT_DAILY);

        return new MealProperties(cal, carbsPerc, proteinsPerc, fatsPerc);
    }

    public Date loadCurrentDate(SharedPreferences preferences) {
        Date ret;
        String date = preferences.getString(Constants.CURRENT_DATE, null);

        if (date == null)
            ret = Calendar.getInstance().getTime();
        else {
            try {
                ret = DateUtils.dateFormat.parse(date);
            } catch (ParseException e) {
                ret = Calendar.getInstance().getTime();
            }
        }

        return ret;
    }

    public void saveCurrentDate(Date date, SharedPreferences preferences) {
        if (date != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString(Constants.CURRENT_DATE, DateUtils.dateFormat.format(date));

                        editor.apply();
                    }
                }
            }).start();
        }
    }
}