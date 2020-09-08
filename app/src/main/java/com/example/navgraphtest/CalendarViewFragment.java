package com.example.navgraphtest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CalendarViewFragment extends Fragment {

    private MaterialCalendarView mCalendarView;
    private CalendarDay currentDate;
    private TextView mTextViewCurrentDate;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    ArrayList<CategoryModel> arrayList;

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        // Update ActionBar titles in MainActivity to reflect current fragment
        // Actionbar is in main layout not the fragments.
        ((MainActivity) getActivity()).setActionBarTitle("Calendar View");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("View your consumption history");

        // Open fragment with today's date selected on calendarview.
        mCalendarView.setSelectedDate(CalendarDay.today());

        // initViews for category tabs
        initViews();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_view, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        mTextViewCurrentDate = getActivity().findViewById(R.id.currentDateTextView);
        mCalendarView = getActivity().findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener(){
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mTextViewCurrentDate.setText(String.valueOf(date));
            }

        });

        final NavController navController = Navigation.findNavController(view);
    }

    private void initViews(){
        Log.d(TAG, "initViews: ");

        mViewPager = getActivity().findViewById(R.id.viewPagerCalendarView);
        mTabLayout =  getActivity().findViewById(R.id.tabLayoutCalendarView);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setDynamicFragmentToTabLayout();

    }

    private void setDynamicFragmentToTabLayout() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        arrayList = new ArrayList<CategoryModel>(databaseHelper.getCategories());

        Log.d(TAG, "setDynamicFragmentToTabLayout: ");

        for (int i = 0; i < arrayList.size() ; i++) {

            mTabLayout.addTab(mTabLayout.newTab().setText(arrayList.get(i).getCategoryTitle()));
        }



        DynamicFragmentAdapter mDynamicFragmentAdapter = new DynamicFragmentAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mDynamicFragmentAdapter);
        mViewPager.setCurrentItem(0);
    }
}