package it.fooddiary.utils;

import android.app.Application;

import it.fooddiary.databases.AppRoomDatabase;
import it.fooddiary.services.IFoodServices;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicesLocator {

    private static ServicesLocator instance = null;

    private ServicesLocator() {}

    public static ServicesLocator getInstance() {
        if (instance == null) {
            synchronized(ServicesLocator.class) {
                instance = new ServicesLocator();
            }
        }
        return instance;
    }

    public IFoodServices getFoodServicesWithRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(IFoodServices.class);
    }

    public AppRoomDatabase getAppDatabase(Application application) {
        return AppRoomDatabase.getDatabase(application);
    }
}
