package it.fooddiary.ui.diary;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiaryPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "DiaryPagerAdapter";

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
    public int getCount() {
        return dataSet.size();
    }

    private void loadFragment(Date centralDate, int numFragment) {
        for(Fragment frag : fragmentManager.getFragments())
            fragmentManager.beginTransaction().remove(frag).commit();
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