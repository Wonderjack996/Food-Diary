package it.fooddiary.ui.search;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.fooddiary.R;

public class FoodAdapter extends BaseAdapter {

    private List<String> stringList;
    private Activity activity;

    public FoodAdapter(List<String> stringList, Activity activity){
        this.stringList = stringList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public String getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.food_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.foodName)).setText(getItem(position));
        ((TextView) convertView.findViewById(R.id.foodCalories)).setText(getItem(position));


        return convertView;
    }


}
