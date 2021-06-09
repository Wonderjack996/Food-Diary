package it.fooddiary.repositories;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.models.UserProperties;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.ServicesLocator;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;

public class UserRepository {

    private final Application application;
    private final FirebaseAuth auth;
    private final DatabaseReference database;

    private static MutableLiveData<UserProperties> userPropertiesMutableLiveData;

    public UserRepository(Application application) {
        this.application = application;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_NAME).getReference();
        if (userPropertiesMutableLiveData == null) {
            userPropertiesMutableLiveData = new MutableLiveData<>();
            UserProperties currentProperties = loadUserPropertiesFromSharedPreferences();
            userPropertiesMutableLiveData.postValue(currentProperties);
        }
    }

    public LiveData<Integer> loginWithMailAndPassword(String mail, String password) {
        MutableLiveData<Integer> result = new MutableLiveData<>();

        if (mail != null && password != null)
            auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();

                        saveUserTokenInSharedPreferences(user.getUid());
                        database.child(Constants.FIREBASE_USERS_TABLE).child(user.getUid())
                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    UserProperties userProperties = task.getResult()
                                            .getValue(UserProperties.class);
                                    if (userProperties != null) {
                                        saveUserPropertiesInSharedPreferences(userProperties);
                                        userPropertiesMutableLiveData.postValue(userProperties);
                                    }
                                }
                            }
                        });

                        result.postValue(Constants.FIREBASE_LOGIN_OK);
                    } else
                        result.postValue(Constants.FIREBASE_LOGIN_ERROR);
                }
            });
        else
            result.setValue(Constants.FIREBASE_LOGIN_ERROR);

        return result;
    }

    public LiveData<Integer> registerWithMailAndPassword(String mail, String password,
                                                         UserProperties insertData) {
        MutableLiveData<Integer> result = new MutableLiveData<>();

        if (mail != null && password != null)
            auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();

                        saveUserTokenInSharedPreferences(user.getUid());
                        saveUserPropertiesInSharedPreferences(insertData);
                        database.child(Constants.FIREBASE_USERS_TABLE)
                                .child(user.getUid()).setValue(insertData);
                        userPropertiesMutableLiveData.postValue(insertData);

                        result.postValue(Constants.FIREBASE_REGISTER_OK);
                    } else
                        result.postValue(Constants.FIREBASE_REGISTER_ERROR);
                }
            });
        else
            result.setValue(Constants.FIREBASE_REGISTER_ERROR);

        return result;
    }

    public void logout() {
        deleteUserAuthIdFromSharedPreferences();
        deleteUserPropertiesFromSharedPreferences();
        deleteCurrentDateFromSharedPreferences();
        userPropertiesMutableLiveData = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServicesLocator.getInstance().getAppDatabase(application).clearAllTables();
            }
        }).start();
    }

    public LiveData<UserProperties> getUserProperties() {
        return userPropertiesMutableLiveData;
    }

    public void setUserProperties(UserProperties newProperties) {
        saveUserPropertiesInSharedPreferences(newProperties);
        userPropertiesMutableLiveData.setValue(newProperties);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String id = getUserAuthIdFromSharedPreferences();
                database.child(Constants.FIREBASE_USERS_TABLE).child(id)
                        .setValue(newProperties);
            }
        }).start();
    }

    public String getUserAuthIdFromSharedPreferences() {
        SharedPreferences preferences = application
                .getSharedPreferences(Constants.FIREBASE_USER_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);
        return preferences.getString(Constants.FIREBASE_USER_ID, null);
    }

    private void deleteCurrentDateFromSharedPreferences() {
        SharedPreferences sharedPref = application.getSharedPreferences(
                Constants.CURRENT_DATE_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove(Constants.CURRENT_DATE);

        editor.apply();
    }

    private void deleteUserPropertiesFromSharedPreferences() {
        SharedPreferences sharedPref = application.getSharedPreferences(
                Constants.PERSONAL_DATA_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove(Constants.USER_AGE);
        editor.remove(Constants.USER_GENDER);
        editor.remove(Constants.USER_WEIGHT_KG);
        editor.remove(Constants.USER_HEIGHT_CM);
        editor.remove(Constants.USER_ACTIVITY_LEVEL);

        editor.apply();
    }

    private void saveUserPropertiesInSharedPreferences(@NotNull UserProperties userProperties) {
        SharedPreferences sharedPref = application.getSharedPreferences(
                Constants.PERSONAL_DATA_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(Constants.USER_AGE, userProperties.getAge());
        editor.putInt(Constants.USER_GENDER, userProperties.getGender());
        editor.putInt(Constants.USER_WEIGHT_KG, userProperties.getWeightKg());
        editor.putInt(Constants.USER_HEIGHT_CM, userProperties.getHeightCm());
        editor.putInt(Constants.USER_ACTIVITY_LEVEL, userProperties.getActivityLevel());

        editor.apply();
    }

    @NotNull
    private UserProperties loadUserPropertiesFromSharedPreferences() {
        SharedPreferences preferences = application
                .getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);

        int age = preferences.getInt(Constants.USER_AGE, Constants.MAX_AGE);
        int gender = preferences.getInt(Constants.USER_GENDER, Constants.GENDER_MALE);
        int heightCm = preferences.getInt(Constants.USER_HEIGHT_CM, Constants.MAX_HEIGHT_CM);
        int weightKg = preferences.getInt(Constants.USER_WEIGHT_KG, Constants.MAX_WEIGHT_KG);
        int activityLevel = preferences.getInt(Constants.USER_ACTIVITY_LEVEL, Constants.OTHER);

        return new UserProperties(age, gender, heightCm, weightKg, activityLevel);
    }

    private void deleteUserAuthIdFromSharedPreferences() {
        SharedPreferences sharedPref = application.getSharedPreferences(
                Constants.FIREBASE_USER_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.FIREBASE_USER_ID);
        editor.apply();
    }

    private void saveUserTokenInSharedPreferences(String userToken) {
        if (userToken != null && !userToken.isEmpty()) {
            SharedPreferences sharedPref = application.getSharedPreferences(
                    Constants.FIREBASE_USER_PREFERENCES_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constants.FIREBASE_USER_ID, userToken);
            editor.apply();
        }
    }
}
