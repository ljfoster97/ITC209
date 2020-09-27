package com.example.navgraphtest.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.navgraphtest.Activities.MainActivity;
import com.example.navgraphtest.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;

public class HomeScreenFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();

        // Update ActionBar titles in MainActivity to reflect current fragment
        ((MainActivity) getActivity()).setActionBarTitle("Today's Consumption");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the navController.
        final NavController navController = Navigation.findNavController(view);


        // Set onClickListener to navigate to the calendarViewFragment when graph is clicked using navController.
        GraphView graph = view.findViewById(R.id.graphView);
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeScreenFragment_to_calendarViewFragment);

            }
        });


    }
}