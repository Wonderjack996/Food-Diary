package it.fooddiary.services;

import it.fooddiary.models.edamam_models.EdamamResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to get foods by Edamam API
 */
public interface IFoodServices {

    @GET("api/food-database/v2/parser")
    Call<EdamamResponse> getFoodsByName(@Query("app_id") String appId,
                                        @Query("app_key") String appKey,
                                        @Query("ingr") String ingredient,
                                        @Query("nutrition-type") String nutritionType,
                                        @Query("categoryLabel") String categoryLabel,
                                        @Query("category") String category);
}
