package com.example.foodiva.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiva.Database.DatabaseHelper;
import com.example.foodiva.Database.FoodItemModel;
import com.example.foodiva.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class DynamicFragment extends Fragment {
    ArrayList<FoodItemModel> arrayList;
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;


    public static DynamicFragment newInstance() {
        return new DynamicFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: view created");
        View view = inflater.inflate(R.layout.dynamic_recyclerview_fragment, container, false);

        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Log.d(TAG, "onCreateView: recyclerview populated");
    }

    private void initViews(View view) {

        Log.d(TAG, "initViews: ");
        TextView textView = view.findViewById(R.id.commonTextView);
        recyclerView = view.findViewById(R.id.recyclerview_fragment);
        databaseHelper = new DatabaseHelper(getActivity());

        // Database ID starts 1, TabLayout positions start at 0.
        // This is a very silly way of doing this but it works for now.
        // For some reason not all categories are loaded
        String categoryID = String.valueOf(getArguments().getInt("position") + 1);

        String categoryTitle = databaseHelper.getCategoryTitleByID(categoryID);

        // need to get tablayout bundle
        arrayList = databaseHelper.getFoodItemsByCategory(categoryTitle);
//        arrayList = databaseHelper.getAllFoodItems();
        itemAdapter = new ItemAdapter(getContext(), (AppCompatActivity) getActivity(), arrayList);

        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        textView.setText("Category: " + categoryID);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
