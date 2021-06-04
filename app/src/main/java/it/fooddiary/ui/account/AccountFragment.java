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
import it.fooddiary.repositories.AppRepository;
import it.fooddiary.viewmodels.AppViewModel;
import it.fooddiary.viewmodels.AppViewModelFactory;

public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";

    private FragmentAccountBinding binding;

    private AppViewModel viewModel;

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

        viewModel = new ViewModelProvider(this,
                new AppViewModelFactory(requireActivity().getApplication(),
                        new AppRepository(requireActivity().getApplication())))
                .get(AppViewModel.class);

        viewModel.getUserProperties().observe(getViewLifecycleOwner(), new Observer<UserProperties>() {
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
