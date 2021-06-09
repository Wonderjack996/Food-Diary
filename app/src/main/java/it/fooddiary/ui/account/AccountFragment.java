package it.fooddiary.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import it.fooddiary.R;
import it.fooddiary.databinding.FragmentAccountBinding;
import it.fooddiary.models.UserProperties;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.repositories.UserRepository;
import it.fooddiary.ui.LaunchScreenActivity;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.ui.login.LoginActivity;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;
import it.fooddiary.viewmodels.user.UserViewModel;
import it.fooddiary.viewmodels.user.UserViewModelFactory;

public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";

    private FragmentAccountBinding binding;

    private FoodViewModel foodViewModel;
    private UserViewModel userViewModel;

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
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_account, container, false);

        foodViewModel = new ViewModelProvider(this,
                new FoodViewModelFactory(requireActivity().getApplication(),
                        new FoodRepository(requireActivity().getApplication())))
                .get(FoodViewModel.class);
        userViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(requireActivity().getApplication(),
                        new UserRepository(requireActivity().getApplication())))
                .get(UserViewModel.class);

        userViewModel.getUserProperties().observe(getViewLifecycleOwner(), new Observer<UserProperties>() {
            @Override
            public void onChanged(UserProperties userProperties) {
                binding.setUserProperties(userProperties);
                binding.invalidateAll();
            }
        });

        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item_edit:
                intent = new Intent(this.getContext(), EditAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.item_logout:
                // clear meal table, recent food table
                userViewModel.logout();
                intent = new Intent(this.getContext(), LoginActivity.class);
                startActivity(intent);
                this.requireActivity().finish();
                break;
            default:
                return false;
        }
        return true;
    }
}
