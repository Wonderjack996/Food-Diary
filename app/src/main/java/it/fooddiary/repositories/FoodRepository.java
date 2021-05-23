package it.fooddiary.repositories;

import android.util.Log;

import it.fooddiary.models.EdamamResponse;
import it.fooddiary.services.IFoodServices;
import it.fooddiary.util.Constants;
import it.fooddiary.util.ServicesLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodRepository {

    private static final String TAG = "FoodRepository";

    private final IFoodServices foodServices;

    public FoodRepository() {
        this.foodServices = ServicesLocator.getInstance().getFoodServicesWithRetrofit();
    }

    public void fetchFood(String ingredient) {
        Call<EdamamResponse> call = foodServices.getFoodsByName(Constants.API_APP_ID,
                Constants.API_APP_KEY, ingredient, Constants.API_NUTRITION_TYPE_LOGGING,
                Constants.API_CATEGORY_LABEL_FOOD, Constants.API_CATEGORY_GENERIC_FOOD);

        call.enqueue(new Callback<EdamamResponse>() {
            @Override
            public void onResponse(Call<EdamamResponse> call, Response<EdamamResponse> response) {
                Log.d(TAG, response.body().getText());
            }

            @Override
            public void onFailure(Call<EdamamResponse> call, Throwable t) {
                Log.d(TAG, "Fail");
            }
        });
    }
}
