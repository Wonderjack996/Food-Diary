package it.fooddiary.ui.diary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.fooddiary.utils.Constants;

public class DiaryPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "DiaryPagerAdapter";

    private final FragmentManager fragmentManager;

    private final List<DiaryFragment> dataSet = new ArrayList<>();

    private final Calendar calendar = Calendar.getInstance();

    public DiaryPagerAdapter(@NotNull @NonNull FragmentManager fm,
                             @NotNull @NonNull Date date) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentManager = fm;
        loadFragment(date);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position >= 0 && position < dataSet.size())
            return dataSet.get(position);
        return new DiaryFragment(new Date());
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    private void loadFragment(@NotNull @NonNull Date centralDate) {
        for(Fragment frag : fragmentManager.getFragments())
            fragmentManager.beginTransaction().remove(frag).commit();
        for(int i = 0; i < dataSet.size(); ++i)
            fragmentManager.beginTransaction().remove(dataSet.get(i)).commit();
        dataSet.clear();

        for(int i = 0; i < Constants.NUM_PRELOADED_FRAGMENT /2; ++i) {
            calendar.setTime(centralDate);
            calendar.add(Calendar.DATE, -(Constants.NUM_PRELOADED_FRAGMENT/2-i));
            dataSet.add(new DiaryFragment(calendar.getTime()));
        }
        dataSet.add(new DiaryFragment(centralDate));
        for(int i = Constants.NUM_PRELOADED_FRAGMENT/2+1; i < Constants.NUM_PRELOADED_FRAGMENT; ++i) {
            calendar.setTime(centralDate);
            calendar.add(Calendar.DATE, (i-Constants.NUM_PRELOADED_FRAGMENT/2));
            dataSet.add(new DiaryFragment(calendar.getTime()));
        }
    }
}