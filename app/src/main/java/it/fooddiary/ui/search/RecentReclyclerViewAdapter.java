package it.fooddiary.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.fooddiary.R;

public class RecentReclyclerViewAdapter extends  RecyclerView.Adapter<RecentReclyclerViewAdapter.RecentViewHolder>{

    private List<String> stringList;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String s);
    }

    public RecentReclyclerViewAdapter(List<String> stringList,OnItemClickListener onItemClickListener){
        this.stringList = stringList;
        listener = onItemClickListener;
    }


    @NonNull
    @Override
    public RecentReclyclerViewAdapter.RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);

        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        holder.bind(stringList.get(position));

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private final TextView calories;

        public RecentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.foodName);
            calories = itemView.findViewById(R.id.foodCalories);
        }

        public void bind(String s){
            titleTextView.setText(s);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(s);
                    calories.setText(String.valueOf(s) + " KCals");
                }
            });

        }
    }
}
