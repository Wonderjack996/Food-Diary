package it.fooddiary.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.fooddiary.databases.MealDao;
import it.fooddiary.databases.RecentFoodDao;
import it.fooddiary.models.Food;
import it.fooddiary.models.Meal;
import it.fooddiary.models.UserProperties;
import it.fooddiary.models.edamam_models.EdamamResponse;
import it.fooddiary.services.IFoodServices;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.DateUtils;
import it.fooddiary.utils.MealType;
import it.fooddiary.utils.ServicesLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodRepository {

    private static final String TAG = "FoodRepository";
    private static final int MAX_RECENT_FOOD_DIM = 30;
    private static final int NUM_RECENT_FOOD_TO_REMOVE = 3;

    private final IFoodServices foodServices;
    private final MealDao mealDao;
    private final RecentFoodDao recentFoodDao;
    private final Application application;

    public FoodRepository(Application application) {
        this.application = application;
        this.foodServices = ServicesLocator.getInstance().getFoodServicesWithRetrofit();
        this.mealDao = ServicesLocator.getInstance().getAppDatabase(application).mealDao();
        this.recentFoodDao = ServicesLocator.getInstance().getAppDatabase(application)
                .recentFoodDao();
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

    public LiveData<Integer> insertFoodInMeal(Food foodToInsert, MealType mealType, Date date) {
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
                    if (!updatedMeal.getMealFoods().contains(foodToInsert)) {
                        updatedMeal.addFood(foodToInsert);
                        mealDao.update(updatedMeal);
                        databaseOperationResult.postValue(Constants.DATABASE_INSERT_OK);
                    } else {
                        databaseOperationResult.postValue(Constants.DATABASE_INSERT_ALREADY_PRESENT);
                    }
                } else
                    databaseOperationResult.postValue(Constants.DATABASE_INSERT_ERROR);
            }
        }).start();

        return databaseOperationResult;
    }

    public LiveData<Integer> updateFoodInMeal(Food foodToUpdate, MealType mealType, Date date) {
        MutableLiveData<Integer> databaseOperationResult = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Meal> meals = mealDao.getMeals(mealType, date);
                if (meals == null || meals.size() != 1)
                    databaseOperationResult.postValue(Constants.DATABASE_UPDATE_ERROR);
                else {
                    Meal newMeal = meals.get(0);
                    if (newMeal.getMealFoods().contains(foodToUpdate)) {
                        Food oldFood = newMeal.getMealFoods()
                                .get(newMeal.getMealFoods().indexOf(foodToUpdate));
                        if (oldFood.getQuantity() != foodToUpdate.getQuantity()) {
                            boolean isRemoved = newMeal.removeFood(foodToUpdate);
                            if (isRemoved) {
                                newMeal.addFood(foodToUpdate);
                                mealDao.update(newMeal);
                                databaseOperationResult.postValue(Constants.DATABASE_UPDATE_OK);
                            } else
                                databaseOperationResult.postValue(Constants.DATABASE_UPDATE_ERROR);
                        }
                    } else
                        databaseOperationResult.postValue(Constants.DATABASE_UPDATE_ERROR);
                }
            }
        }).start();

        return databaseOperationResult;
    }

    public LiveData<Integer> removeFoodFromMeal(Food foodToRemove, MealType mealType, Date date) {
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

    public LiveData<Integer> addFoodToRecent(Food foodToAdd){
        MutableLiveData<Integer> databaseOperationResult = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Food> recentList = recentFoodDao.getAll();
                if(recentList != null && !recentList.contains(foodToAdd)) {
                    if (recentList.size() >= MAX_RECENT_FOOD_DIM) {
                        for (int i = 0; i < NUM_RECENT_FOOD_TO_REMOVE; ++i)
                            recentFoodDao.deleteOlder();
                    }
                    recentFoodDao.insert(foodToAdd);
                    databaseOperationResult.postValue(Constants.DATABASE_INSERT_RECENT_FOOD_OK);
                }
                else {
                    databaseOperationResult.postValue(Constants.DATABASE_INSERT_RECENT_FOOD_ERROR);
                }
            }
        }).start();

        return databaseOperationResult;
    }

    public LiveData<List<Food>> getRecentFoods() {
        MutableLiveData<List<Food>> listFoodLiveData = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Food> recentList = recentFoodDao.getAll();
                if(recentList != null)
                    listFoodLiveData.postValue(recentList);
                else
                    listFoodLiveData.postValue(new ArrayList<>());
            }
        }).start();
        return listFoodLiveData;
    }

    public LiveData<Integer> removeFoodFormRecent(Food foodToRemove) {
        MutableLiveData<Integer> databaseOperationResult = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Food> recentList = recentFoodDao.getAll();
                if(recentList != null && recentList.contains(foodToRemove)) {
                    recentFoodDao.deleteFood(foodToRemove.getName());
                    databaseOperationResult.postValue(Constants.DATABASE_REMOVE_RECENT_FOOD_OK);
                }
                else {
                    databaseOperationResult.postValue(Constants.DATABASE_REMOVE_RECENT_FOOD_ERROR);
                }
            }
        }).start();

        return databaseOperationResult;
    }

    public Date loadCurrentDate() {
        Date ret;
        SharedPreferences preferences = application
                .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);
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

    public void saveCurrentDate(Date date) {
        if (date != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        SharedPreferences preferences = application
                                .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                                        Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString(Constants.CURRENT_DATE, DateUtils.dateFormat.format(date));

                        editor.apply();
                    }
                }
            }).start();
        }
    }
}