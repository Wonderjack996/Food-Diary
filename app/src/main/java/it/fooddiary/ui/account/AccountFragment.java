package it.fooddiary.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import it.fooddiary.R;
import it.fooddiary.databinding.FragmentAccountBinding;
import it.fooddiary.utils.Constants;

public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";

    private FragmentAccountBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_account, menu);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);


        SharedPreferences preferences =
                getActivity().getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);

        Log.d(TAG, preferences.getString(Constants.USER_GENDER, null));
        //Log.d(TAG, preferences.getInt(Constants.USER_AGE, 0));
        binding.ageText.setText("" + preferences.getInt(Constants.USER_AGE, 0));
        binding.genderText.setText(preferences.getString(Constants.USER_GENDER, null));

        return binding.getRoot();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_edit:
                Intent intent = new Intent(this.getContext(), EditAccountActivity.class);
                startActivity(intent);
                break;
            default:
                return false;
        }
        return true;
    }
}
