package it.fooddiary.ui.search;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import it.fooddiary.MainActivity;
import it.fooddiary.R;



public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        MainActivity main = (MainActivity) getActivity();
        main.setSearchToolbar();

        root.findViewById(R.id.addCalories_floatingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder addCaloriesDialog = new MaterialAlertDialogBuilder(getActivity());
                NumberPicker numberPicker = new NumberPicker(getActivity());
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(10000);
                numberPicker.setValue(200);
                addCaloriesDialog.setTitle(R.string.addCalories);
                addCaloriesDialog.setView(numberPicker);
                addCaloriesDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                addCaloriesDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });
                addCaloriesDialog.show();
            }
        });

        return root;
    }
}

