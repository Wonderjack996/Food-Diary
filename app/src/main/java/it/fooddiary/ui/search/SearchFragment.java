package it.fooddiary.ui.search;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import it.fooddiary.MainActivity;
import it.fooddiary.R;



public class SearchFragment extends Fragment {

    private TabLayout TabItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        MainActivity main = (MainActivity) getActivity();
        main.setSearchToolbar();

        return root;
    }



}

