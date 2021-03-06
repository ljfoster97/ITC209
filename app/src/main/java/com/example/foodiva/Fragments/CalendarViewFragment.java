package com.example.foodiva.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.foodiva.Activities.MainActivity;
import com.example.foodiva.Database.CategoryModel;
import com.example.foodiva.Database.DatabaseHelper;
import com.example.foodiva.R;
import com.example.foodiva.RecyclerView.DynamicFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CalendarViewFragment extends Fragment {
    ArrayList<CategoryModel> arrayList;
    View view;
    // Declarations
    private MaterialCalendarView mCalendarView;
    private CalendarDay currentDate;
    private TextView mTextViewCurrentDate;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        // initViews for category tabs
        initViews();

        // Get textview to display date
        mTextViewCurrentDate = getActivity().findViewById(R.id.tv_calendar_selecteddate);
        // Get calendarview
        mCalendarView = getActivity().findViewById(R.id.calendarview_calendar_calendar);
        // Set listener to update textview when a  day is selected
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mTextViewCurrentDate.setText(String.valueOf(date));
            }

        });

        // Get NavController
        final NavController navController = Navigation.findNavController(view);
    }

    private void initViews() {
        Log.d(TAG, "initViews: ");
        // Get ViewPager.
        mViewPager = getActivity().findViewById(R.id.viewpager_calendar_tabpages);
        // Get TabLayout
        mTabLayout = getActivity().findViewById(R.id.tablayout_calendar_categorytabs);
        mViewPager.setOffscreenPageLimit(5);
        // Set up ViewPager to update based on tabs.
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
        // arrayList of categories.
        arrayList = new ArrayList<CategoryModel>(databaseHelper.getCategories());

        Log.d(TAG, "setDynamicFragmentToTabLayout: ");

        // For every category in the arrayList, create a new tab and set the appropriate name for the tab.
        for (int i = 0; i < arrayList.size(); i++) {

            mTabLayout.addTab(mTabLayout.newTab().setText(arrayList.get(i).getCategoryTitle()));
        }
        // Call dynamicFragmentAdapter to create fragments for each tab.
        DynamicFragmentAdapter mDynamicFragmentAdapter = new DynamicFragmentAdapter(getChildFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mDynamicFragmentAdapter);
        mViewPager.setCurrentItem(0);
    }
}