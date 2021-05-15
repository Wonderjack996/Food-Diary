package it.fooddiary.ui.diary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiaryPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager fragmentManager;

    private final List<DiaryFragment> dataSet = new ArrayList<>();

    private final Calendar calendar = Calendar.getInstance();

    public DiaryPagerAdapter(FragmentManager fm, Date date, int numPreloadedFragment) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentManager = fm;
        loadFragment(date, numPreloadedFragment);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getItemPosition(Object object){
        /*
         * called when the fragments are reordered to get the
         * changes.
         */
        int idx = dataSet.indexOf((DiaryFragment) object);
        return idx < 0 ? POSITION_NONE : idx;
    }

    @Override
    public long getItemId(int position) {
        /*
         * map to a position independent ID, because this
         * adapter reorders fragments
         */
        return System.identityHashCode(dataSet.get(position));
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    public void addPrevious() {
        calendar.setTime(dataSet.get(0).getCurrentDate());
        calendar.add(Calendar.DATE, -1);

        dataSet.add(0, new DiaryFragment(calendar.getTime()));

        notifyDataSetChanged();
    }

    public void reorderFragment(int position, int numFragmentToLoad) {
        loadFragment(dataSet.get(position).getCurrentDate(), numFragmentToLoad);
        notifyDataSetChanged();
    }

    public void addLast() {
        calendar.setTime(dataSet.get(dataSet.size()-1).getCurrentDate());
        calendar.add(Calendar.DATE, 1);

        dataSet.add(new DiaryFragment(calendar.getTime()));

        notifyDataSetChanged();
    }

    private void loadFragment(Date centralDate, int numFragment) {
        for(int i = 0; i < dataSet.size(); ++i)
            fragmentManager.beginTransaction().remove(dataSet.get(i)).commit();
        dataSet.clear();

        for(int i = 0; i < numFragment/2; ++i) {
            calendar.setTime(centralDate);
            calendar.add(Calendar.DATE, -(numFragment/2-i));
            dataSet.add(new DiaryFragment(calendar.getTime()));
        }
        dataSet.add(new DiaryFragment(centralDate));
        for(int i = numFragment/2+1; i < numFragment; ++i) {
            calendar.setTime(centralDate);
            calendar.add(Calendar.DATE, (i-numFragment/2));
            dataSet.add(new DiaryFragment(calendar.getTime()));
        }
    }
}
