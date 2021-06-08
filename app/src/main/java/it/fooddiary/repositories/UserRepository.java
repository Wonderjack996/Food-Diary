package it.fooddiary.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.utils.Constants;

public class UserRepository {

    private final Application application;
    private final FirebaseAuth auth;

    public UserRepository(Application application) {
        this.application = application;
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<Integer> loginWithMailAndPassword(String mail, String password) {
        MutableLiveData<Integer> result = new MutableLiveData<>();

        if (mail != null && password != null)
            auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        saveUserTokenInSharedPreferences(user.getIdToken(false)
                                .getResult().getToken());
                        result.postValue(Constants.FIREBASE_LOGIN_OK);
                    } else
                        result.postValue(Constants.FIREBASE_LOGIN_ERROR);
                }
            });
        else
            result.setValue(Constants.FIREBASE_LOGIN_ERROR);

        return result;
    }

    public LiveData<Integer> registerWithMailAndPassword(String mail, String password) {
        MutableLiveData<Integer> result = new MutableLiveData<>();

        if (mail != null && password != null)
            auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();

                        saveUserTokenInSharedPreferences(user.getIdToken(false)
                                .getResult().getToken());
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
        deleteUserTokenFromSharedPreferences();
    }

    private void deleteUserTokenFromSharedPreferences() {
        SharedPreferences sharedPref = application.getSharedPreferences(
                Constants.FIREBASE_USER_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.FIREBASE_USER_TOKEN);
        editor.apply();
    }

    private void saveUserTokenInSharedPreferences(String userToken) {
        if (userToken != null && !userToken.isEmpty()) {
            SharedPreferences sharedPref = application.getSharedPreferences(
                    Constants.FIREBASE_USER_PREFERENCES_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constants.FIREBASE_USER_TOKEN, userToken);
            editor.apply();
        }
    }
}
