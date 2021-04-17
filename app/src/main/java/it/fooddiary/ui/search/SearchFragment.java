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

    private TabLayout TabItem;
    private MaterialAlertDialogBuilder addCaloriesDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        MainActivity main = (MainActivity) getActivity();
        main.setSearchToolbar();
        addCaloriesDialog = new MaterialAlertDialogBuilder(main);
        NumberPicker numberPicker;
        numberPicker = new NumberPicker(main);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10000);
        addCaloriesDialog.setView(numberPicker);
        addCaloriesDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        addCaloriesDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });


        return root;
    }
    public void onAddCaloriesClick(View root){

    }



}

