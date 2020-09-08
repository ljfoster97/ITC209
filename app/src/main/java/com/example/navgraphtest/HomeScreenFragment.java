package com.example.navgraphtest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;

public class HomeScreenFragment extends Fragment {
    @Override
    public void onResume(){
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

        final NavController navController = Navigation.findNavController(view);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
                fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeScreenFragment_to_categoryQuickAddFragment);
            }
        });

        GraphView graph = view.findViewById(R.id.graphView);
                graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeScreenFragment_to_calendarViewFragment);

            }
        });


    }
}